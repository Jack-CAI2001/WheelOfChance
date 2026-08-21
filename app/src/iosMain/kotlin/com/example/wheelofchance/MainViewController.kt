package com.example.wheelofchance

import androidx.compose.ui.window.ComposeUIViewController
import com.example.wheelofchance.data.WheelRepository
import com.example.wheelofchance.data.local.getDatabaseBuilder
import com.example.wheelofchance.data.local.getRoomDatabase
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
    val builder = getDatabaseBuilder()
    val database = getRoomDatabase(builder)
    val repository = WheelRepository(
        database.wheelDao(),
        database.entryDao()
    )
    
    App(repository)
}
