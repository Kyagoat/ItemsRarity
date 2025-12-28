package net.Kyap.ItemsRarity.util.effects;

import net.Kyap.ItemsRarity.util.AttributeHelper;
import net.Kyap.ItemsRarity.util.effects.data.EffectConfigHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;

public class StandardAttributeEffect implements GearEffect {

    private final String effectId;

    public StandardAttributeEffect(String effectId) {
        this.effectId = effectId;
    }

    @Override
    public String getId() {
        return this.effectId;
    }

    @Override
    public boolean isApplicableTo(ItemStack stack) {
        return EffectConfigHelper.isItemValidForEffect(stack, this.effectId);
    }

    @Override
    public void applyEffect(ItemStack stack, CompoundTag tag, float effectValue) {
        String nbtKey = Character.toUpperCase(this.effectId.charAt(0)) + this.effectId.substring(1) + "Effect";
        tag.putFloat(nbtKey, effectValue);

        ListTag effectsList = tag.getList("CustomEffects", 8);
        boolean exists = false;
        for (int i = 0; i < effectsList.size(); i++) {
            if (effectsList.getString(i).equals(this.effectId)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            effectsList.add(StringTag.valueOf(this.effectId));
            tag.put("CustomEffects", effectsList);
        }

        AttributeHelper.applyEffectIdAsAttribute(this.effectId, stack, effectValue);
    }

    @Override
    public void removeEffect(ItemStack stack, CompoundTag tag) {
        String nbtKey = Character.toUpperCase(this.effectId.charAt(0)) + this.effectId.substring(1) + "Effect";
        if (tag.contains(nbtKey)) {
            tag.remove(nbtKey);
        }
    }

    @Override
    public float getValueByRarity(ItemStack stack) {
        if (!stack.hasTag()) return 0.0f;
        CompoundTag tag = stack.getTag();
        String nbtKey = Character.toUpperCase(this.effectId.charAt(0)) + this.effectId.substring(1) + "Effect";
        
        if (tag != null && tag.contains(nbtKey)) {
            return tag.getFloat(nbtKey);
        }
        return 0.0f;
    }
}