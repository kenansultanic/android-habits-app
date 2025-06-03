package ba.kenan.myhabits.presentation.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import ba.kenan.myhabits.R
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        shape = RoundedCornerShape(dimensionResource(R.dimen.rounded_corner)),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_medium)),
        modifier = modifier
    ) {
        Text(
            text = text,
            style =  MaterialTheme.typography.labelLarge
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
