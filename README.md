Notes Application

A modern Notes application built with Kotlin in **Android Studio.  
The app uses Room Database for offline-first storage and syncs automatically when the device is back online.  
It also supports Google Single Sign-On (SSO) for secure authentication.


✨ Features

- 📝 Create, edit, and delete notes.
- 📂 Organize notes locally with RoomDB.
- 🔒 **Google SSO** integration for easy login.
- 🌐 Offline-first approach:  
  - Notes are stored locally when offline.  
  - Automatic sync with the backend API when internet becomes available.
- 🔄 Seamless synchronization between devices (via API backend).
- 🎨 Modern Material Design UI.



🛠️ Tech Stack

Language:Kotlin  
IDE: Android Studio  
Database: RoomDB (offline persistence)  
Authentication: Google Sign-In API (SSO)  
Networking: Retrofit / Ktor Client (for API communication)  
Architecture: MVVM with LiveData / Flow & ViewModel  

📱 Installation

1. Clone the repository:
  