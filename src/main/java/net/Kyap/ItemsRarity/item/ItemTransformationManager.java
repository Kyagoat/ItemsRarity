package net.Kyap.ItemsRarity.item;

import net.Kyap.ItemsRarity.data.ItemTier;
import net.Kyap.ItemsRarity.data.ItemTierHelper;
import net.Kyap.ItemsRarity.data.TierManager;
import net.Kyap.ItemsRarity.util.ModRarities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Gestionnaire pour transformer les items avec le nouveau système de tiers data-driven
 */
public class ItemTransformationManager {
    
    /**
     * Transforme un ItemStack vers une version avec le tier spécifié
     */
    public static ItemStack transformItemWithRarity(ItemStack originalStack, ModRarities.ModRarity newRarity) {
        if (originalStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        
        // Convertir ModRarity vers le nom du tier
        String tierName = newRarity.name().toLowerCase();
        
        // Créer une copie de l'ItemStack
        ItemStack newStack = originalStack.copy();
        
        // Appliquer le nouveau tier
        ItemTierHelper.setItemTier(newStack, tierName);
        
        return newStack;
    }
    
    /**
     * Transforme un ItemStack vers une version avec le tier spécifié (nouveau système)
     */
    public static ItemStack transformItemWithTier(ItemStack originalStack, ItemTier newTier) {
        if (originalStack.isEmpty() || newTier == null) {
            return ItemStack.EMPTY;
        }
        
        // Créer une copie de l'ItemStack
        ItemStack newStack = originalStack.copy();
        
        // Appliquer le nouveau tier
        ItemTierHelper.setItemTier(newStack, newTier);
        
        return newStack;
    }
    
    /**
     * Transforme un ItemStack vers une version avec le tier spécifié par nom
     */
    public static ItemStack transformItemWithTier(ItemStack originalStack, String tierName) {
        ItemTier tier = TierManager.getInstance().getTier(tierName);
        return transformItemWithTier(originalStack, tier);
    }
    
    /**
     * Récupère la rareté actuelle d'un ItemStack (compatible avec l'ancien système)
     */
    public static ModRarities.ModRarity getCurrentRarity(ItemStack stack) {
        if (stack.isEmpty()) {
            return ModRarities.ModRarity.COMMON;
        }
        
        ItemTier tier = ItemTierHelper.getItemTier(stack);
        if (tier == null) {
            return ModRarities.ModRarity.COMMON;
        }
        
        // Convertir le tier vers ModRarity pour la compatibilité
        try {
            return ModRarities.ModRarity.valueOf(tier.getId().getPath().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ModRarities.ModRarity.COMMON;
        }
    }
    
    /**
     * Récupère l'item original depuis un ItemStack transformé
     */
    public static Item getOriginalItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        
        // Dans notre nouveau système, l'item original est simplement l'item actuel
        // car nous n'utilisons plus des items transformés mais des NBT tags
        return stack.getItem();
    }
    
    /**
     * Vérifie si un ItemStack est un item transformé
     */
    public static boolean isTransformedItem(ItemStack stack) {
        return ItemTierHelper.hasCustomTier(stack);
    }
}
