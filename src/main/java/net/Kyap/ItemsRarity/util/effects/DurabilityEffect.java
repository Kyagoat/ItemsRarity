package net.Kyap.ItemsRarity.util.effects;

import net.Kyap.ItemsRarity.util.effects.data.EffectConfigHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;

public class DurabilityEffect implements GearEffect {

    @Override
    public String getId() {
        return "durability";
    }

    @Override
    public boolean isApplicableTo(ItemStack stack) {
        return stack.isDamageableItem() && EffectConfigHelper.isItemValidForEffect(stack, getId());
    }

    @Override
    public void applyEffect(ItemStack stack, CompoundTag tag, float effectValue) {
        tag.putFloat("DurabilityEffect", effectValue);

        ListTag effectsList = tag.getList("CustomEffects", 8);
        boolean exists = false;
        for(int i = 0; i < effectsList.size(); i++) {
            if(effectsList.getString(i).equals(getId())) exists = true;
        }
        if (!exists) {
            effectsList.add(StringTag.valueOf(getId()));
            tag.put("CustomEffects", effectsList);
        }
    }

    @Override
    public void removeEffect(ItemStack stack, CompoundTag tag) {
        if (tag.contains("DurabilityEffect")) {
            tag.remove("DurabilityEffect");
        }
    }

    @Override
    public float getValueByRarity(ItemStack stack) {
        if (!stack.hasTag()) return 0.0f;
        CompoundTag tag = stack.getTag();
        return (tag != null && tag.contains("DurabilityEffect")) ? tag.getFloat("DurabilityEffect") : 0.0f;
    }
}