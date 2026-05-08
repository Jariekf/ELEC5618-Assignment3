package com.shatteredpixel.shatteredpixeldungeon;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Basic resource tests for screenshot-related UI messages.
 *
 * These tests verify that the required message keys for the screenshot
 * shortcut, screenshot folder setting, and screenshot result messages
 * exist in the property files.
 */
public class ScreenshotMessageResourcesTest {

    // Test Case 1: Verify screenshot shortcut and folder setting labels exist
    @Test
    public void windowMessagesShouldContainScreenshotLabels() throws IOException {
        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream(
                "src/main/assets/messages/windows/windows.properties")) {
            properties.load(input);
        }

        assertEquals(
                "Take Screenshot",
                properties.getProperty("windows.wndkeybindings.screenshot")
        );

        assertEquals(
                "Screenshot Folder",
                properties.getProperty("windows.wndsettings$inputtab.screenshot_directory")
        );

        assertEquals(
                "Choose Screenshot Folder",
                properties.getProperty("windows.wndsettings$inputtab.screenshot_directory_title")
        );

        assertNotNull(
                properties.getProperty("windows.wndsettings$inputtab.screenshot_directory_current")
        );
    }

    // Test Case 2: Verify screenshot success and failure messages exist
    @Test
    public void miscMessagesShouldContainScreenshotResultMessages() throws IOException {
        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream(
                "src/main/assets/messages/misc/misc.properties")) {
            properties.load(input);
        }

        assertEquals(
                "Screenshot saved: %s",
                properties.getProperty("misc.screenshot_saved")
        );

        assertEquals(
                "Screenshot save failed: %s",
                properties.getProperty("misc.screenshot_failed")
        );
    }
}