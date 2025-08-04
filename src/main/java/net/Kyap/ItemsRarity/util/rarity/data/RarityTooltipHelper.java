package net.Kyap.ItemsRarity.util.rarity.data;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

public class RarityTooltipHelper {
    
    /**
     * Ajoute les tooltips de chance de rareté pour un matériau donné
     */
    public static void addRarityTooltips(List<Component> pTooltipComponents, String materialId) {
        RarityJsonData config = RarityConfigHelper.getRarityConfig(materialId);
        if (config == null) return;
        // Ajouter le titre des chances de rareté
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(Component.translatable("tooltip.rarity.chances").withStyle(ChatFormatting.GRAY));

        // Ajouter chaque rareté avec son pourcentage
        for (String rarityName : config.rarity_chances.keySet()) {
            String percentage = RarityConfigHelper.getRarityPercentage(materialId, rarityName);
            String translationKey = "tooltip.rarity." + rarityName.toLowerCase();
            pTooltipComponents.add(Component.translatable(translationKey, percentage).withStyle(ChatFormatting.GRAY));
        }
    }
}
