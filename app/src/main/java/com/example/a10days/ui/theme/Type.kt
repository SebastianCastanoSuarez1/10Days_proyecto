package com.example.a10days.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.a10days.R

val CrimSonPro = FontFamily(
    Font(R.font.crimsonpro_bold),
    Font(R.font.crimsonpro_regular)
)
val poppins = FontFamily(
    Font(R.font.poppins_bold),
)
// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = CrimSonPro,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp
    ),
    displayMedium = TextStyle(
        fontFamily = poppins,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp

    ),
    labelSmall = TextStyle(
        fontFamily = CrimSonPro,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = CrimSonPro,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)