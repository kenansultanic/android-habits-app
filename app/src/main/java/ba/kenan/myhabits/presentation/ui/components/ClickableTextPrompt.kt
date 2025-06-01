package ba.kenan.myhabits.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme

@Composable
fun ClickableTextPrompt(
    onTextClick: () -> Unit,
    firstText: String,
    secondText: String,
    tag: String,
    annotation: String
) {
    val annotatedText = buildAnnotatedString {
        append(firstText)

        pushStringAnnotation(
            tag = tag,
            annotation = annotation
        )
        withStyle(
            style = SpanStyle(fontWeight = FontWeight.Medium)
        ) {
            append(secondText)
        }
        pop()
    }

    ClickableText(
        text = annotatedText,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier.fillMaxWidth(),
        onClick = { offset ->
            annotatedText.getStringAnnotations(tag = tag, start = offset, end = offset)
                .firstOrNull()?.let {
                    onTextClick()
                }
        }
    )
}

@Preview
@Composable
private fun ClickableTextPromptPreview() {
    MyHabitsAppTheme {
        ClickableTextPrompt(
            onTextClick = {  },
            firstText = "Already have an account? ",
            secondText = "Login",
            tag = "LOGIN",
            annotation = "login"
        )
    }
}
