package net.Kyap.ItemsRarity.util.effects;

import net.Kyap.ItemsRarity.util.effects.data.EffectConfigHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.Locale;

public class DamageEffect implements GearEffect {

    @Override
    public String getId() {
        return "damage";
    }

    @Override
    public boolean isApplicableTo(ItemStack stack) {
        return EffectConfigHelper.isItemValidForEffect(stack, getId());
    }

    @Override
    public void applyEffect(ItemStack stack, CompoundTag tag, float effectValue) {
        // Stocker la valeur de l'effet dans le tag NBT
        tag.putFloat("DamageEffect", effectValue);

        // Ajouter l'ID de l'effet à la liste des effets
        ListTag effectsList = tag.getList("CustomEffects", 8);
        effectsList.add(StringTag.valueOf(getId()));
        tag.put("CustomEffects", effectsList);
    }

    @Override
    public void removeEffect(ItemStack weapon, CompoundTag tag) {
        // Supprimer la valeur spécifique de l'effet
        if (tag.contains("DamageEffect")) {
            tag.remove("DamageEffect");
        }
    }

    @Override
    public void onHit(LivingEntity attacker, LivingEntity target, ItemStack weapon, LivingHurtEvent event) {
        // Modifier les dégâts lors de l'attaque
        float damageModifier = getValueByRarity(weapon);
        if (damageModifier != 0) {
            float currentDamage = event.getAmount();
            float newDamage = currentDamage * (1.0f + damageModifier);
            event.setAmount(newDamage);
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
        if (tag == null || !tag.contains("DamageEffect")) return 0.0f;

        return tag.getFloat("DamageEffect");
    }

    @Override
    public String getTooltip(float effectValue) {
        float percent = effectValue * 100f;
        String sign = percent >= 0 ? "+" : "";

        // Format spécial pour les dégâts : "(+X%)"
        return String.format(Locale.ROOT, "(%s%.1f%%)", sign, percent);
    }
}
