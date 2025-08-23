package net.Kyap.ItemsRarity.util.rarity.data;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Map;

public class RarityTooltipHelper {
    
    /**
     * Ajoute les tooltips de chance de rareté pour un matériau donné
     */
    public static void addRarityTooltips(List<Component> pTooltipComponents, String materialId) {
        Map<String, Float> rarityChances = RarityRatesDataManager.getUpgradeRarityChances(materialId);
        if (rarityChances == null || rarityChances.isEmpty()) return;
        
        // Ajouter le titre des chances de rareté
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(Component.translatable("tooltip.rarity.chances").withStyle(ChatFormatting.GRAY));

        // Définir l'ordre des raretés par niveau
        String[] rarityOrder = {"common", "uncommon", "rare", "epic", "legendary", "mythic"};
        
        // Ajouter chaque rareté dans l'ordre de niveau
        for (String rarityName : rarityOrder) {
            Float percentage = rarityChances.get(rarityName);
            if (percentage != null && percentage > 0) {
                String percentageStr = String.format("%.1f%%", percentage);
                String translationKey = "tooltip.rarity." + rarityName.toLowerCase();
                pTooltipComponents.add(Component.translatable(translationKey, percentageStr).withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
