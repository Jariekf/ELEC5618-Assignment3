# Introduction
This repository contains the work for **ELEC5618 Assignment 3**, which requires the team to implement three quality improvements for **Shattered Pixel Dungeon v3.0.2**. The purpose of this assignment is to identify quality issues in the existing system, modify the source code to address these issues, and verify that the improvements enhance the software quality without breaking the original game behaviour.

In this assignment, our team proposed and implemented two original improvements: **Unavailable Item Visual Feedback in Quick-slot** and **Critical Health Screen Warning**. These improvements focus on improving usability, operability, user error protection, and visual feedback during gameplay. In addition, we selected one predefined improvement from the tutor-provided list: **Configurable In-game Screenshot Shortcut**. This improvement allows users to capture gameplay screenshots through a configurable in-game shortcut.

Together, these three improvements aim to make the game easier to operate, clearer to understand, and more convenient to use, while preserving the original gameplay mechanics and balance.

## Improvement 1: Configurable In-game Screenshot Shortcut

### Original Problem

The original game did not provide a built-in, configurable in-game shortcut for capturing and saving the current gameplay scene. Players could only rely on external screenshot tools, which may be inconvenient during gameplay.

### Why it is a quality issue

The lack of an integrated screenshot function reduces operational convenience when players want to record bugs, gameplay evidence, or important game moments. If the screenshot key were hard-coded, it would also reduce maintainability and user control.

### Why it is not just a new feature

This improvement is selected from the predefined list. It directly improves the game’s operability by allowing screenshots to be captured during gameplay without interrupting the game flow.

### Quality Attributes and Corresponding Changes

| ISO/IEC 25010 Quality Attribute | Corresponding Change | Explanation |
|---|---|---|
| Usability | Add an in-game screenshot shortcut | Users can capture the current game scene more conveniently without using external tools. |
| Operability | Make the shortcut configurable through the existing keybinding system | Users can rebind the screenshot key according to their preference, making the control easier to operate. |
| Functional Suitability | Save the current game scene as a PNG file when the shortcut is pressed | The implemented behaviour directly satisfies the required screenshot functionality. |
| Maintainability | Implement screenshot as a normal SPDAction, not a hard-coded key listener | The feature follows the project’s existing input architecture, making it easier to maintain or extend. |
| Reliability | Handle screenshot save failure through existing log/message system | If saving fails, the game should report the error instead of crashing. |
| Operability | Allow users to choose the screenshot save folder | Users can control where screenshots are stored, making the feature easier to use and more flexible across different workflows. |

### SQA

For the Configurable In-game Screenshot Shortcut improvement, SQA activities were used to verify that the feature was functional, configurable, stable, and did not interrupt the original gameplay flow.

Because this improvement depends on keyboard input, runtime rendering, framebuffer capture, file saving, custom folder selection, and settings persistence, integration testing and system-level testing were more suitable than isolated unit testing.

#### 1. Build verification

The project was compiled after the implementation using Gradle.

This verified that the newly added screenshot-related code did not introduce compilation errors or break the existing build process.

#### 2. Integration testing with the keybinding system

The screenshot action was implemented as a normal configurable SPDAction and connected to the existing keybinding system.

This verified that the screenshot shortcut was not hard-coded. The user could change the screenshot key through the existing shortcut settings.

#### 3. Default shortcut test

The game was launched, and the default screenshot shortcut was pressed during active gameplay.

This verified that the screenshot action could be triggered in the real gameplay environment.

#### 4. Key rebinding test

The screenshot shortcut was changed in the keybinding settings, then the new key was tested during gameplay.

This verified that the rebinding function worked correctly and that the new key could trigger the screenshot action.

#### 5. Custom save folder test

The screenshot save location was changed using the new folder selection button.

This verified that the user could choose where screenshots were saved instead of being limited to a fixed default folder.

#### 6. Persistence test

After selecting a custom screenshot folder, the game was restarted and another screenshot was taken.

This verified that the selected screenshot folder was saved and reused after restarting the game.

#### 7. File output test

The generated screenshot file was manually checked in the selected folder.

This verified that the screenshot was saved as a PNG file, used a timestamped filename, and contained the correct gameplay scene.

#### 8. Non-interruption test

The screenshot shortcut was pressed during active gameplay.

This verified that taking a screenshot did not pause, freeze, or interrupt the game flow.

#### 9. Failure handling check

The implementation included fallback handling and failure messages if the screenshot could not be saved.

This verified that the game would not crash when the save path was unavailable or invalid.

#### 10. Regression testing

Existing controls were tested after the screenshot feature was added, including movement, inventory, quick-slot actions, menu navigation, and other shortcuts.

This verified that the screenshot improvement did not break existing gameplay controls or input behaviour.

### Outcome

After this improvement, players can capture the current gameplay scene using a configurable in-game shortcut. The screenshot is saved as a PNG file, and users can choose the folder where screenshots are stored. This improves operability and usability because players can record gameplay moments, bugs, or evidence without relying on external tools. The feature also keeps the original gameplay flow unchanged and does not affect existing controls.


## Improvement 2: Unavailable Item Visual Feedback in Quick-slot

## Improvement 3: Critical Health Screen Warning
