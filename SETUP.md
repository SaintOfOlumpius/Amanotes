# Amanotes - Setup Guide

This guide will help you set up the Amanotes application with all required services for Play Store publication.

## Prerequisites

- Android Studio (latest version)
- Google Cloud Console account
- Firebase Console account
- MongoDB Atlas account (optional, for Realm Sync)

## 1. Firebase Setup

### Step 1: Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select an existing one
3. Add Android app with package name: `com.example.amanotes`

### Step 2: Download google-services.json
1. Download `google-services.json` from Firebase Console
2. Place it in `app/` directory (root of the app module)

### Step 3: Enable Firebase Services
Enable the following services in Firebase Console:
- **Authentication**: Enable Google Sign-In provider
- **Cloud Storage**: Enable for blob storage
- **Cloud Messaging**: Enable for push notifications
- **Analytics**: Enable for app analytics

### Step 4: Configure Google Sign-In
1. In Firebase Console, go to Authentication > Sign-in method
2. Enable Google Sign-In
3. Note the Web client ID (you'll need this for strings.xml)

## 2. Google Sign-In Configuration

### Step 1: Get Web Client ID
1. In Firebase Console, go to Project Settings > General
2. Under "Your apps", find the Web app
3. Copy the Web client ID

### Step 2: Update strings.xml
1. Open `app/src/main/res/values/strings.xml`
2. Replace `YOUR_WEB_CLIENT_ID_HERE` with your actual Web client ID:
   ```xml
   <string name="default_web_client_id">YOUR_ACTUAL_WEB_CLIENT_ID</string>
   ```

### Step 3: Configure OAuth Consent Screen
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Select your project
3. Go to APIs & Services > OAuth consent screen
4. Configure the consent screen with your app details
5. Add test users if needed

## 3. MongoDB Realm Setup (Optional)

### Step 1: Create MongoDB Atlas Account
1. Go to [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Create a free cluster
3. Create a database user

### Step 2: Create Realm App
1. Go to MongoDB Realm
2. Create a new Realm App
3. Note your App ID
4. Configure authentication (email/password or Google OAuth)

### Step 3: Update Build Configuration
1. Open `app/build.gradle.kts`
2. In the `ksp` block, replace `REPLACE_WITH_YOUR_REALM_APP_ID` with your Realm App ID:
   ```kotlin
   ksp {
       arg("realm.sync.url", "YOUR_REALM_APP_ID")
   }
   ```

## 4. Build Configuration

### Update Version for Play Store
1. Open `app/build.gradle.kts`
2. Update version for each release:
   ```kotlin
   versionCode = 2  // Increment for each release
   versionName = "1.1.0"  // Update version string
   ```

### Signing Configuration (Production)
1. Generate a keystore:
   ```bash
   keytool -genkey -v -keystore amanotes-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias amanotes
   ```
2. Create `keystore.properties` in project root:
   ```properties
   storePassword=your_store_password
   keyPassword=your_key_password
   keyAlias=amanotes
   storeFile=path/to/amanotes-release.jks
   ```
3. Update `app/build.gradle.kts` to use release signing (remove debug signing config for production)

## 5. Permissions

The following permissions are already configured in AndroidManifest.xml:
- Internet access
- Network state
- Storage (for blob storage)
- Notifications
- Camera (optional, for attachments)

For Android 13+, you may need to request runtime permissions for:
- POST_NOTIFICATIONS
- READ_MEDIA_IMAGES
- READ_MEDIA_VIDEO
- READ_MEDIA_AUDIO

## 6. Testing

### Before Publishing
1. Test Google Sign-In flow
2. Test offline mode and sync
3. Test push notifications
4. Test file uploads to Firebase Storage
5. Test on multiple Android versions (API 24+)
6. Run ProGuard/R8 to ensure no obfuscation issues

### Build Release APK
```bash
./gradlew assembleRelease
```

### Build Release AAB (for Play Store)
```bash
./gradlew bundleRelease
```

## 7. Play Store Preparation

### Store Listing Requirements
1. App icon (512x512 and 1024x1024)
2. Feature graphic (1024x500)
3. Screenshots (at least 2, max 8)
4. App description
5. Privacy policy URL
6. Content rating questionnaire

### Privacy Policy
You must have a privacy policy that covers:
- Data collection (user data, analytics)
- Firebase services usage
- Google Sign-In data
- Storage of user content
- Push notifications

### Content Rating
Complete the content rating questionnaire in Play Console.

## 8. Features Implemented

✅ MongoDB Realm for NoSQL database
✅ Google Single Sign-On (SSO)
✅ Firebase Storage for blob storage
✅ Firebase Cloud Messaging for real-time notifications
✅ Offline mode with automatic sync
✅ Network state monitoring
✅ Background sync with WorkManager
✅ ProGuard rules for release builds
✅ Play Store ready configuration

## 9. Known Issues & Notes

- The sync service currently marks items as synced locally. For production, integrate with MongoDB Realm Sync or your backend API.
- Google Sign-In requires proper OAuth configuration in Google Cloud Console.
- Firebase Storage requires proper security rules (configure in Firebase Console).
- Push notifications require FCM server key for backend integration.

## 10. Support

For issues or questions, please check:
- Firebase Documentation: https://firebase.google.com/docs
- MongoDB Realm Documentation: https://docs.mongodb.com/realm/
- Google Sign-In Documentation: https://developers.google.com/identity/sign-in/android

---

**Note**: Remember to replace all placeholder values with actual credentials before building for production!

