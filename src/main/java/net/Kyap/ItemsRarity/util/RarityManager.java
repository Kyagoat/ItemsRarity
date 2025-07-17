package net.Kyap.ItemsRarity.util;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;

public class RarityManager {

    public static boolean isItemUpgradable(ItemStack stack) {
    if (stack.isEmpty()) {
        return false;
    }
    // Verify if the item is a tool or weapon
    if (stack.getItem() instanceof SwordItem || 
        stack.getItem() instanceof AxeItem || 
        stack.getItem() instanceof PickaxeItem || 
        stack.getItem() instanceof ShovelItem || 
        stack.getItem() instanceof HoeItem || 
        stack.getItem() instanceof BowItem || 
        stack.getItem() instanceof CrossbowItem ||
        stack.getItem() instanceof TridentItem ||
        stack.getItem() instanceof ArmorItem) {
        
        // Convert Minecraft Rarity to ModRarity and check if upgradable
        ModRarities.ModRarity modRarity = getModRarityFromMinecraft(stack.getRarity());
        if (modRarity == null) {
            return false;
        }
        
        return ModRarities.isRarityUpgradable(modRarity);
    }
    
    return false; // Not a tool or weapon
}
    
    /**
     * Converts a Minecraft Rarity to ModRarity enum
     */
    private static ModRarities.ModRarity getModRarityFromMinecraft(Rarity rarity) {
        for (ModRarities.ModRarity modRarity : ModRarities.ModRarity.values()) {
            if (modRarity.getMinecraftRarity().equals(rarity)) {
                return modRarity;
            }
        }
        return null; // Rarity not found in our enum
    }


}