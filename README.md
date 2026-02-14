## Description

**DevPeek** is a GitHub user search application built using modern
Android development practices.

It follows **Clean Architecture** principles with a clear separation
between **Presentation**, **Domain**, and **Data** layers. The
application uses **Kotlin Coroutines** for asynchronous operations and
**Room** for local caching and offline support.

------------------------------------------------------------------------

## Architecture

The project uses the **MVVM (Model--View--ViewModel)** architecture.\
This architecture is widely recommended by Google for Android
development and is well-known among developers, making the codebase
easier to understand, maintain, and collaborate on.

------------------------------------------------------------------------

## Challenges

Implementing **Paging 3** was the main challenge, especially: - Fetching
data from the remote source - Passing it through `RemoteMediator` -
Integrating pagination with the search logic

------------------------------------------------------------------------

## Additional Features

-   **Saved Users**\
    Users found from search can be saved for quick access later.\
    This feature also works in offline conditions.

-   **Dark Mode Toggle**\
    The app allows users to choose between light and dark themes based
    on their preference.

------------------------------------------------------------------------

## How to Run

### 1. Requirements

Make sure your environment meets the following requirements:

-   **Android Studio Otter \| 2025.2.1** (recommended)
-   **Java 11**
-   Android SDK installed via SDK Manager
-   Internet connection (required for GitHub API requests)

------------------------------------------------------------------------

### 2. Get a GitHub API Token

The app uses authenticated requests to avoid GitHub API rate limits.

1.  Open GitHub → **Settings**
2.  Go to **Developer Settings → Personal access tokens → Tokens
    (classic)**
3.  Click **Generate new token**
4.  No special permissions are required (public data only is enough)
5.  Copy the generated token

------------------------------------------------------------------------

### 3. Configure `local.properties`

Create or edit the file:

local.properties

Add the following line:

GITHUB_TOKEN=YOUR_SECRET_KEY

Example:

GITHUB_TOKEN=ghp_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

Do NOT wrap the token in quotes\
Do NOT commit this file to version control

------------------------------------------------------------------------

### 4. Sync Gradle

After adding the token:

1.  Open the project in Android Studio
2.  Click **Sync Project with Gradle Files**
3.  Wait until all dependencies are downloaded

------------------------------------------------------------------------

### 5. Build the Project

To verify everything works:

Option A --- From UI 1. Build → Make Project

Option B --- From terminal ./gradlew assembleDebug

------------------------------------------------------------------------

### 6. Run the Application

1.  Connect a physical Android device or start an emulator
2.  Press Run ▶
3.  Select the `app` configuration
4.  Wait until installation finishes

------------------------------------------------------------------------

### 7. First Launch Notes

-   The first search requires internet connection
-   After data is cached, saved users remain accessible offline
-   If results are empty, the token is usually missing or invalid

------------------------------------------------------------------------

### Troubleshooting

Build fails with missing token error\
Ensure `local.properties` is located in the root project folder, not
inside /app.

HTTP 401 / 403\
The token is invalid or expired --- generate a new one.

No search results\
GitHub may temporarily rate-limit requests. Wait a moment and retry.
