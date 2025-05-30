package ba.kenan.myhabits.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ba.kenan.myhabits.R
import ba.kenan.myhabits.presentation.ui.components.ClickableTextPrompt
import ba.kenan.myhabits.presentation.ui.components.CustomButton
import ba.kenan.myhabits.presentation.ui.components.CustomDatePicker
import ba.kenan.myhabits.presentation.ui.components.CustomTextField
import ba.kenan.myhabits.presentation.ui.components.LoadingComponent
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme
import ba.kenan.myhabits.presentation.utils.DevicesPreview
import ba.kenan.myhabits.presentation.viewmodels.register.RegisterUiState
import ba.kenan.myhabits.presentation.viewmodels.register.RegisterViewModel
import java.util.Date

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegisterButtonClick: () -> Unit,
    navigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var nameState by remember { mutableStateOf(TextFieldValue("")) }
    var emailState by remember { mutableStateOf(TextFieldValue("")) }
    var passwordState by remember { mutableStateOf(TextFieldValue("")) }
    var birthDateState by remember { mutableStateOf<Long?>(null) }

    when (uiState) {
        is RegisterUiState.Loading -> {
            LoadingComponent()
        }

        is RegisterUiState.Success -> {
            onRegisterButtonClick()
        }

        is RegisterUiState.Failure -> {
            Log.e("Register", "Greška pri registraciji", (uiState as RegisterUiState.Failure).error)
        }

        RegisterUiState.Init -> {
            RegisterScreen(
                onRegisterButtonClick = {
                    viewModel.registerUser(
                        email = emailState.text.trim(),
                        password = passwordState.text,
                        name = nameState.text.trim(),
                        birthDate = birthDateState?.let { Date(it) } ?: Date()
                    )
                },
                name = nameState,
                email = emailState,
                password = passwordState,
                birthday = birthDateState,
                onNameValueChange = { nameState = it },
                onEmailValueChange = { emailState = it },
                onPasswordValueChange = { passwordState = it },
                onBirthdayValueChange = { birthDateState = it },
                navigateToLogin = navigateToLogin,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun RegisterScreen(
    onRegisterButtonClick: () -> Unit,
    name: TextFieldValue,
    email: TextFieldValue,
    password: TextFieldValue,
    birthday: Long?,
    onNameValueChange: (TextFieldValue) -> Unit,
    onEmailValueChange: (TextFieldValue) -> Unit,
    onPasswordValueChange: (TextFieldValue) -> Unit,
    onBirthdayValueChange: (Long?) -> Unit,
    navigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 32.dp, top = 40.dp, end = 32.dp),
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
            text = stringResource(R.string.in_order_to_use_this_app_you_will_need_to_register),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xxmedium)))
        CustomTextField(
            text = name,
            placeholder = stringResource(R.string.full_name_placeholder),
            onTextValueChange = onNameValueChange,
            modifier = Modifier.padding(vertical = 4.dp)
        )
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
            modifier = Modifier.padding(vertical = 4.dp)
        )
        CustomDatePicker(
            selectedDateMillis = birthday,
            textFieldPlaceholder = stringResource(R.string.date_of_birth_placeholder),
            setSelectedDate = onBirthdayValueChange,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        CustomButton(
            text = stringResource(R.string.register_button),
            onClick = onRegisterButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xxxmedium)))
        ClickableTextPrompt(
            onTextClick = navigateToLogin,
            firstText = "Already have an account? ",
            secondText = "Login",
            tag = "LOGIN",
            annotation = "login"
        )
    }
}

@DevicesPreview
@Composable
private fun RegisterScreenPreview() {
    MyHabitsAppTheme {
        RegisterScreen(
            onRegisterButtonClick = {},
            name = TextFieldValue("Kenan"),
            email = TextFieldValue("kenan@email.com"),
            password = TextFieldValue("password123"),
            birthday = Date().time,
            onNameValueChange = {},
            onEmailValueChange = {},
            onPasswordValueChange = {},
            onBirthdayValueChange = {},
            navigateToLogin = {}
        )
    }
}
