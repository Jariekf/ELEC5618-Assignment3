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

### Original Problem

In the original game, items placed in the quick-slot could still appear visually available even when they were currently unusable, depleted, or invalid to use in the current context.

This means that the quick-slot UI did not always clearly communicate whether a selected item could actually perform its intended action at that moment.

### Why it is a quality issue

This may confuse players because the quick-slot icon does not clearly reflect the actual availability of the item. Players may only discover that an item is unavailable after clicking it, which can lead to unnecessary interactions, failed actions, or misunderstanding of the item state.

### Why it is not just a new feature

This improvement does not add new items, new actions, or new gameplay mechanics. It only improves the visual feedback of the existing quick-slot system. The item’s original behaviour remains unchanged, and the item is still clickable. The improvement only helps the player understand the current availability of the item before interacting with it.

### Quality Attributes and Corresponding Changes

| ISO/IEC 25010 Quality Attribute | Corresponding Change | Explanation |
|---|---|---|
| Usability | Dim unavailable quick-slot items | Players can more easily understand whether an item is currently usable. |
| Operability | Show item availability before the player clicks | Players can make better operation decisions based on the visual state of the quick-slot item. |
| User Error Protection | Keep depleted or unavailable items visually dimmed | This reduces confusing or failed actions caused by clicking items that cannot currently be used. |
| Functional Suitability | Make the quick-slot UI reflect the actual usability state of the item | The quick-slot interface better fulfils its purpose of representing usable item shortcuts. |
| Maintainability | Use a general quick-slot usability check where possible | The implementation avoids relying only on hard-coded item-specific cases and makes future extension easier. |

### SQA

For the Unavailable Item Visual Feedback in Quick-slot improvement, SQA activities were used to verify that the quick-slot visual state correctly reflected item availability without changing item mechanics or gameplay behaviour.

Because this improvement depends on runtime item states, quick-slot rendering, item default actions, and player interaction, manual system-level testing and regression testing were used.

#### 1. Build verification

The project was compiled after the implementation using Gradle.

This verified that the changes to item availability detection and quick-slot rendering did not introduce compilation errors.

#### 2. General unavailable item visual state test

Different quick-slot items were tested under unavailable, depleted, or invalid-use conditions.

This verified that unavailable quick-slot items became visually dimmed or semi-transparent instead of appearing normally available.

#### 3. Available item visual state test

Quick-slot items that were currently usable were tested.

This verified that available items remained visually normal and were not incorrectly dimmed.

#### 4. Click behaviour test

Unavailable quick-slot items were clicked while dimmed.

This verified that clicking did not incorrectly restore the item to normal colour if the item was still unavailable.

#### 5. Recovery test

Items were tested after returning from an unavailable state to an available state.

This verified that the quick-slot icon returned to normal only when the item actually became usable again.

#### 6. Regression testing

Normal quick-slot usage and existing item interactions were tested after the change.

This verified that the improvement did not change item mechanics, item usage rules, player controls, or existing quick-slot behaviour.

### Outcome

After this improvement, unavailable quick-slot items are visually dimmed or semi-transparent before the player clicks them. This makes item availability clearer and reduces confusing interactions. The improvement does not change item mechanics or gameplay behaviour, but improves usability, operability, and user error protection by giving players better visual feedback.

---

## Improvement 3: Critical Health Screen Warning

### Original Problem

The original game already has a low-health warning where the hero avatar becomes red and flashes when the player’s HP falls below the existing low-health threshold. However, this warning is limited to a small area of the status pane and can be missed during combat or fast gameplay.

### Why it is a quality issue

If players do not notice the low-health state, they may fail to heal, retreat, or change strategy in time. This can lead to avoidable mistakes because the critical health information is not visually noticeable enough during active gameplay.

### Why it is not just a new feature

This improvement does not change HP values, damage calculation, enemy behaviour, healing, combat rules, or game balance. It only improves the visibility of an existing low-health state by reusing the original red screen flash logic that already appears when the hero takes damage.

### Quality Attributes and Corresponding Changes

| ISO/IEC 25010 Quality Attribute | Corresponding Change | Explanation |
|---|---|---|
| Usability | Add a repeated red screen warning when the hero is in the existing low-health state | Players can notice critical health more easily during gameplay. |
| Operability | Make the low-health warning more visible beyond the small avatar area | Players can make timely decisions such as healing, retreating, or avoiding further risk. |
| User Error Protection | Repeat the warning while the hero remains below the low-health threshold | This reduces the chance of players missing the dangerous HP state. |
| Functional Suitability | Improve how the UI communicates the hero’s danger state | The health feedback system better fulfils its purpose of informing the player about critical health. |
| Maintainability | Reuse the existing low-health condition and original damage flash mechanism | The implementation follows existing game logic instead of introducing an unrelated warning system. |

### SQA

For the Critical Health Screen-edge Warning improvement, SQA activities were used to verify that the warning appeared at the correct time, reused the existing low-health condition, and did not affect combat or game balance.

Because this improvement is a runtime visual feedback change, manual system-level testing and regression testing were used to verify the behaviour in the actual gameplay environment.

#### 1. Build verification

The project was compiled after the implementation using Gradle.

This verified that the changes to GameScene, StatusPane, and the existing flash mechanism did not introduce compilation errors.

#### 2. Existing threshold verification

The implementation reused the original low-health condition already used by the avatar warning.

This verified that no new HP threshold was introduced and the existing low-health logic was preserved.

#### 3. Original damage flash reuse verification

The implementation reused the game’s existing red damage flash mechanism instead of creating a completely separate overlay.

This verified that the new warning followed the game’s original visual feedback architecture and remained consistent with existing screen flash behaviour.

#### 4. HP above threshold test

The hero’s HP was kept above the existing low-health threshold.

This verified that the repeated red screen flash did not appear when the hero was not in a critical health state.

#### 5. HP below threshold test

The hero took damage until HP dropped below the existing low-health threshold.

This verified that the repeated red screen flash appeared when the hero entered the existing low-health state.

#### 6. Flash interval test

The repeated low-health warning was observed during gameplay.

This verified that the low-health screen flash appeared at a slower interval of approximately 2 seconds, making it noticeable without being too distracting.

#### 7. Original damage flash regression test

The hero took damage while above and below the low-health threshold.

This verified that the original stronger damage flash still worked and was not replaced by the new low-health warning.

#### 8. Healing recovery test

The hero healed above the existing low-health threshold.

This verified that the repeated red screen flash stopped after the hero was no longer in the critical health state.

#### 9. Gameplay regression testing

Movement, combat, inventory, quick-slot actions, menus, and other UI elements were tested while the warning was active.

This verified that the warning did not block gameplay, hide important UI elements, or affect player controls.

### Outcome

After this improvement, the game provides a more noticeable warning when the hero remains in a low-health state. The screen gently flashes red every 2 seconds by reusing the original damage flash logic, while the original avatar warning still remains. This improves usability, operability, and user error protection because players can notice critical health more easily and respond in time. The improvement does not change combat mechanics, HP values, healing, enemy behaviour, or game balance.
