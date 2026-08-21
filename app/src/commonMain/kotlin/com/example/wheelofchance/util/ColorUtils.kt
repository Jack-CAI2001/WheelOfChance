package com.example.wheelofchance.util

import androidx.compose.ui.graphics.Color

fun String.toColor(): Color {
    val hex = this.removePrefix("#")
    return try {
        if (hex.length == 6) {
            Color(
                red = hex.substring(0, 2).toInt(16) / 255f,
                green = hex.substring(2, 4).toInt(16) / 255f,
                blue = hex.substring(4, 6).toInt(16) / 255f,
                alpha = 1f
            )
        } else if (hex.length == 8) {
            Color(
                red = hex.substring(2, 4).toInt(16) / 255f,
                green = hex.substring(4, 6).toInt(16) / 255f,
                blue = hex.substring(6, 8).toInt(16) / 255f,
                alpha = hex.substring(0, 2).toInt(16) / 255f
            )
        } else {
            Color.Gray
        }
    } catch (e: Exception) {
        Color.Gray
    }
}
