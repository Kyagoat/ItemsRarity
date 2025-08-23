package net.Kyap.ItemsRarity.util.effects;

import net.Kyap.ItemsRarity.util.effects.data.EffectConfigHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class LifeStealEffect implements GearEffect {

    @Override
    public String getId() {
        return "life_steal";
    }

    @Override
    public void applyEffect(ItemStack stack, CompoundTag tag, float effectValue) {
        // Stocker la valeur de l'effet dans le tag NBT
        tag.putFloat("LifeStealValue", effectValue);
        
        // Ajouter l'ID de l'effet à la liste des effets
        ListTag effectsList = tag.getList("CustomEffects", 8);
        effectsList.add(StringTag.valueOf(getId()));
        tag.put("CustomEffects", effectsList);
    }

    @Override
    public void removeEffect(ItemStack weapon, CompoundTag tag) {
        // Supprimer la valeur spécifique de l'effet Life Steal
        if (tag.contains("LifeStealValue")) {
            tag.remove("LifeStealValue");
        }
    }

    @Override
    public void onTick(LivingEntity holder, ItemStack stack, Rarity rarity) {
        GearEffect.super.onTick(holder, stack, rarity);
    }

    @Override
    public void onHit(LivingEntity attacker, LivingEntity target, ItemStack weapon, LivingHurtEvent event) {
        if (!weapon.hasTag()) return;
        
        CompoundTag tag = weapon.getTag();
        if (tag == null || !tag.contains("LifeStealValue")) return;
        
        float lifestealPercent = tag.getFloat("LifeStealValue");
        float damage = event.getAmount();
        float healAmount = damage * lifestealPercent;
        
        if (healAmount > 0) {
            attacker.heal(healAmount);
        }
    }

    @Override
    public boolean isApplicableTo(ItemStack stack) {
        return EffectConfigHelper.isItemValidForEffect(stack, getId());
    }

    @Override
    public float getValueByRarity(ItemStack weapon) {
        if (!weapon.hasTag()) return 0.0f;
        
        CompoundTag tag = weapon.getTag();
        if (tag == null || !tag.contains("LifeStealValue")) return 0.0f;
        
        return tag.getFloat("LifeStealValue");
    }
}
