package com.example.wheelofchance

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.wheelofchance.data.WheelRepository
import com.example.wheelofchance.ui.EditorViewModel
import com.example.wheelofchance.ui.SpinViewModel
import com.example.wheelofchance.ui.WheelViewModel
import com.example.wheelofchance.ui.dashboard.DashboardScreen
import com.example.wheelofchance.ui.editor.EntryEditorScreen
import com.example.wheelofchance.ui.navigation.DashboardRoute
import com.example.wheelofchance.ui.navigation.EditorRoute
import com.example.wheelofchance.ui.navigation.SpinRoute
import com.example.wheelofchance.ui.spin.SpinScreen
import com.example.wheelofchance.ui.theme.WheelOfChanceTheme
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

private val nav3SerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(DashboardRoute::class)
        subclass(EditorRoute::class)
        subclass(SpinRoute::class)
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun App(repository: WheelRepository) {
    WheelOfChanceTheme {
        val backStack = rememberNavBackStack(
            configuration = SavedStateConfiguration {
                serializersModule = nav3SerializersModule
            },
            DashboardRoute
        )
        val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

        val entryProvider = entryProvider {
            entry<DashboardRoute>(
                metadata = ListDetailSceneStrategy.listPane()
            ) {
                val viewModel: WheelViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            WheelViewModel(repository)
                        }
                    }
                )
                DashboardScreen(
                    viewModel = viewModel,
                    onWheelClick = { id -> 
                        backStack.add(EditorRoute(id)) 
                    },
                    onSpinClick = { id -> 
                        backStack.add(SpinRoute(id))
                    }
                )
            }

            entry<EditorRoute>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { key ->
                val viewModel: EditorViewModel = viewModel(
                    key = "Editor_${key.wheelId}",
                    factory = viewModelFactory {
                        initializer {
                            EditorViewModel(repository, key.wheelId)
                        }
                    }
                )
                EntryEditorScreen(
                    viewModel = viewModel,
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            entry<SpinRoute>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { key ->
                val viewModel: SpinViewModel = viewModel(
                    key = "Spin_${key.wheelId}",
                    factory = viewModelFactory {
                        initializer {
                            SpinViewModel(repository, key.wheelId)
                        }
                    }
                )
                SpinScreen(
                    viewModel = viewModel,
                    onBack = { backStack.removeLastOrNull() }
                )
            }
        }

        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            sceneStrategies = listOf(listDetailStrategy),
            entryProvider = entryProvider
        )
    }
}
