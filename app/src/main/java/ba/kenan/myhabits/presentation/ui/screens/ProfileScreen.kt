package ba.kenan.myhabits.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ba.kenan.myhabits.R
import ba.kenan.myhabits.domain.models.Frequency
import ba.kenan.myhabits.domain.models.Habit
import ba.kenan.myhabits.domain.models.UserProfile
import ba.kenan.myhabits.presentation.ui.components.ImageBox
import ba.kenan.myhabits.presentation.ui.components.LoadingComponent
import ba.kenan.myhabits.presentation.ui.theme.MyHabitsAppTheme
import ba.kenan.myhabits.presentation.ui.theme.fontBlack
import ba.kenan.myhabits.presentation.ui.theme.fontDarkGrey
import ba.kenan.myhabits.presentation.ui.theme.fontGrey
import ba.kenan.myhabits.presentation.utils.DevicesPreview
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        /*Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Menu, contentDescription = null)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.MoreVert, contentDescription = null)
            }
        }*/

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            ImageBox(initials = initials)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = profile.name,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Neki tekst",
                style = MaterialTheme.typography.bodyMedium,
                color = fontGrey
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = stringResource(R.string.information),
            style = MaterialTheme.typography.titleMedium,
            color = fontDarkGrey,
            modifier = Modifier.padding(start = 28.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 24.dp,),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                InfoItem("Name", profile.name)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 0.7.dp,
                    color = fontGrey.copy(alpha = 0.4f)
                )
                InfoItem("Email", profile.email)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 0.7.dp,
                    color = fontGrey.copy(alpha = 0.4f)
                )
                InfoItem("Timezone", profile.timezone)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 0.7.dp,
                    color = fontGrey.copy(alpha = 0.4f)
                )
                InfoItem("Age", age)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 0.7.dp,
                    color = fontGrey.copy(alpha = 0.4f)
                )
                InfoItem("Joined", joinedOn)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 0.7.dp,
                    color = fontGrey.copy(alpha = 0.4f)
                )
                InfoItem("Total habits", profile.habits.size.toString())
            }
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(color = fontGrey, fontSize = 20.sp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(color = fontBlack, fontSize = 20.sp)
        )
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
