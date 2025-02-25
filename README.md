# Spotify Project Overview

## Project Structure

- **src/**: Contains all source code files.
  - **checker/**: Checker files.
  - **fileio/**: Classes responsible for reading data from JSON files.
  - **main/**:
    - **Main**: Entry point for running the implementation.
    - **Test**: Runs test cases and stores the output in `out.txt` for comparison.
  - **implementareprogram/**: Contains all core classes used in the project.
- **input/**: Test cases and JSON-formatted library.
- **ref/**: Reference output for validation.

## Functionality

### Core Features

- The **Spotify** class implements all commands, taking input from `main` and storing results in an `ObjectNode`.
- The **User** class keeps track of command status, last searches, selections, and navigation.
- The **GetTop** class stores general statistics like liked songs and followed playlists.
- **UserList** maintains a collection of all users.

### Command Implementations

- **Search**: Uses a specialized class to filter audio files based on search criteria.
- **Select**: Stores the last selected audio file type and ensures valid selection based on previous searches.
- **Load**: Updates user status, keeping track of playback conditions, shuffle state, and current file.
- **Status**: Manages playback states (paused/playing) and simulates time passage for audio files, including **podcasts**.
- **Playlists**: Stores visibility, followers count, shuffle seed, and indices. Follows a dynamic time simulation approach similar to songs.
- **Navigation (Prev/Next)**: Tracks playback history and updates user state accordingly.

### Design Patterns Used

- **Singleton**: Used in sorting mechanisms to maintain a single instance.
- **Visitor**: Implements functionality for displaying user pages dynamically.
- **Command**: Handles navigation, storing previous and next pages for undo/redo actions.
- **Observer**: Manages user subscriptions and notifications for artist/host activities.

### Additional Features

- **User Online/Offline Mode**: The admin skips time simulation for offline users.
- **Album Implementation**: Extended from `AudioCollection`, requiring modifications to search and shuffle mechanisms.
- **Artists & Hosts**: Added as subclasses of `User`, with `searchable` versions for filtering.
- **Announcements, Events, and Merch**: Each artist/host maintains a list of these in a `userutils` package.
- **Admin System**: Handles albums, podcasts, and user management dynamically.
- **Delete Operations**:
  - Removing a podcast ensures no active user is currently playing it.
  - Deleting an album involves verifying dependencies on songs and playlists.
  - User deletion updates followed playlists and resets associated pages for offline users.

### Top Artists & Albums

- **Top 5 Artists**: Ranked by total song likes, with lexicographic ordering for ties.
- **Top Albums**: Ranked based on total likes of included songs.

This implementation provides a robust simulation of a music streaming service with advanced state tracking, search capabilities, and structured data handling.
