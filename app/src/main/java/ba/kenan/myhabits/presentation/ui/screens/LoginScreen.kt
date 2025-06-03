package ba.kenan.myhabits.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ba.kenan.myhabits.R
import ba.kenan.myhabits.presentation.ui.components.ClickableTextPrompt
import ba.kenan.myhabits.presentation.ui.components.CustomButton
import ba.kenan.myhabits.presentation.ui.components.CustomTextField
import ba.kenan.myhabits.presentation.ui.components.LoadingComponent
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme
import ba.kenan.myhabits.presentation.utils.DevicesPreview
import ba.kenan.myhabits.presentation.viewmodels.login.LoginUiState
import ba.kenan.myhabits.presentation.viewmodels.login.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginButtonClick: () -> Unit,
    navigateToRegistration: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var emailState by remember { mutableStateOf(TextFieldValue("")) }
    var passwordState by remember { mutableStateOf(TextFieldValue("")) }

    when (uiState) {
        is LoginUiState.Loading -> LoadingComponent()

        is LoginUiState.Success -> onLoginButtonClick()

        is LoginUiState.Failure -> {
            Log.e("Login", "Login error: ", (uiState as LoginUiState.Failure).error)
            viewModel.resetState()
        }

        LoginUiState.Init -> {
            LoginForm(
                onLoginButtonClick = {
                    viewModel.loginUser(
                        email = emailState.text.trim(),
                        password = passwordState.text
                    )
                },
                email = emailState,
                password = passwordState,
                onEmailValueChange = { emailState = it },
                onPasswordValueChange = { passwordState = it },
                navigateToRegistration = navigateToRegistration,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun LoginForm(
    onLoginButtonClick: () -> Unit,
    email: TextFieldValue,
    password: TextFieldValue,
    onEmailValueChange: (TextFieldValue) -> Unit,
    onPasswordValueChange: (TextFieldValue) -> Unit,
    navigateToRegistration: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = dimensionResource(R.dimen.padding_large),
                top = dimensionResource(R.dimen.padding_xlarge),
                end = dimensionResource(R.dimen.padding_large)
            ),
    ) {
        Text(
            text = stringResource(R.string.myhabits),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 60.sp
            )
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xxlarge)))
        Text(
            text = stringResource(R.string.welcome),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xmedium)))
        Text(
            text = stringResource(R.string.in_order_to_use_this_app_you_will_need_to_login),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xxxmedium)))
        CustomTextField(
            text = email,
            placeholder = stringResource(R.string.email_placeholder),
            onTextValueChange = onEmailValueChange,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        CustomTextField(
            text = password,
            password = true,
            placeholder = stringResource(R.string.password_placeholder),
            onTextValueChange = onPasswordValueChange,
            modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_xsmall))
        )
        CustomButton(
            text = stringResource(R.string.login_button),
            onClick = onLoginButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.padding_xsmall))
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xxxmedium)))
        ClickableTextPrompt(
            onTextClick = navigateToRegistration,
            firstText = stringResource(R.string.don_t_have_an_account_text_prompt),
            secondText = stringResource(R.string.register_text_prompt),
            tag = "REGISTER",
            annotation = "register"
        )
    }
}

@DevicesPreview
@Composable
private fun LoginScreenPreview() {
    MyHabitsAppTheme {
        LoginForm(
            onLoginButtonClick = {},
            email = TextFieldValue("kenan@email.com"),
            password = TextFieldValue("password123"),
            onEmailValueChange = {},
            onPasswordValueChange = {},
            navigateToRegistration = {}
        )
    }
}
