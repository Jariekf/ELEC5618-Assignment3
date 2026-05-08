package com.shatteredpixel.shatteredpixeldungeon;

import com.badlogic.gdx.Input;
import com.watabou.input.GameAction;
import com.watabou.input.KeyBindings;
import com.watabou.input.KeyEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Basic code-level tests for the screenshot shortcut action.
 *
 * These tests verify that the screenshot feature is registered in the
 * existing SPDAction system and connected to the keybinding system.
 */
public class ScreenshotActionTest {

    @Before
    public void setUp() {
        // Reset key bindings before each test to avoid state pollution.
        KeyBindings.setAllBindings(new LinkedHashMap<Integer, GameAction>());
    }

    // Test Case 1: Verify screenshot action exists
    @Test
    public void screenshotActionShouldExist() {
        assertNotNull(SPDAction.SCREENSHOT);
    }

    // Test Case 2: Verify screenshot action has the correct action name
    @Test
    public void screenshotActionShouldHaveCorrectName() {
        assertEquals("screenshot", SPDAction.SCREENSHOT.name());
    }

    // Test Case 3: Verify F12 is the default screenshot shortcut
    @Test
    public void f12ShouldBeDefaultScreenshotShortcut() {
        Map<Integer, ?> defaultBindings = SPDAction.getDefaults();

        assertTrue(defaultBindings.containsKey(Input.Keys.F12));
        assertEquals(SPDAction.SCREENSHOT, defaultBindings.get(Input.Keys.F12));
    }

    // Test Case 4: Verify F12 resolves to screenshot action through KeyBindings
    @Test
    public void f12ShouldResolveToScreenshotThroughKeyBindings() {
        KeyBindings.setAllBindings(SPDAction.getDefaults());

        KeyEvent event = new KeyEvent(Input.Keys.F12, true);
        GameAction action = KeyBindings.getActionForKey(event);

        assertEquals(SPDAction.SCREENSHOT, action);
    }
}