package com.shatteredpixel.shatteredpixeldungeon.ui;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Unit tests for the structure of the quick-slot visual feedback implementation.
 *
 * These tests verify that the UI methods required by the unavailable quick-slot
 * visual feedback improvement exist.
 */
public class QuickslotVisualFeedbackStructureTest {

    // Test Case 1: ItemSlot availability method
    @Test
    public void itemSlotShouldHaveAvailableMethod() throws Exception {
        Method method = ItemSlot.class.getDeclaredMethod("available", boolean.class);

        assertNotNull(method);
    }

    // Test Case 2: ItemSlot visual refresh
    @Test
    public void itemSlotShouldHaveRefreshVisualStateMethod() throws Exception {
        Method method = ItemSlot.class.getDeclaredMethod("refreshVisualState");

        assertNotNull(method);
    }

    // Test Case 3: QuickSlotButton visual refresh
    @Test
    public void quickSlotButtonShouldHaveRefreshVisualStateMethod() throws Exception {
        Method method = QuickSlotButton.class.getDeclaredMethod("refreshVisualState");

        assertNotNull(method);
    }
}