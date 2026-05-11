package com.shatteredpixel.shatteredpixeldungeon.ui;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the shared low-health warning condition.
 *
 * These tests verify the HP threshold logic used by both the original
 * avatar warning and the new critical health screen warning.
 */
public class StatusPaneLowHealthWarningTest {

    // Test Case 1: HP exactly at 30%
    @Test
    public void hpAtThirtyPercentShouldTriggerWarning() {
        assertTrue(StatusPane.lowHealthWarning(30, 100));
    }

    // Test Case 2: HP below 30%
    @Test
    public void hpBelowThirtyPercentShouldTriggerWarning() {
        assertTrue(StatusPane.lowHealthWarning(20, 100));
    }

    // Test Case 3: HP above 30%
    @Test
    public void hpAboveThirtyPercentShouldNotTriggerWarning() {
        assertFalse(StatusPane.lowHealthWarning(31, 100));
    }

    // Test Case 4: Invalid max HP
    @Test
    public void invalidMaxHpShouldNotTriggerWarning() {
        assertFalse(StatusPane.lowHealthWarning(1, 0));
    }
}