# Firebase Setup Instructions

## ⚠️ Important: Replace Placeholder google-services.json

The current `google-services.json` file is a **placeholder** and will not work with Firebase services. You need to replace it with your actual Firebase configuration.

## Steps to Get Your google-services.json File

### 1. Create a Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project" or select an existing project
3. Follow the setup wizard:
   - Enter a project name (e.g., "Amanotes")
   - Enable/disable Google Analytics as needed
   - Accept terms and create project

### 2. Add Android App to Firebase

1. In your Firebase project, click the Android icon or "Add app"
2. Register your Android app:
   - **Package name**: `com.example.amanotes` (must match your app's package name)
   - **App nickname** (optional): "Amanotes Android"
   - **Debug signing certificate SHA-1** (optional for now)
3. Click "Register app"

### 3. Download google-services.json

1. Click "Download google-services.json"
2. Save the file
3. **Replace** the placeholder file at:
   ```
   /Users/saint/Downloads/Amanotes-master/app/google-services.json
   ```

### 4. Enable Firebase Services

In Firebase Console, enable the following services:

#### Authentication
1. Go to **Authentication** → **Sign-in method**
2. Enable **Email/Password** provider
3. Enable **Google** provider:
   - Add support email
   - Save the **Web client ID** (you'll need this)

#### Firestore Database
1. Go to **Firestore Database**
2. Click "Create database"
3. Choose "Start in test mode" (for development)
4. Select a location (prefer closest to your users)

#### Cloud Storage
1. Go to **Storage**
2. Click "Get started"
3. Start in test mode
4. Select a location

#### Cloud Messaging (FCM)
1. Go to **Cloud Messaging**
2. The service is automatically enabled
3. Note the Server key if needed (in Project Settings → Cloud Messaging)

### 5. Update Web Client ID

1. In Firebase Console, go to **Project Settings** → **General**
2. Under "Your apps", find your Web app (or create one if it doesn't exist)
3. Copy the **Web client ID**
4. Open `app/src/main/res/values/strings.xml`
5. Replace `YOUR_WEB_CLIENT_ID_HERE` with your actual Web client ID:
   ```xml
   <string name="default_web_client_id">YOUR_ACTUAL_WEB_CLIENT_ID</string>
   ```

### 6. Configure OAuth Consent Screen (for Google Sign-In)

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Select your Firebase project
3. Go to **APIs & Services** → **OAuth consent screen**
4. Configure:
   - User type (Internal or External)
   - App information (name, logo, etc.)
   - Scopes (add email, profile)
   - Test users (if using External user type)

### 7. Build and Test

1. Sync your Gradle project
2. Build the app
3. Test Firebase services:
   - Authentication (Email/Password and Google Sign-In)
   - Firestore (creating/reading data)
   - Cloud Storage (uploading files)
   - Cloud Messaging (receiving notifications)

## Security Rules

### Firestore Security Rules

For production, update your Firestore security rules:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can only access their own data
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    match /notes/{noteId} {
      allow read, write: if request.auth != null && 
        request.resource.data.userId == request.auth.uid;
    }
    
    match /projects/{projectId} {
      allow read, write: if request.auth != null && 
        request.resource.data.userId == request.auth.uid;
    }
    
    match /tasks/{taskId} {
      allow read, write: if request.auth != null && 
        request.resource.data.userId == request.auth.uid;
    }
  }
}
```

### Storage Security Rules

```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

## Troubleshooting

### Build Error: "File google-services.json is missing"
- Make sure the file is in `app/google-services.json` (not `app/src/...`)
- Check that the file name is exactly `google-services.json` (case-sensitive)

### Authentication Not Working
- Verify the package name in `google-services.json` matches your app's package name
- Check that the Web client ID in `strings.xml` is correct
- Ensure OAuth consent screen is configured

### Firestore Connection Issues
- Check Firestore security rules
- Verify the database is created and active
- Check internet connectivity

## Notes

- The placeholder file allows the app to build, but Firebase features won't work
- Replace the placeholder before deploying to production
- Keep your `google-services.json` file secure and never commit it to public repositories if it contains sensitive data

## Support

For more help:
- [Firebase Documentation](https://firebase.google.com/docs)
- [Firebase Android Setup Guide](https://firebase.google.com/docs/android/setup)
- [Firebase Support](https://firebase.google.com/support)

