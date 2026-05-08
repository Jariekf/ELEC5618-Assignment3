package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Unit tests for the general quick-slot usability logic.
 *
 * These tests verify the basic conditions used by Item.isQuickslotUsable(...)
 * to decide whether an item should appear available in the quick-slot UI.
 */
public class ItemQuickslotUsabilityTest {

    // Test Case 1: Available item
    @Test
    public void itemWithDefaultActionAndPositiveQuantityShouldBeUsable() throws Exception {
        Hero hero = dummyHero();
        TestQuickslotItem item = new TestQuickslotItem("USE", true);
        item.quantity(1);

        assertTrue(item.isQuickslotUsable(hero));
    }

    // Test Case 2: Zero quantity item
    @Test
    public void itemWithZeroQuantityShouldNotBeUsable() throws Exception {
        Hero hero = dummyHero();
        TestQuickslotItem item = new TestQuickslotItem("USE", true);
        item.quantity(0);

        assertFalse(item.isQuickslotUsable(hero));
    }

    // Test Case 3: No default action
    @Test
    public void itemWithoutDefaultActionShouldNotBeUsable() throws Exception {
        Hero hero = dummyHero();
        TestQuickslotItem item = new TestQuickslotItem(null, true);
        item.quantity(1);

        assertFalse(item.isQuickslotUsable(hero));
    }

    // Test Case 4: Unavailable default action
    @Test
    public void itemWithoutAvailableDefaultActionShouldNotBeUsable() throws Exception {
        Hero hero = dummyHero();
        TestQuickslotItem item = new TestQuickslotItem("USE", false);
        item.quantity(1);

        assertFalse(item.isQuickslotUsable(hero));
    }

    /**
     * Creates a Hero object without running the Hero constructor.
     *
     * This avoids loading sprites, textures, backpacks, and other libGDX
     * runtime resources that are not available during unit testing.
     */
    private static Hero dummyHero() throws Exception {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
        return (Hero) unsafe.allocateInstance(Hero.class);
    }

    /**
     * Simple test item used to control whether the default action is exposed
     * through actions(hero).
     */
    private static class TestQuickslotItem extends Item {

        private final boolean exposeDefaultAction;

        TestQuickslotItem(String defaultAction, boolean exposeDefaultAction) {
            this.defaultAction = defaultAction;
            this.exposeDefaultAction = exposeDefaultAction;
        }

        @Override
        public ArrayList<String> actions(Hero hero) {
            ArrayList<String> actions = super.actions(hero);

            if (exposeDefaultAction && defaultAction() != null) {
                actions.add(defaultAction());
            }

            return actions;
        }
    }
}