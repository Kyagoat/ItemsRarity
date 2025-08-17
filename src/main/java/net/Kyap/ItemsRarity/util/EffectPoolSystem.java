package net.Kyap.ItemsRarity.util;

import net.Kyap.ItemsRarity.util.ModRarities;
import net.Kyap.ItemsRarity.util.effects.EffectRegistry;
import net.Kyap.ItemsRarity.util.effects.data.EffectConfigHelper;
import net.Kyap.ItemsRarity.util.effects.data.EffectDataManager;
import net.Kyap.ItemsRarity.util.effects.data.EffectJsonData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.*;

public class EffectPoolSystem {

    private static final Map<Rarity, Integer> EFFECTS_COUNT = Map.of(
            Rarity.COMMON, 1,
            Rarity.UNCOMMON, 1,
            Rarity.RARE, 2,
            Rarity.EPIC, 3,
            ModRarities.LEGENDARY, 4,
            ModRarities.MYTHIC, 4
    );

    private static final Random RANDOM = new Random();

    /**
     * Apply random effects to an item based on its rarity.
     */
    public static void rollEffectsOnItem(ItemStack stack) {
        EffectRegistry.removeAllEffects(stack);

        // Get max number of applicable effects based on the object's rarity
        int maxEffects = EFFECTS_COUNT.getOrDefault(stack.getRarity(), 1);
        
        List<String> applicableEffects = getApplicableEffectsForItem(stack);
        
        if (applicableEffects.isEmpty()) {
            return;
        }
        
        Set<String> selectedEffects = selectRandomEffects(applicableEffects, stack.getRarity(), maxEffects);
        for (String effectId : selectedEffects) {
            EffectRegistry.applyEffectToItem(effectId, stack, stack.getOrCreateTag());
        }
    }

    /**
     * Gets all applicable effects for the given item stack.
     */
    private static List<String> getApplicableEffectsForItem(ItemStack stack) {
        List<String> applicableEffects = new ArrayList<>();
        
        for (Map.Entry<String, EffectJsonData> entry : EffectDataManager.getAllConfigs().entrySet()) {
            String effectId = entry.getKey();
            
            if (EffectConfigHelper.isItemValidForEffect(stack, effectId) &&
                EffectConfigHelper.canEffectAppearOnRarity(effectId, stack.getRarity())) {
                applicableEffects.add(effectId);
            }
        }
        return applicableEffects;
    }

    /**
     * Select random effects based on the rarity and maximum number of effects.
     */
    private static Set<String> selectRandomEffects(List<String> availableEffects, Rarity rarity, int maxEffects) {
        Set<String> selectedEffects = new HashSet<>();
        List<String> effectsPool = new ArrayList<>(availableEffects);
        
        for (int i = 0; i < maxEffects && !effectsPool.isEmpty(); i++) {
            String selectedEffect = selectWeightedRandomEffect(effectsPool, rarity);
            if (selectedEffect != null) {
                selectedEffects.add(selectedEffect);
                effectsPool.remove(selectedEffect); // Remove selected effect to avoid duplicates
            }
        }
        return selectedEffects;
    }

    /**
     * Select a random effect from the list based on their weights.
     */
    private static String selectWeightedRandomEffect(List<String> effects, Rarity rarity) {
        if (effects.isEmpty()) return null;
        
        // Calculate total weight of all effects
        int totalWeight = 0;
        for (String effectId : effects) {
            totalWeight += EffectConfigHelper.getEffectWeight(effectId, rarity);
        }
        
        if (totalWeight <= 0) return null;
        
        // Select a random effect based on weights
        int randomValue = RANDOM.nextInt(totalWeight); // Random value in range of total weight
        int currentWeight = 0;
        
        for (String effectId : effects) {
            currentWeight += EffectConfigHelper.getEffectWeight(effectId, rarity);
            if (randomValue < currentWeight) {
                return effectId;
            }
        }
        return effects.get(0); // Fallback
    }
}
