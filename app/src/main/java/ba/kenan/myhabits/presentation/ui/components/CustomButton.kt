package ba.kenan.myhabits.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import ba.kenan.myhabits.R
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme
import ba.kenan.myhabits.presentation.ui.theme.blackContentColor
import ba.kenan.myhabits.presentation.ui.theme.yellowBackgroundColor


@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(dimensionResource(R.dimen.icon_size_xxsmall)),
    containerColor: Color = yellowBackgroundColor,
    contentColor: Color = blackContentColor,
    borderColor: Color = yellowBackgroundColor,
    disabledContainerColor: Color = yellowBackgroundColor,
    disabledContentColor: Color = blackContentColor,
    textStyle: TextStyle = MaterialTheme.typography.headlineLarge,
    contentPadding: PaddingValues = PaddingValues(dimensionResource(R.dimen.padding_medium))
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        shape = shape,
        contentPadding = contentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
        ),
        border = BorderStroke(dimensionResource(id = R.dimen.border_thin), borderColor),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = textStyle
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CtaButtonPreview() {
    MyHabitsAppTheme {
        CustomButton(
            text = "Login with Office 365",
            onClick = {  }
        )
    }
}
