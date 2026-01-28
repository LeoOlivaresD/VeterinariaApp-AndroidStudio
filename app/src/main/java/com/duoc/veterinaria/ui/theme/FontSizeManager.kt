package com.duoc.veterinaria.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object FontSizeManager {
    var fontScale by mutableStateOf(1.0f)
        private set

    fun increaseFontSize() {
        if (fontScale < 1.5f) {
            fontScale += 0.1f
        }
    }

    fun decreaseFontSize() {
        if (fontScale > 0.8f) {
            fontScale -= 0.1f
        }
    }

    fun resetFontSize() {
        fontScale = 1.0f
    }
}