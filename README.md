# ELEC5618 Assignment 3

## Quality Improvements for Shattered Pixel Dungeon v3.0.2

This repository contains the work for **ELEC5618 Assignment 3**. The target project is **Shattered Pixel Dungeon v3.0.2**. The purpose of this assignment is to identify quality issues in the existing game, modify the source code to address these issues, and verify that the improvements enhance software quality without breaking the original game behaviour.

In this assignment, our group implemented three source code level quality improvements:

1. **Configurable In-game Screenshot Shortcut**  
   This improvement was selected from the five options provided by the tutor.

2. **Unavailable Item Visual Feedback in Quick-slot**  
   This improvement was proposed by our team and approved by the tutor.

3. **Critical Health Screen Warning**  
   This improvement was proposed by our team and approved by the tutor.

Together, these improvements aim to make the game easier to operate, clearer to understand, and more convenient to use, while preserving the original gameplay mechanics and balance.

## How to Run the Game

Run the following command in the project root directory:

```bash
cd shattered-pixel-dungeon-3.0.2
windows: ./gradlew.bat desktop:debug
macos: ./gradlew desktop:debug
---

If terminal shows a permission denied error, grant execution permission to the Gradle wrapper first:
```bash
chmod +x gradlew
---

## Improvement 1: Configurable In-game Screenshot Shortcut

### Original Problem

The original game did not provide a built in, configurable in game shortcut for capturing and saving the current gameplay scene. Players could only rely on external screenshot tools, which may be inconvenient during gameplay.

### Why it is a Quality Issue

The lack of an integrated screenshot function reduces operational convenience when players want to record bugs, gameplay evidence, or important game moments. If the screenshot key were fixed directly in the code, it would also reduce maintainability and user control.

### Why it is not just a New Feature

This improvement is selected from the predefined list provided by the tutor. It directly improves the game’s operability by allowing screenshots to be captured during gameplay without interrupting the game flow.

### Quality Attributes and Corresponding Changes

| ISO/IEC 25010 Attribute | Corresponding Change | Explanation |
|---|---|---|
| **Operability** | Add a configurable screenshot shortcut and custom save folder | Users can capture screenshots during gameplay, change the shortcut, and choose where screenshots are stored. |
| **Functional Suitability** | Save the current scene as a PNG file | The behaviour directly satisfies the required screenshot function. |
| **Maintainability** | Integrate the shortcut into the existing key binding system | The feature follows the game’s original input design, so it is easier to maintain and extend. |
| **Reliability** | Handle save failures through logs and messages | If saving fails, the game reports the error instead of crashing. |

### Source Code Implementation

The screenshot feature was implemented by integrating it with the existing input system, settings system, platform support layer, and message resource system.

- `SPDAction.java`: Added `SCREENSHOT` as a new configurable game action and assigned `F12` as the default shortcut.
- `CellSelector.java`: Detected the `SCREENSHOT` action during gameplay and called `ScreenshotManager.capture()` to trigger screenshot capture.
- `ScreenshotManager.java`: Added a new manager class to capture the framebuffer, fix image orientation, generate a timestamped PNG filename, select the save directory, and save the screenshot.
- `SPDSettings.java`: Added screenshot directory settings so the selected screenshot folder can be stored and reused.
- `WndSettings.java`: Added the `Screenshot Folder` option in the settings window and handled folder selection safely, including fullscreen related cases.
- `PlatformSupport.java` / `DesktopPlatformSupport.java`: Added platform level folder selection support for desktop environments.
- `windows.properties` / `misc.properties`: Added UI labels and feedback messages for the screenshot shortcut, screenshot folder option, and screenshot save success or failure.

Overall, the screenshot feature is configurable through the original key binding system. Screenshots are saved as timestamped PNG files, and users can choose where screenshots are stored.

### SQA Activities

#### Part 1: Code Level Testing

Unit testing was used to verify implementation logic that can be checked without launching the full game.

1. `core/src/test/java/com/shatteredpixel/shatteredpixeldungeon/ScreenshotActionTest.java`

   - **Test Case 1: Screenshot action exists**  
     Verified that `SCREENSHOT` was registered as a normal `SPDAction`.

   - **Test Case 2: Correct action name**  
     Verified that the action name is `screenshot`, which is used by the key binding and message systems.

   - **Test Case 3: Default shortcut**  
     Verified that `F12` is mapped to the screenshot action by default.

   - **Test Case 4: KeyBindings integration**  
     Verified that `F12` resolves to `SCREENSHOT` through the existing `KeyBindings` system.

   These tests verify that the screenshot feature is registered in the existing key binding system.

2. `core/src/test/java/com/shatteredpixel/shatteredpixeldungeon/ScreenshotMessageResourcesTest.java`

   - **Test Case 1: Window message keys**  
     Verified that UI labels exist for `Take Screenshot`, `Screenshot Folder`, `Choose Screenshot Folder`, and current screenshot folder display.

   - **Test Case 2: Result message keys**  
     Verified that success and failure messages exist for `Screenshot saved` and `Screenshot save failed`.

   These tests verify that the required message keys for the screenshot shortcut, screenshot folder setting, and screenshot result messages exist in the property files.

#### Part 2: In Game Black Box Testing

Manual system level black box testing was used because the screenshot feature depends on the actual running game environment, including keyboard input, gameplay rendering, file output, and folder selection.

1. **Build Verification**  
   Compiled the project using Gradle to ensure the screenshot related source code did not introduce build errors.

2. **Default Shortcut Test**  
   Pressed the default screenshot shortcut during gameplay and verified that the screenshot action was triggered.

3. **Key Rebinding Test**  
   Changed the screenshot shortcut in the key binding settings and verified that the new key worked correctly.

4. **Custom Save Folder Test**  
   Selected a screenshot folder in the settings window and verified that screenshots were saved to the selected location.

5. **File Output Test**  
   Checked the generated PNG file, timestamped filename, and captured gameplay image.

6. **Non Interruption Test**  
   Took screenshots during active gameplay and verified that the game did not pause, freeze, or interrupt player control.

7. **Fullscreen and Failure Handling Test**  
   Tested folder selection in fullscreen mode and verified that the game did not crash.

8. **Regression Testing**  
   Tested movement, inventory, quick slot actions, menu navigation, and other shortcuts to confirm existing controls were not affected.

### Outcome

After this improvement, players can capture the current gameplay scene using a configurable shortcut. The screenshot is saved as a PNG file, and users can choose the folder where screenshots are stored. This improves operability because players can record gameplay moments, bugs, or evidence without relying on external tools. The feature also keeps the original gameplay flow unchanged and does not affect existing controls.

---

## Improvement 2: Unavailable Item Visual Feedback in Quick-slot

### Original Problem

In the original game, items placed in the quick slot could still appear visually available even when they were currently unusable, depleted, or invalid to use in the current context.

This means that the quick slot UI did not always clearly communicate whether a selected item could actually perform its intended action at that moment.

### Why it is a Quality Issue

This may confuse players because the quick slot icon does not clearly reflect the actual availability of the item. Players may only discover that an item is unavailable after clicking it, which can lead to unnecessary interactions, failed actions, or misunderstanding of the item state.

### Why it is not just a New Feature

This improvement does not add new items, new actions, or new gameplay mechanics. It only improves the visual feedback of the existing quick slot system. The item’s original behaviour remains unchanged, and the item is still clickable. The improvement only helps the player understand the current availability of the item before interacting with it.

### Quality Attributes and Corresponding Changes

| ISO/IEC 25010 Attribute | Corresponding Change | Explanation |
|---|---|---|
| **Operability** | Dim unavailable quick slot items and show availability before click | Players can recognise whether an item is currently usable before interacting with it. This improves ease of use and reduces confusing operations. |
| **Functional Suitability** | Reflect actual item usability in the quick slot UI | The quick slot better fulfils its role of representing item shortcuts by showing whether the item can currently perform its intended action. |
| **Maintainability** | Use a general quick slot usability check | The implementation uses shared logic, so future item support is easier to add. |

### Source Code Implementation

This improvement was implemented by linking item availability detection with the existing quick slot UI rendering system.

- `Item.java`: Added `isQuickslotUsable(Hero)` as a general quick slot availability check.
- `ItemSlot.java`: Added an unavailable visual state and applied reduced opacity when an item is not currently usable.
- `QuickSlotButton.java`: Connected quick slot item rendering to `isQuickslotUsable()` and refreshed the visual state after clicking.

Unavailable items are shown as dimmed or semi transparent, while usable items remain visually normal. The implementation only changes visual feedback and does not change item mechanics, item effects, quantity logic, charge logic, or gameplay balance.

### SQA Activities

#### Part 1: Code Level Testing

Unit testing was used to verify implementation logic that can be checked without launching the full game.

1. `core/src/test/java/com/shatteredpixel/shatteredpixeldungeon/items/ItemQuickslotUsabilityTest.java`

   - **Test Case 1: Available item**  
     Verified that an item with positive quantity and an available default action is quick slot usable.

   - **Test Case 2: Zero quantity item**  
     Verified that an item with zero quantity is not quick slot usable.

   - **Test Case 3: No default action**  
     Verified that an item without a default action is not quick slot usable.

   - **Test Case 4: Unavailable default action**  
     Verified that an item is not usable when its default action is not currently available.

   These tests verify the basic conditions used by `Item.isQuickslotUsable(...)` to decide whether an item should appear available in the quick slot UI.

2. `core/src/test/java/com/shatteredpixel/shatteredpixeldungeon/ui/QuickslotVisualFeedbackStructureTest.java`

   - **Test Case 1: ItemSlot availability method**  
     Verified that `ItemSlot` supports available and unavailable visual state.

   - **Test Case 2: ItemSlot visual refresh**  
     Verified that `ItemSlot` can refresh its visual state after availability changes.

   - **Test Case 3: QuickSlotButton visual refresh**  
     Verified that quick slot visual state can be refreshed after user interaction.

   These tests verify that the UI methods required by the unavailable quick slot visual feedback improvement exist.

#### Part 2: In Game Black Box Testing

Manual system level black box testing was used because the quick slot feedback depends on runtime item availability and UI rendering.

1. **Build Verification**  
   Compiled the project using Gradle to ensure the quick slot visual feedback changes did not introduce build errors.

2. **Unavailable Item Visual State Test**  
   Tested quick slot items under unavailable, depleted, or invalid use conditions and verified that their icons became dimmed or semi transparent.

3. **Available Item Visual State Test**  
   Tested currently usable quick slot items and verified that they remained visually normal.

4. **Click Behaviour Test**  
   Clicked unavailable quick slot items while they were dimmed and verified that clicking did not incorrectly restore the normal colour.

5. **Recovery Test**  
   Restored items from an unavailable state to an available state and verified that the quick slot icon returned to normal only when the item became usable again.

6. **Special Item Availability Test**  
   Tested special quick slot items with more complex availability conditions and verified that the dimming logic was not limited to simple quantity based items.

7. **Mechanics Preservation Test**  
   Verified that unavailable items remained clickable and kept their original behaviour, messages, and usage rules.

8. **Regression Testing**  
   Tested movement, inventory, quick slot actions, item usage, menu navigation, and combat to confirm existing gameplay behaviour was not affected.

### Outcome

After this improvement, unavailable quick slot items are visually dimmed or semi transparent before the player clicks them. This makes item availability clearer and reduces confusing interactions. The improvement does not change item mechanics or gameplay behaviour, but improves operability and functional suitability by giving players better visual feedback.

---

## Improvement 3: Critical Health Screen Warning

### Original Problem

The original game already has a low health warning where the hero avatar becomes red and flashes when the player’s HP falls below the existing low health condition. However, this warning is limited to a small area of the status pane and can be missed during combat or fast gameplay.

### Why it is a Quality Issue

If players do not notice the low health state, they may fail to heal, retreat, or change strategy in time. This can lead to avoidable mistakes because the critical health information is not visually noticeable enough during active gameplay.

### Why it is not just a New Feature

This improvement does not change HP values, damage calculation, enemy behaviour, healing, combat rules, or game balance. It only improves the visibility of an existing low health state by reusing the original red screen flash logic that already appears when the hero takes damage.

### Quality Attributes and Corresponding Changes

| ISO/IEC 25010 Attribute | Corresponding Change | Explanation |
|---|---|---|
| **Operability** | Make the low health warning more visible | Players can notice critical health more easily and make timely decisions such as healing, retreating, or avoiding risk. |
| **Functional Suitability** | Make the danger state clearer to the player | The health feedback system better informs the player when health is critical. |
| **Maintainability** | Reuse existing low health and damage flash logic | The implementation follows the existing game architecture instead of adding an unrelated warning system. |

### Source Code Implementation

This improvement was implemented by reusing the original low health avatar condition and the existing red damage flash mechanism.

- `StatusPane.java`: Added `lowHealthWarning(int health, int max)` as a shared low health condition.
- `GameScene.java`: Added `LOW_HEALTH_FLASH_INTERVAL`, `LOW_HEALTH_FLASH_DURATION`, and `timeSinceFlash` to control repeated low health screen flashes.
- `GameScene.java`: Added a low health check inside `update()` to trigger the warning only when the hero is alive and HP is below the existing condition.
- `GameScene.java`: Added `flashLowHealthWarning()` to trigger a subtle red screen flash approximately every 2 seconds.
- `GameScene.java`: Updated the existing `flash()` method to reset `timeSinceFlash` when the original damage flash happens, so the low health warning does not conflict with the stronger damage flash.
- `PixelScene.java`: Added `fadeIn(int color, boolean light, float duration)` so the low health warning can reuse the existing full screen fade and flash mechanism with a shorter duration.

When the hero remains below the existing low health condition of **HP / max HP ≤ 0.3**, the screen gently flashes red every 2 seconds. The original HP condition, damage calculation, healing, combat logic, and game balance were not changed.

### SQA Activities

#### Part 1: Code Level Testing

Unit testing was used to verify implementation logic that can be checked without launching the full game.

1. `core/src/test/java/com/shatteredpixel/shatteredpixeldungeon/ui/StatusPaneLowHealthWarningTest.java`

   - **Test Case 1: HP exactly at 30%**  
     Verified that HP at the existing low health condition triggers the warning.

   - **Test Case 2: HP below 30%**  
     Verified that HP below the condition triggers the warning.

   - **Test Case 3: HP above 30%**  
     Verified that HP above the condition does not trigger the warning.

   - **Test Case 4: Invalid max HP**  
     Verified that invalid maximum HP does not incorrectly trigger the warning.

   These tests verify the HP condition logic used by both the original avatar warning and the new critical health screen warning.

2. `core/src/test/java/com/shatteredpixel/shatteredpixeldungeon/scenes/CriticalHealthWarningStructureTest.java`

   - **Test Case 1: Flash interval**  
     Verified that the repeated low health flash interval is set to approximately 2 seconds.

   - **Test Case 2: Flash duration**  
     Verified that a custom low health flash duration is defined.

   - **Test Case 3: Low health flash method**  
     Verified that `GameScene` contains the method used to trigger the warning.

   - **Test Case 4: Custom fade duration support**  
     Verified that `PixelScene` supports fade in with a custom duration.

   These tests verify that the repeated low health screen warning is connected to `GameScene` and reuses the `PixelScene` fade mechanism.

#### Part 2: In Game Black Box Testing

Manual system level black box testing was used because the health warning depends on the hero’s runtime HP state and screen flash rendering.

1. **Build Verification**  
   Compiled the project using Gradle to ensure the changes to `GameScene`, `PixelScene`, and `StatusPane` did not introduce build errors.

2. **Existing Condition Verification**  
   Reduced the hero’s HP to the original low health state and verified that the warning reused the existing `HP / max HP ≤ 0.3` condition.

3. **HP Above Condition Test**  
   Kept the hero’s HP above the low health condition and verified that the repeated red screen flash did not appear.

4. **HP Below Condition Test**  
   Reduced the hero’s HP below the existing condition and verified that the repeated red screen flash appeared.

5. **Flash Interval Test**  
   Observed the repeated warning during gameplay and verified that the red flash appeared approximately every 2 seconds.

6. **Original Damage Flash Regression Test**  
   Took damage above and below the low health condition and verified that the original stronger damage flash still worked.

7. **Healing Recovery Test**  
   Healed the hero above the low health condition and verified that the repeated red screen flash stopped.

8. **Gameplay Regression Testing**  
   Tested movement, combat, inventory, quick slot actions, menus, and UI visibility while the warning was active to confirm existing gameplay was not affected.

### Outcome

After this improvement, the game provides a more noticeable warning when the hero remains in a low health state. The screen gently flashes red every 2 seconds by reusing the original damage flash logic, while the original avatar warning still remains. This improves operability and functional suitability because players can notice critical health more easily and respond in time. The improvement does not change combat mechanics, HP values, healing, enemy behaviour, or game balance.
