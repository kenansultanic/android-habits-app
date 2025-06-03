package ba.kenan.myhabits.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import ba.kenan.myhabits.R
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme

@Composable
fun CustomSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_default))
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Text(
            text = snackbarData.visuals.message,
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_default))
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun CustomSnackbarSnackbarPreview() {
    val mockSnackbarData = object : SnackbarData {
        override val visuals: SnackbarVisuals
            get() = object : SnackbarVisuals {
                override val actionLabel: String?
                    get() = null
                override val withDismissAction: Boolean
                    get() = false
                override val duration: SnackbarDuration
                    get() = SnackbarDuration.Short
                override val message: String
                    get() = "Login failed!"
            }

        override fun dismiss() {}
        override fun performAction() {}
    }
    MyHabitsAppTheme {
        CustomSnackbar(mockSnackbarData)
    }
}
