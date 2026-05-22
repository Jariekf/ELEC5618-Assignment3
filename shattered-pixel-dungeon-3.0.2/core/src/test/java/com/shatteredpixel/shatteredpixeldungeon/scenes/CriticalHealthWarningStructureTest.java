package com.shatteredpixel.shatteredpixeldungeon.scenes;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Unit tests for the source-code structure of the critical health warning.
 *
 * These tests verify that the repeated low-health screen warning is connected
 * to GameScene and reuses the PixelScene fade mechanism.
 */
public class CriticalHealthWarningStructureTest {

    // Test Case 1: Flash interval
    @Test
    public void lowHealthFlashIntervalShouldBeTwoSeconds() throws Exception {
        Field field = GameScene.class.getDeclaredField("LOW_HEALTH_FLASH_INTERVAL");
        field.setAccessible(true);

        assertEquals(2f, field.getFloat(null), 0.001f);
    }

    // Test Case 2: Flash duration
    @Test
    public void lowHealthFlashDurationShouldBeDefined() throws Exception {
        Field field = GameScene.class.getDeclaredField("LOW_HEALTH_FLASH_DURATION");
        field.setAccessible(true);

        assertTrue(field.getFloat(null) > 0f);
    }

    // Test Case 3: Low-health flash method
    @Test
    public void gameSceneShouldHaveFlashLowHealthWarningMethod() throws Exception {
        Method method = GameScene.class.getDeclaredMethod("flashLowHealthWarning");

        assertNotNull(method);
    }

    // Test Case 4: Custom fade duration support
    @Test
    public void pixelSceneShouldSupportFadeInWithCustomDuration() throws Exception {
        Method method = PixelScene.class.getDeclaredMethod(
                "fadeIn",
                int.class,
                boolean.class,
                float.class
        );

        assertNotNull(method);
    }
}