package net.Kyap.ItemsRarity.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Utilitaires pour gérer les tiers sur les ItemStacks
 */
public class ItemTierHelper {
    
    private static final String TIER_TAG = "ItemsRarity_Tier";
    
    /**
     * Définit le tier d'un ItemStack
     */
    public static void setItemTier(ItemStack stack, ItemTier tier) {
        if (stack.isEmpty() || tier == null) return;
        
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(TIER_TAG, tier.getId().toString());
    }
    
    /**
     * Définit le tier d'un ItemStack par son ID
     */
    public static void setItemTier(ItemStack stack, String tierId) {
        ItemTier tier = TierManager.getInstance().getTier(tierId);
        if (tier != null) {
            setItemTier(stack, tier);
        }
    }
    
    /**
     * Récupère le tier d'un ItemStack
     */
    public static ItemTier getItemTier(ItemStack stack) {
        if (stack.isEmpty()) return null;
        
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(TIER_TAG)) {
            return TierManager.getInstance().getTier("common"); // Tier par défaut
        }
        
        String tierIdString = tag.getString(TIER_TAG);
        ResourceLocation tierId = ResourceLocation.parse(tierIdString);
        return TierManager.getInstance().getTier(tierId);
    }
    
    /**
     * Vérifie si un ItemStack a un tier personnalisé
     */
    public static boolean hasCustomTier(ItemStack stack) {
        if (stack.isEmpty()) return false;
        
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(TIER_TAG);
    }
    
    /**
     * Supprime le tier personnalisé d'un ItemStack
     */
    public static void removeItemTier(ItemStack stack) {
        if (stack.isEmpty()) return;
        
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(TIER_TAG)) {
            tag.remove(TIER_TAG);
            if (tag.isEmpty()) {
                stack.setTag(null);
            }
        }
    }
    
    /**
     * Améliore un ItemStack au tier suivant
     */
    public static boolean upgradeItemTier(ItemStack stack) {
        ItemTier currentTier = getItemTier(stack);
        if (currentTier == null) {
            currentTier = TierManager.getInstance().getTier("common");
        }
        
        ItemTier nextTier = TierManager.getInstance().getNextTier(currentTier);
        if (nextTier == null) {
            return false; // Déjà au tier maximum
        }
        
        setItemTier(stack, nextTier);
        return true;
    }
    
    /**
     * Récupère le nom affiché du tier
     */
    public static String getTierDisplayName(ItemStack stack) {
        ItemTier tier = getItemTier(stack);
        return tier != null ? tier.getName() : "Common";
    }
    
    /**
     * Récupère la couleur du tier
     */
    public static String getTierColor(ItemStack stack) {
        ItemTier tier = getItemTier(stack);
        return tier != null ? tier.getColor() : "#FFFFFF";
    }
}
