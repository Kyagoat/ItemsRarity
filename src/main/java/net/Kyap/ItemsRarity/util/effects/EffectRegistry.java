package net.Kyap.ItemsRarity.util.effects;

import net.Kyap.ItemsRarity.util.effects.data.EffectConfigHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.HashMap;
import java.util.Map;

public class EffectRegistry {

    private static final Map<String, GearEffect> effects = new HashMap<>();

    public static void registerEffect(String id, GearEffect effect) {
        effects.put(id, effect);
    }

    public static GearEffect getEffect(String id) {
        return effects.get(id);
    }

    public static void initializeEffects() {
        registerEffect("damage", new StandardAttributeEffect("damage"));
        registerEffect("crit_chance", new StandardAttributeEffect("crit_chance"));
        registerEffect("speed", new StandardAttributeEffect("speed"));
        registerEffect("health", new StandardAttributeEffect("health"));
        registerEffect("crit_damage", new StandardAttributeEffect("crit_damage"));
        registerEffect("armor_penetration", new StandardAttributeEffect("armor_penetration"));
        registerEffect("durability", new DurabilityEffect());
        registerEffect("life_steal", new StandardAttributeEffect("life_steal"));
        registerEffect("dodge", new StandardAttributeEffect("dodge"));
        registerEffect("armor_pierce", new StandardAttributeEffect("armor_pierce"));
        registerEffect("arrow_damage", new StandardAttributeEffect("arrow_damage"));
        registerEffect("overheal", new StandardAttributeEffect("overheal"));
    }

    public static void applyEffectToItem(String effectId, ItemStack stack, CompoundTag tag) {
        GearEffect effect = getEffect(effectId);

        if (effect == null || !effect.isApplicableTo(stack)) return;

        Rarity rarity = stack.getRarity();
        float effectValue = EffectConfigHelper.getRandomEffectValue(effectId, rarity);

        if (effectValue != 0) {
            effect.applyEffect(stack, tag, effectValue);
        }
    }

    public static void removeAllEffect(ItemStack stack) {
        if (!stack.hasTag()) return;

        CompoundTag tag = stack.getTag();

        assert tag != null;
        if (tag.contains("CustomEffects")) {
            tag.remove("CustomEffects");
        }

        if (tag.contains("DurabilityEffect")) tag.remove("DurabilityEffect");
        if (tag.contains("durability")) tag.remove("durability");

        if (tag.contains("AttributeModifiers")) {
            tag.remove("AttributeModifiers");
        }
    }
}