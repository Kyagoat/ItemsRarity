package net.Kyap.ItemsRarity.util;

import net.minecraft.world.item.ItemStack;

public class RarityManager {

    public static boolean canHaveRarity(ItemStack stack) {
        // Add logic to determine if the item can have a rarity
        return stack != null && !stack.isEmpty();
    }
}