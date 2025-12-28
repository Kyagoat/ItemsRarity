package net.Kyap.ItemsRarity.util.effects.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.Random;

public class EffectConfigHelper {
    
    private static final Random RANDOM = new Random();

    /**
     * Vérifie si un item correspond au tag spécifié dans la configuration
     */
    public static boolean isItemValidForEffect(ItemStack stack, String effectId) {
        EffectJsonData config = EffectDataManager.getEffectConfig(effectId);

        if (config == null) {
            return false;
        }

        if (config.tag == null) {
            return false;
        }

        try {
            ResourceLocation tagLocation = new ResourceLocation(config.tag);
            TagKey<Item> itemTag = TagKey.create(BuiltInRegistries.ITEM.key(), tagLocation);
            return stack.is(itemTag);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtient la valeur aléatoire de l'effet basée sur la rareté de l'item
     */
    public static float getRandomEffectValue(String effectId, Rarity rarity) {
        EffectJsonData config = EffectDataManager.getEffectConfig(effectId);
        if (config == null) return 0.0f;

        EffectJsonData.RarityConfig rarityConfig = config.rarity_config.get(rarity.name().toLowerCase());
        if (rarityConfig == null) return 0.0f;

        if (rarityConfig.min == rarityConfig.max) {
            return rarityConfig.min;
        }

        return rarityConfig.min + RANDOM.nextFloat() * (rarityConfig.max - rarityConfig.min);
    }

    /**
     * Obtient le nom de l'effet
     */
    public static String getEffectName(String effectId) {
        EffectJsonData config = EffectDataManager.getEffectConfig(effectId);
        return config != null ? config.name : null;
    }

    /**
     * Obtient le poids (chance d'apparition) de l'effet pour une rareté donnée
     */
    public static int getEffectWeight(String effectId, Rarity rarity) {
        EffectJsonData config = EffectDataManager.getEffectConfig(effectId);
        if (config == null) return 0;

        EffectJsonData.RarityConfig rarityConfig = config.rarity_config.get(rarity.name().toLowerCase());
        return rarityConfig != null ? rarityConfig.weight : 0;
    }

    /**
     * Vérifie si un effet peut apparaître sur une rareté donnée
     */
    public static boolean canEffectAppearOnRarity(String effectId, Rarity rarity) {
        return getEffectWeight(effectId, rarity) > 0;
    }
}
