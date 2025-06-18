package com.martorell.albert.meteomartocompose.ui.screens.shared

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.martorell.albert.meteomartocompose.R

@Composable
fun CityText(
    fontSize: TextUnit = 25.sp,
    fontWeight: FontWeight = FontWeight.W400,
    contentFix: String,
    contentDynamic: String = "",
    colorFix: Color = Color.DarkGray,
    colorDynamic: Color = Color.DarkGray,
) {

    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = colorFix)) {
                append(contentFix)
            }
            if (contentDynamic.isNotEmpty()) {
                withStyle(style = SpanStyle(color = colorDynamic)) {
                    append(contentDynamic)
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontWeight = fontWeight,
        fontSize = fontSize

    )

    Spacer(Modifier.height(dimensionResource(R.dimen.medium_spacer)))

}


