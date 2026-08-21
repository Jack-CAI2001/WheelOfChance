# Wheel Management UI Implementation Plan

This plan outlines the steps to implement the Wheel Management UI, including the Repository, ViewModels, Dashboard Screen, and Entry Editor Screen using Jetpack Compose and Navigation 3.

## User Review Required

> [!IMPORTANT]
> This implementation uses **Jetpack Navigation 3** for screen transitions and **Room** for data persistence.

## Proposed Changes

### Data Layer

#### [NEW] [WheelRepository](file:///C:/Users/longd/AndroidStudioProjects/WheelOfChance/app/src/main/java/com/example/wheelofchance/data/WheelRepository.kt)
A repository class to handle data operations between the UI and the Room database (`WheelDao` and `EntryDao`).

#### [NEW] [WheelOfChanceApplication](file:///C:/Users/longd/AndroidStudioProjects/WheelOfChance/app/src/main/java/com/example/wheelofchance/WheelOfChanceApplication.kt)
An `Application` class to initialize the Room database and provide repository access.

### ViewModels

#### [NEW] [WheelViewModel](file:///C:/Users/longd/AndroidStudioProjects/WheelOfChance/app/src/main/java/com/example/wheelofchance/ui/WheelViewModel.kt)
Handles the state for the Dashboard Screen (listing, adding, deleting wheels).

#### [NEW] [EditorViewModel](file:///C:/Users/longd/AndroidStudioProjects/WheelOfChance/app/src/main/java/com/example/wheelofchance/ui/EditorViewModel.kt)
Handles the state for the Entry Editor Screen (editing wheel name, managing entries).

### UI & Navigation

#### [NEW] [DashboardScreen](file:///C:/Users/longd/AndroidStudioProjects/WheelOfChance/app/src/main/java/com/example/wheelofchance/ui/dashboard/DashboardScreen.kt)
Displays the list of wheels with a FAB to add new ones.

#### [NEW] [EntryEditorScreen](file:///C:/Users/longd/AndroidStudioProjects/WheelOfChance/app/src/main/java/com/example/wheelofchance/ui/editor/EntryEditorScreen.kt)
UI to edit wheel details and its entries.

#### [NEW] [NavRoutes](file:///C:/Users/longd/AndroidStudioProjects/WheelOfChance/app/src/main/java/com/example/wheelofchance/ui/navigation/NavRoutes.kt)
Defines the navigation keys using `@Serializable` for Navigation 3.

#### [MODIFY] [MainActivity](file:///C:/Users/longd/AndroidStudioProjects/WheelOfChance/app/src/main/java/com/example/wheelofchance/MainActivity.kt)
Update to host the `NavDisplay` and manage top-level navigation state.

## Verification Plan

### Automated Tests
- Run Room database tests if they exist.
- Manual verification of UI flows.

### Manual Verification
1. Launch the app.
2. Add a new wheel from the Dashboard.
3. Verify the new wheel appears in the list.
4. Click the wheel to open the Editor.
5. Change the wheel name and add some entries with different colors.
6. Click "Save" and verify changes are persisted by returning to the editor.
7. Delete a wheel from the Dashboard and verify it's removed.
