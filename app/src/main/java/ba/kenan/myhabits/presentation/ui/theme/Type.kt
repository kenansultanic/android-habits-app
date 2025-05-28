package ba.kenan.myhabits.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ba.kenan.myhabits.R


val helveticaNeue = FontFamily(
    Font(R.font.helvetica_neue_roman, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.helvetica_neue_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.helvetica_neue_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.helvetica_neue_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.helvetica_neue_black, FontWeight.Black, FontStyle.Normal),
    Font(R.font.helvetica_neue_black_italic, FontWeight.Black, FontStyle.Italic),
    Font(R.font.helvetica_neue_thin, FontWeight.Thin, FontStyle.Normal),
    Font(R.font.helvetica_neue_thin_italic, FontWeight.Thin, FontStyle.Italic),
    Font(R.font.helvetica_neue_ultra_light, FontWeight.ExtraLight, FontStyle.Normal),
    Font(R.font.helvetica_neue_ultra_light_italic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.helvetica_neue_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.helvetica_neue_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.helvetica_neue_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.helvetica_neue_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.helvetica_neue_heavy, FontWeight.ExtraBold, FontStyle.Normal),
    Font(R.font.helvetica_neue_heavy_italic, FontWeight.ExtraBold, FontStyle.Italic)
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = helveticaNeue,
        fontWeight = FontWeight.Medium,
        fontSize = 40.sp,
        lineHeight = 48.sp,
        letterSpacing = (-2).sp
    ),
    titleSmall = TextStyle(
        fontFamily = helveticaNeue,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = helveticaNeue,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = helveticaNeue,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 26.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = helveticaNeue,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    bodySmall = TextStyle(
        fontFamily = helveticaNeue,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = helveticaNeue,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 30.sp
    ),
    displaySmall = TextStyle(
        fontFamily = helveticaNeue,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp
    ),
    labelSmall = TextStyle(
        fontFamily = helveticaNeue,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 12.sp
    ),
    labelMedium = TextStyle(
        fontFamily = helveticaNeue,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.43).sp
    )
)