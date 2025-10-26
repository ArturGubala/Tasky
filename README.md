# Tasky (Android)

**Tasky** is a modern, offline-first **agenda manager** for Android built with **Kotlin**.  
It lets users manage their **tasks, events, and reminders** in one place — with responsive layouts,
dark mode, offline synchronization, and clean Material 3 design.

---

## ✨ Features

### Theme & UI

- Light and Dark mode with **system theme detection**
- **Material 3 design** with adaptive colors and typography
- Fully **responsive layouts** for phones and tablets
- Persists theme preference between sessions

---

### Authentication

- Secure registration and login via REST API
- Validations with visual feedback
- Keeps user logged in while token is valid
- Login state checked on splash screen

---

### Agenda Screen

- Displays all agenda items (events, tasks, reminders) ordered by time
- Floating Action Button ➕ to add new items
- Context menu for **Open**, **Edit**, **Delete** (with confirmation dialog)
- Date picker and horizontal date scroll for ±15 days
- Profile icon showing user initials with logout dropdown
- Real-time “current time needle” indicator

---

### Events

- Title, description, start/end time, reminder options
- Add up to **10 photos** (auto-compressed to ≤1 MB)
- Offline caching for remote images
- Attendee management (add/remove/check user via API)
- Role-based permissions:
    - **Creator** → Full edit & delete access
    - **Attendee** → View-only + personal reminder editing
- Two-phase photo upload to S3 with confirmation flow
- Offline restrictions for photo and attendee editing

---

### Tasks

- Title, description, time, and reminder
- Mark as complete directly from agenda
- Confirmation bottom sheet before deletion
- Offline creation and editing supported

---

### Reminders

- Similar to tasks but cannot be marked done/undone
- Dedicated edit screens for title and description
- Smart scheduling and cleanup for reminders

---

### Notifications

- Local notifications for upcoming events and reminders
- Tapping a notification opens the detail screen
- Cross-device synchronization of scheduled reminders
- Smart scheduling: triggers only for future times
- All reminders cleared on logout

---

### Offline-First Architecture

- **Full offline support** after initial login
- Queue changes locally and sync automatically when online
- Handles conflicts using *last-write-wins* strategy
- Offline indicators for restricted actions
- Online-only for login, logout, photos, and attendees

---

## Tech Stack

| Layer                | Technology                         |
|----------------------|------------------------------------|
| Language             | **Kotlin**                         |
| Architecture         | **MVI + Clean Architecture**       |
| UI                   | **Jetpack Compose** / Material 3   |
| Async                | **Coroutines & Flow**              |
| Persistence          | **Room / DataStore / Local cache** |
| Networking           | **Ktor**                           |
| Dependency Injection | **Koin**                           |
| Offline Sync         | Custom queue-based sync manager    |
| Notifications        | Android AlarmManager / WorkManager |
| Image Loading        | Coil                               |
| Build                | Gradle (KTS)                       |

---

## Responsive Design

| Device | Orientation | Layout                    |
|--------|-------------|---------------------------|
| Mobile | Portrait    | Single-column             |
| Mobile | Landscape   | Two-panel / horizontal    |
| Tablet | Portrait    | Centered layout           |
| Tablet | Landscape   | Split view / multi-column |

---

## Acknowledgements

- This project was build as part
  of [12-week individual mentorship from Philipp Lackner](https://pl-coding.com/drop-table-mentoring/)
- Built with ❤️ using Kotlin and Jetpack Compose  
