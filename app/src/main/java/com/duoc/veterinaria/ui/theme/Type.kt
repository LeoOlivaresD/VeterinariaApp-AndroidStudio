package com.duoc.veterinaria.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun scaledTypography(): Typography {
    val scale = FontSizeManager.fontScale

    return Typography(
        displayLarge = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = (57.sp.value * scale).sp
        ),
        displayMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = (45.sp.value * scale).sp
        ),
        displaySmall = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = (36.sp.value * scale).sp
        ),
        headlineLarge = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = (32.sp.value * scale).sp
        ),
        headlineMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = (28.sp.value * scale).sp
        ),
        headlineSmall = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = (24.sp.value * scale).sp
        ),
        titleLarge = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = (22.sp.value * scale).sp
        ),
        titleMedium = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = (16.sp.value * scale).sp
        ),
        titleSmall = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = (14.sp.value * scale).sp
        ),
        bodyLarge = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = (16.sp.value * scale).sp
        ),
        bodyMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = (14.sp.value * scale).sp
        ),
        bodySmall = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = (12.sp.value * scale).sp
        ),
        labelLarge = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = (14.sp.value * scale).sp
        ),
        labelMedium = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = (12.sp.value * scale).sp
        ),
        labelSmall = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = (11.sp.value * scale).sp
        )
    )
}