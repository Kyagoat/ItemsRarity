package net.Kyap.ItemsRarity.util.effects;

import net.Kyap.ItemsRarity.util.effects.data.EffectConfigHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import static net.Kyap.ItemsRarity.util.effects.data.EffectConfigHelper.getEffectName;

public class CritChanceEffect implements GearEffect {

    @Override
    public String getId() {
        return "crit_chance";
    }

    @Override
    public GearEffect getInstance() {
        return this;
    }

    @Override
    public void applyEffect(ItemStack stack, CompoundTag tag, float effectValue) {
        // Stocker la valeur de l'effet dans le tag NBT
        tag.putFloat("CritChanceEffect", effectValue);
        
        // Ajouter l'ID de l'effet à la liste des effets
        ListTag effectsList = tag.getList("CustomEffects", 8);
        effectsList.add(StringTag.valueOf(getId()));
        tag.put("CustomEffects", effectsList);
    }

    @Override
    public void removeEffect(ItemStack weapon, CompoundTag tag) {
        // Supprimer la valeur spécifique de l'effet Life Steal
        if (tag.contains("CritChanceEffect")) {
            tag.remove("CritChanceEffect");
        }
    }

    @Override
    public void onTick(LivingEntity holder, ItemStack stack, Rarity rarity) {
        GearEffect.super.onTick(holder, stack, rarity);
    }

    @Override
    public boolean isApplicableTo(ItemStack stack) {
        return EffectConfigHelper.isItemValidForEffect(stack, getId());
    }

    @Override
    public float getValueByRarity(ItemStack weapon) {
        if (!weapon.hasTag()) return 0.0f;
        
        CompoundTag tag = weapon.getTag();
        if (tag == null || !tag.contains("CritChanceEffect")) return 0.0f;
        
        return tag.getFloat("CritChanceEffect");
    }

    @Override
    public String getTooltip(float effectValue, ItemStack weapon) {
        float percent = effectValue * 100f;
        ChatFormatting formatting = weapon.getRarity().color;
        String colorCode = "§" + formatting.getChar();
        String effectName = getEffectName(getId());
        return String.format("%s+%.1f%% %s", colorCode, percent, effectName);
    }
}
