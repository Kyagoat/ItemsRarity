package net.Kyap.ItemsRarity.util.effects;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import static net.Kyap.ItemsRarity.util.effects.data.EffectConfigHelper.getEffectName;

public class DurabilityEffect implements GearEffect {

    @Override
    public String getId() {
        return "durability";
    }

    @Override
    public GearEffect getInstance() {
        return null;
    }

    @Override
    public boolean isApplicableTo(ItemStack stack) {
        // Ici tu choisis sur quoi ça peut s’appliquer (armes, armures...)
        return stack.isDamageableItem();
    }

    @Override
    public void applyEffect(ItemStack stack, CompoundTag tag, float effectValue) {
        // Stocker la valeur de l'effet dans le tag NBT
        tag.putFloat("DurabilityEffect", effectValue);

        // Ajouter l'ID de l'effet à la liste des effets
        ListTag effectsList = tag.getList("CustomEffects", 8);
        effectsList.add(StringTag.valueOf(getId()));
        tag.put("CustomEffects", effectsList);
    }

    @Override
    public void removeEffect(ItemStack weapon, CompoundTag tag) {
        // Supprimer la valeur spécifique de l'effet
        if (tag.contains("DurabilityEffect")) {
            tag.remove("DurabilityEffect");
        }
    }

    @Override
    public void onTick(LivingEntity holder, ItemStack stack, Rarity rarity) {
        GearEffect.super.onTick(holder, stack, rarity);
    }

    @Override
    public float getValueByRarity(ItemStack weapon) {
        if (!weapon.hasTag()) return 0.0f;

        CompoundTag tag = weapon.getTag();
        if (tag == null || !tag.contains("DurabilityEffect")) return 0.0f;

        return tag.getFloat("DurabilityEffect");
    }
}
