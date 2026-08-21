package com.example.wheelofchance.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object DashboardRoute : NavKey

@Serializable
data class EditorRoute(val wheelId: Long = -1L) : NavKey

@Serializable
data class SpinRoute(val wheelId: Long) : NavKey
