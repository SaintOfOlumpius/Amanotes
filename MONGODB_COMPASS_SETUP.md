# MongoDB Compass Setup Guide

This guide explains how to configure the app to sync data with MongoDB Atlas so you can view it in MongoDB Compass.

## Option 1: MongoDB Atlas Data API (Recommended for Compass)

This approach uses MongoDB Atlas Data API to directly sync data to your MongoDB Atlas cluster, which you can then view in MongoDB Compass.

### Step 1: Set up MongoDB Atlas Data API

1. **Go to MongoDB Atlas**
   - Log in to [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
   - Create a cluster (Free tier works fine)

2. **Create Data API**
   - Go to your Atlas project
   - Click on "Data API" in the left sidebar
   - Click "Create API Key"
   - Save your API key securely
   - Note your App ID from the URL: `https://data.mongodb-api.com/app/YOUR_APP_ID/...`

3. **Configure Database and Collections**
   - Create a database named `amanotes`
   - Create collections: `notes`, `projects`, `tasks`, `users`

### Step 2: Update MongoDBAtlasService.kt

1. Open `app/src/main/java/com/example/amanotes/data/remote/MongoDBAtlasService.kt`

2. Update these constants:
   ```kotlin
   private const val ATLAS_API_BASE_URL = "https://data.mongodb-api.com/app/YOUR_APP_ID/endpoint/data/v1"
   private const val ATLAS_API_KEY = "YOUR_API_KEY"
   ```

3. **For Production**: Store API key securely:
   - Use BuildConfig for debug builds
   - Use Android Keystore or secure storage for production
   - Never commit API keys to version control

### Step 3: Configure Data API Permissions

1. In MongoDB Atlas Data API settings:
   - Allow read/write access to your database
   - Set proper permissions for the API key

### Step 4: View Data in MongoDB Compass

1. **Get Connection String**
   - In MongoDB Atlas, click "Connect"
   - Choose "MongoDB Compass"
   - Copy the connection string

2. **Connect in Compass**
   - Open MongoDB Compass
   - Paste the connection string
   - Replace `<password>` with your database password
   - Connect

3. **View Collections**
   - Navigate to `amanotes` database
   - You'll see collections: `notes`, `projects`, `tasks`, `users`
   - Data will appear as you sync from the app

## Option 2: MongoDB Realm Sync (Alternative)

This approach uses Realm Sync, which automatically syncs Realm database to MongoDB Atlas.

### Step 1: Create Realm App

1. Go to [MongoDB Realm](https://realm.mongodb.com/)
2. Create a new Realm App
3. Link it to your MongoDB Atlas cluster
4. Enable Realm Sync

### Step 2: Configure Sync

1. In Realm App dashboard:
   - Go to "Sync" section
   - Enable "Development Mode" or configure sync rules
   - Set partition key (e.g., `user_id`)

2. Get your App ID from Realm App settings

### Step 3: Update Build Configuration

1. Open `app/build.gradle.kts`
2. Update the Realm App ID:
   ```kotlin
   ksp {
       arg("realm.sync.url", "YOUR_REALM_APP_ID")
   }
   ```

3. Update `RealmManager.kt` to enable sync (uncomment sync configuration)

### Step 4: View in Compass

- Data synced via Realm Sync will appear in your Atlas cluster
- Connect Compass to the same cluster
- View data in the `amanotes` database

## Security Best Practices

### For Production:

1. **Store API Keys Securely**
   ```kotlin
   // Use BuildConfig for debug
   private const val ATLAS_API_KEY = BuildConfig.MONGODB_API_KEY
   
   // Or use Android Keystore
   private fun getApiKey(): String {
       // Retrieve from secure storage
   }
   ```

2. **Use Environment Variables**
   - Store sensitive data in `local.properties` (not committed to git)
   - Load in `build.gradle.kts`

3. **Set Proper Database Permissions**
   - Use read/write permissions only where needed
   - Implement proper authentication
   - Use MongoDB Atlas network access rules

## Testing the Connection

1. **Test Sync**
   - Create a note in the app
   - Trigger sync (or wait for automatic sync)
   - Check MongoDB Compass for the new document

2. **Verify Data**
   - Check that all fields are synced correctly
   - Verify user IDs match
   - Check timestamps

## Troubleshooting

### Data Not Appearing in Compass

1. **Check API Key**
   - Verify API key is correct
   - Check API key permissions
   - Ensure API key hasn't expired

2. **Check Database Name**
   - Ensure database name matches: `amanotes`
   - Verify collection names match

3. **Check Network**
   - Verify internet connection
   - Check MongoDB Atlas network access rules
   - Ensure your IP is allowed

4. **Check Sync Status**
   - Look at sync logs in the app
   - Check `synced` field in Realm objects
   - Verify sync service is running

### Connection Issues

1. **MongoDB Atlas Connection**
   - Verify cluster is running
   - Check connection string
   - Verify username/password

2. **Data API Issues**
   - Check API endpoint URL
   - Verify App ID is correct
   - Check API response errors

## Database Schema

The app uses these collections:

### notes
```json
{
  "_id": ObjectId,
  "userId": String,
  "title": String,
  "content": String,
  "category": String,
  "isFavorite": Boolean,
  "createdAt": Number,
  "updatedAt": Number,
  "attachmentUrl": String (optional)
}
```

### projects
```json
{
  "_id": ObjectId,
  "userId": String,
  "title": String,
  "description": String,
  "status": String,
  "priority": String,
  "progress": Number,
  "dueDate": Number (optional),
  "createdAt": Number,
  "updatedAt": Number,
  "thumbnailUrl": String (optional)
}
```

### tasks
```json
{
  "_id": ObjectId,
  "userId": String,
  "title": String,
  "isCompleted": Boolean,
  "projectId": String (optional),
  "createdAt": Number,
  "updatedAt": Number
}
```

### users
```json
{
  "_id": ObjectId,
  "userId": String,
  "name": String,
  "email": String,
  "photoUrl": String (optional),
  "token": String,
  "createdAt": Number,
  "updatedAt": Number
}
```

## Next Steps

1. Set up MongoDB Atlas cluster
2. Configure Data API or Realm Sync
3. Update configuration files with your credentials
4. Test sync functionality
5. Connect MongoDB Compass
6. Verify data appears correctly

---

**Note**: Remember to never commit API keys or sensitive credentials to version control. Use secure storage methods for production apps.

