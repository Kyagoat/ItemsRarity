package net.Kyap.ItemsRarity.util.rarity.data;

import net.Kyap.ItemsRarity.util.ModRarities;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class RarityConfigHelper {
    
    /**
     * Obtient la configuration de rareté pour un matériau donné
     */
    public static RarityJsonData getRarityConfig(String materialId) {
        return RarityDataManager.getRarityConfig(materialId);
    }
    
    /**
     * Fait un roll pour déterminer le nouveau tier selon la ressource utilisée
     */
    public static ModRarities.ModRarity rollNewTier(ItemStack stack) {
        String materialId = getMaterialId(stack);
        if (materialId == null) return null;
        
        RarityJsonData config = getRarityConfig(materialId);
        if (config == null) return null;
        
        double roll = ThreadLocalRandom.current().nextDouble();
        double cumulative = 0.0;
        
        // Note: On assume que les chances sont déjà cumulatives ou qu'on les accumule
        for (String rarityName : config.rarity_chances.keySet()) {
            cumulative += config.rarity_chances.get(rarityName);
            if (roll < cumulative) {
                return getModRarityFromName(rarityName);
            }
        }
        
        // Fallback - retourner la dernière rareté si on dépasse (peut arriver avec des arrondis)
        String[] rarityNames = config.rarity_chances.keySet().toArray(new String[0]);
        if (rarityNames.length > 0) {
            return getModRarityFromName(rarityNames[rarityNames.length - 1]);
        }
        
        return ModRarities.ModRarity.COMMON;
    }
    
    /**
     * Obtient l'ID du matériau basé sur l'item
     */
    private static String getMaterialId(ItemStack stack) {
        String itemName = stack.getItem().toString();
        if (itemName.contains("bismuth")) return "bismuth";
        if (itemName.contains("sulfur")) return "sulfur";
        if (itemName.contains("flourite")) return "flourite";
        return null;
    }
    
    /**
     * Convertit un nom de rareté en ModRarity
     */
    private static ModRarities.ModRarity getModRarityFromName(String rarityName) {
        return switch (rarityName.toLowerCase()) {
            case "uncommon" -> ModRarities.ModRarity.UNCOMMON;
            case "rare" -> ModRarities.ModRarity.RARE;
            case "epic" -> ModRarities.ModRarity.EPIC;
            case "legendary" -> ModRarities.ModRarity.LEGENDARY;
            case "mythic" -> ModRarities.ModRarity.MYTHIC;
            default -> ModRarities.ModRarity.COMMON;
        };
    }
    
    /**
     * Obtient le pourcentage formaté pour une rareté donnée d'un matériau
     */
    public static String getRarityPercentage(String materialId, String rarityName) {
        RarityJsonData config = getRarityConfig(materialId);
        if (config == null || !config.rarity_chances.containsKey(rarityName)) {
            return "0%";
        }
        
        float chance = config.rarity_chances.get(rarityName);
        return String.format("%.0f%%", chance * 100);
    }

}
