package net.Kyap.ItemsRarity.integration;

import net.Kyap.ItemsRarity.data.ItemTier;
import net.Kyap.ItemsRarity.data.ItemTierHelper;
import net.minecraft.world.item.ItemStack;

/**
 * Classe d'intégration pour Obscure Tooltips
 * Permet au mod de détecter nos tiers personnalisés
 */
public class ObscureTooltipsIntegration {
    
    /**
     * Méthode appelée par Obscure Tooltips pour vérifier la rareté d'un item
     * Cette méthode sera détectée automatiquement par le mod si elle existe
     */
    public static String getRarity(ItemStack stack) {
        if (stack.isEmpty()) {
            return "common";
        }
        
        ItemTier tier = ItemTierHelper.getItemTier(stack);
        if (tier == null) {
            return "common";
        }
        
        // Retourner l'ID du tier pour qu'Obscure Tooltips puisse le matcher
        return tier.getId().getPath();
    }
    
    /**
     * Méthode alternative pour exposer les tiers avec plus de détails
     */
    public static ItemTierInfo getTierInfo(ItemStack stack) {
        ItemTier tier = ItemTierHelper.getItemTier(stack);
        if (tier == null) {
            return new ItemTierInfo("common", "Common", "#FFFFFF");
        }
        
        return new ItemTierInfo(
            tier.getId().getPath(),
            tier.getName(),
            tier.getColor()
        );
    }
    
    /**
     * Classe de données pour transférer les informations de tier
     */
    public static class ItemTierInfo {
        public final String id;
        public final String displayName;
        public final String color;
        
        public ItemTierInfo(String id, String displayName, String color) {
            this.id = id;
            this.displayName = displayName;
            this.color = color;
        }
    }
}
