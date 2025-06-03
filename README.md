# Technical Documentation - MyHabits Android App

## 1. App Overview

**Name:** MyHabits  
**Description:** MyHabits is an Android app designed to help users track their personal habits. Users can create, edit, and archive habits, record daily completions, and view statistics such as total completed habits and current streak.  
**Platform:** Android  
**Technologies:**

- Kotlin  
- Jetpack Compose  
- Firebase Authentication  
- Firebase Firestore  
- Dagger Hilt (DI)  
- Clean Architecture  
- AlarmManager & BroadcastReceiver (for notifications)  

---

## 2. Architecture

### Design Pattern

The app is built using Clean Architecture with MVVM (Model-View-ViewModel). Each layer has a clear responsibility, making the code easier to test, maintain, and scale.

### Clean Architecture Layers

#### 1. Presentation Layer (`ba.kenan.myhabits.presentation`)

Handles the user interface and UI logic. The UI communicates with the ViewModel, which in turn interacts with the domain layer.

- `ui/` - Jetpack Compose screens and layout  
- `viewmodels/` - ViewModel classes exposing UI state and handling interactions  
- `notifications/` - Logic for daily reminders (AlarmManager + BroadcastReceiver)  
- `utils/` - UI-related helper functions  
- `di/` - Hilt modules for the presentation layer  

#### 2. Domain Layer (`ba.kenan.myhabits.domain`)

The core logic of the app, fully independent from Android framework code. The ViewModel interacts with interfaces defined in this layer without knowing their actual implementation.

- `model/` - Plain data classes (e.g., Habit, Frequency, User)  
- `repository/` - Abstract repository interfaces (e.g., HabitRepository)  
- `network/` - Interface for checking network status  

#### 3. Data Layer (`ba.kenan.myhabits.data`)

Provides actual implementations of the domain interfaces using Firebase services.

- `repository/` - Firebase-based implementations  
- `utils/` - Helpers for data formatting, Firestore conversion, etc.  
- `di/` - Hilt modules binding concrete implementations to interfaces  
- `network/` - NetworkStatusProvider implementation  

---

## 3. Features & Functionality

### Authentication

- Sign up and login using Firebase Authentication  
- Input validation  
- UI adapts based on authentication status  

### Habit Management

- Create and edit habits  
- Supports three frequency types: daily, weekly, and custom  
- Daily completion tracking saved in the `history` subcollection  
- Archive and delete habits  

### Statistics

- Total number of completed habits  
- Current streak tracking  
- Displays motivational messages based on user progress  

### Notifications

- Sends a daily reminder at 6:00 PM  
- Uses AlarmManager and BroadcastReceiver to schedule notifications  
- General, non-interactive notification  

### Light & Dark Theme

- Automatically follows system theme (light or dark)  
- All UI components adapt accordingly  

### Responsive Design

- UI scales across different screen sizes  
- Uses dimension resources (`dimens.xml`) to ensure consistent layout on all devices  

### Custom App Icon

- App uses a custom adaptive icon for all supported Android versions  
- Multiple resolutions included (`mdpi`, `hdpi`, `xhdpi`, `xxhdpi`, `xxxhdpi`)  

### Offline Support

- Firestore’s offline persistence is enabled  
- Previously loaded data (habits, history, stats) is accessible offline  
- Limitations while offline:
  - Creating, editing, or deleting habits is not supported  
  - New entries require an internet connection  
- Local cache is used to display the most recent available data  

---

## 4. Build & Run Instructions

### Build Tools

- Android Gradle Plugin  
- Jetpack Compose (enabled)  
- Min SDK: 26  
- Target SDK: 34  

### How to Run

1. Clone the repository from GitHub  
2. Open the project in Android Studio  
3. Run Gradle sync  
4. Launch the app on an emulator or a physical device  

---

## 5. Author & Contributions

- **Author:** Kenan Sultanić  
- **Technologies used:** Android SDK, Kotlin, Firebase, Jetpack Compose  
- **Development period:** May–June 2025  
