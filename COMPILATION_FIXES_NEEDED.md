# Compilation Fixes Needed

## Summary
After migrating to Firestore, many files still reference old Room/Realm entities. Here's what needs to be fixed:

## Files That Need Updates

### 1. HomeScreen.kt
**Issues:**
- Uses `TaskEntity`, `NoteEntity`, `ProjectEntity` (Room entities)
- Repository methods called without `userId` parameter
- Uses `ProjectStatus` from `data.local` instead of `data.firestore`

**Fix:**
- Import Firestore models: `com.example.amanotes.data.firestore.*`
- Get `userId` from `authRepository.getCurrentUserId()`
- Pass `userId` to all repository methods
- Use `com.example.amanotes.data.firestore.ProjectStatus` enum

### 2. NotesScreen.kt
**Issues:**
- Uses `NoteEntity` instead of `Note`
- Repository methods called without `userId`

**Fix:**
- Import `com.example.amanotes.data.firestore.Note`
- Get `userId` and pass to repository methods

### 3. ProjectDetailsScreen.kt
**Issues:**
- Uses `ProjectEntity` instead of `Project`
- Uses `ProjectStatus` and `ProjectPriority` from `data.local`
- Repository methods called without `userId`

**Fix:**
- Import `com.example.amanotes.data.firestore.*`
- Use Firestore enums for status and priority
- Get `userId` and pass to repository methods

### 4. SwipeableTaskItem.kt
**Issues:**
- Uses `TaskEntity` instead of `Task`

**Fix:**
- Import `com.example.amanotes.data.firestore.Task`

### 5. MainActivity.kt
**Issues:**
- `attachBaseContext` override removed (ComponentActivity doesn't support it)

**Fix:**
- Locale handling moved to App.kt only

## Quick Fix Script

Run this to see all files that need updates:
```bash
grep -r "import.*\.local\." app/src/main/java/com/example/amanotes/ui/
grep -r "Entity" app/src/main/java/com/example/amanotes/ui/
grep -r "ProjectStatus\|ProjectPriority" app/src/main/java/com/example/amanotes/ui/
```

## Next Steps
1. Update all UI screens to use Firestore models
2. Add userId parameter to all repository calls
3. Update imports
4. Fix enum references

