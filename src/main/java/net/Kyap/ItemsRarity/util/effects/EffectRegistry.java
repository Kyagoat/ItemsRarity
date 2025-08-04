package net.Kyap.ItemsRarity.util.effects;

import net.Kyap.ItemsRarity.util.effects.data.EffectConfigHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.HashMap;
import java.util.Map;

public class EffectRegistry {
    
    private static final Map<String, GearEffect> REGISTERED_EFFECTS = new HashMap<>();
    
    /**
     * Enregistre un effet dans le système
     */
    public static void registerEffect(GearEffect effect) {
        REGISTERED_EFFECTS.put(effect.getId(), effect);
    }
    
    /**
     * Obtient un effet par son ID
     */
    public static GearEffect getEffect(String effectId) {
        return REGISTERED_EFFECTS.get(effectId);
    }
    
    /**
     * Obtient tous les effets enregistrés
     */
    public static Map<String, GearEffect> getAllEffects() {
        return REGISTERED_EFFECTS;
    }
    
    /**
     * Vérifie si un effet est enregistré
     */
    public static boolean isEffectRegistered(String effectId) {
        return REGISTERED_EFFECTS.containsKey(effectId);
    }
    
    /**
     * Applique un effet sur un item avec sa valeur basée sur la rareté
     */
    public static void applyEffectToItem(String effectId, ItemStack stack, CompoundTag tag) {
        GearEffect effect = getEffect(effectId);
        if (effect == null || !effect.isApplicableTo(stack)) return;
        
        Rarity rarity = stack.getRarity();
        float effectValue = EffectConfigHelper.getRandomEffectValue(effectId, rarity);
        
        if (effectValue > 0) {
            effect.applyEffect(stack, tag, effectValue);
        }
    }
    
    /**
     * Supprime complètement tous les effets personnalisés et leurs valeurs d'un item
     */
    private static void removeAllEffect(ItemStack stack){
        if (!stack.hasTag()) return;

        CompoundTag tag = stack.getTag();
        if (tag == null) return;

        // Supprimer la liste des effets
        if (tag.contains("CustomEffects")) {
            // Avant de supprimer la liste, nettoyer les valeurs individuelles des effets
            ListTag effectsList = tag.getList("CustomEffects", 8);
            for (int i = 0; i < effectsList.size(); i++) {
                String effectId = effectsList.getString(i);
                GearEffect effect = getEffect(effectId);
                if (effect != null) {
                    // Supprimer les valeurs spécifiques de chaque effet
                    effect.removeEffect(stack, tag);
                }
            }
            
            // Supprimer la liste des effets
            tag.remove("CustomEffects");
        }
        
        stack.setTag(tag);
    }

    /**
     * Supprime complètement tous les effets personnalisés et leurs valeurs d'un item (méthode publique)
     */
    public static void removeAllEffects(ItemStack stack){
        removeAllEffect(stack);
    }
    
    /**
     * Gère les événements onHit pour tous les effets d'un item
     */
    public static void handleOnHitEffects(LivingEntity attacker, LivingEntity target, ItemStack weapon, LivingHurtEvent event) {
        if (!weapon.hasTag()) return;
        
        CompoundTag tag = weapon.getTag();
        if (tag == null || !tag.contains("CustomEffects")) return;
        
        // Parcourir tous les effets sur l'item
        for (String effectId : REGISTERED_EFFECTS.keySet()) {
            GearEffect effect = getEffect(effectId);
            if (effect != null && hasEffectApplied(weapon, effectId)) {
                String effectType = EffectConfigHelper.getEffectType(effectId);
                if ("on_hit".equals(effectType)) {
                    effect.onHit(attacker, target, weapon, event);
                }
            }
        }
    }
    
    /**
     * Gère les événements onTick pour tous les effets d'un item
     */
    public static void handleOnTickEffects(LivingEntity holder, ItemStack stack) {
        if (!stack.hasTag()) return;
        
        for (String effectId : REGISTERED_EFFECTS.keySet()) {
            GearEffect effect = getEffect(effectId);
            if (effect != null && hasEffectApplied(stack, effectId)) {
                String effectType = EffectConfigHelper.getEffectType(effectId);
                if ("on_tick".equals(effectType) || "passive".equals(effectType)) {
                    effect.onTick(holder, stack, stack.getRarity());
                }
            }
        }
    }
    
    /**
     * Vérifie si un effet spécifique est appliqué sur un item
     */
    private static boolean hasEffectApplied(ItemStack stack, String effectId) {
        if (!stack.hasTag()) return false;
        
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("CustomEffects")) return false;
        
        return tag.getList("CustomEffects", 8).toString().contains(effectId);
    }
    
    /**
     * Initialise tous les effets par défaut
     */
    public static void initializeEffects() {
        // Les effets seront enregistrés ici
        registerEffect(new LifeStealEffect());
        registerEffect(new CritChanceEffect());
    }
}
