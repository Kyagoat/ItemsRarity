package net.Kyap.ItemsRarity.util.effects;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import static net.Kyap.ItemsRarity.util.effects.data.EffectConfigHelper.getEffectName;

public interface GearEffect {
    String name = null;

    String getId(); // Exemple : "life_steal"

    GearEffect getInstance();

    boolean isApplicableTo(ItemStack stack); // ex : seulement pour les épées

    void applyEffect(ItemStack weapon, CompoundTag tag, float effectValue); // modifier les nbt avec la valeur calculée

    default void removeEffect(ItemStack weapon, CompoundTag tag) {} // supprimer les valeurs NBT de cet effet

    default void onTick(LivingEntity holder, ItemStack stack, Rarity rarity) {}

    default void onHit(LivingEntity attacker, LivingEntity target, ItemStack weapon, LivingHurtEvent event) {}

    float getValueByRarity(ItemStack weapon);

    default String getTooltip(float effectValue, ItemStack weapon) {
        float percent = effectValue * 100f;
        ChatFormatting formatting = weapon.getRarity().color;
        String colorCode = "§" + formatting.getChar();
        String effectName = getEffectName(getId());

        String sign = percent >= 0 ? "+" : "";
        return String.format("%s%s%.1f%% %s", colorCode, sign, percent, effectName);
    }
}
