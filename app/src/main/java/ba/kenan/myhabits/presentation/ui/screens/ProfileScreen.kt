package ba.kenan.myhabits.presentation.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ba.kenan.myhabits.domain.model.Frequency
import ba.kenan.myhabits.domain.model.Habit
import ba.kenan.myhabits.domain.model.UserProfile
import ba.kenan.myhabits.presentation.ui.components.ImageBox
import ba.kenan.myhabits.presentation.ui.components.InfoCard
import ba.kenan.myhabits.presentation.ui.components.LoadingComponent
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme
import ba.kenan.myhabits.presentation.utils.DevicesPreview
import ba.kenan.myhabits.presentation.utils.calculateHabitStatistics
import ba.kenan.myhabits.presentation.viewmodels.profile.ProfileUiState
import ba.kenan.myhabits.presentation.viewmodels.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val context = LocalContext.current

    LaunchedEffect(userId) {
        userId?.let { viewModel.loadProfile(it) }
    }

    when (uiState) {
        is ProfileUiState.Loading -> LoadingComponent()
        is ProfileUiState.Error -> {
            Log.e("Profile", "Failed to load profile", (uiState as ProfileUiState.Error).error)
        }
        is ProfileUiState.Success -> {
            val profile = (uiState as ProfileUiState.Success).profile

            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val joinedOn = formatter.format(profile.joinedOn)
            val birthLocalDate = profile.birthDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            val today = LocalDate.now()
            val age = ChronoUnit.YEARS.between(birthLocalDate, today).toString()
            val nameParts = profile.name.trim().split(" ")
            val initials = nameParts.take(2).map { it.first().uppercaseChar() }.joinToString("")
            var hasShownMotivation by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                if (!hasShownMotivation && viewModel.shouldShowMotivation(profile.habits)) {
                    hasShownMotivation = true
                    Toast.makeText(
                        context,
                        "Znaš da ti ${today.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }} teško padaju - danas napravi mali korak. Možeš ti to!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            ProfileScreen(
                profile = profile,
                age = age,
                initials = initials,
                joinedOn = joinedOn,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun ProfileScreen(
    profile: UserProfile,
    initials: String,
    age: String,
    joinedOn: String,
    modifier: Modifier = Modifier
) {
    val (totalCompleted, streak) = calculateHabitStatistics(profile.habits)

    val informationItems = listOf(
        "Name" to profile.name,
        "Email" to profile.email,
        "Timezone" to profile.timezone,
        "Age" to age,
        "Joined" to joinedOn
    )

    val statisticsItems = listOf(
        "Completed days" to totalCompleted.toString(),
        "Current streak" to "$streak day${if (streak == 1) "" else "s"}",
        "Total habits" to profile.habits.size.toString()
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            ImageBox(initials = initials)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = profile.name,
                style = MaterialTheme.typography.headlineSmall
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        InfoCard(title = "Information", items = informationItems)
        InfoCard(title = "Statistics", items = statisticsItems)
    }
}

@DevicesPreview
@Composable
private fun ProfileScreenPreview() {
    MyHabitsAppTheme {
        val previewProfile = UserProfile(
            name = "Kenan Sultanić",
            email = "kenan@gmail.com",
            birthDate = Calendar.getInstance().apply { set(2025, 4, 13) }.time,
            timezone = "Europe/Sarajevo",
            joinedOn = Calendar.getInstance().apply { set(2025, 4, 29) }.time,
            habits = listOf(
                Habit(
                    id = "1",
                    name = "Running",
                    tags = listOf("health", "fitness"),
                    frequency = Frequency("Weekly", listOf(1, 3, 5)),
                    isArchived = false,
                    createdAt = Date(),
                    history = mapOf("2025-05-28" to true)
                )
            )
        )
        ProfileScreen(profile = previewProfile, initials = "KS", age = "24", joinedOn = "09/05/2025")
    }
}
