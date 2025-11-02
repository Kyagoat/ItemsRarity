package net.Kyap.ItemsRarity.util.effects;

import net.Kyap.ItemsRarity.util.effects.data.EffectConfigHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.UUID;


public class SpeedEffect implements GearEffect {
    
    // UUIDs uniques pour chaque slot d'armure pour permettre le stacking
    private static final UUID SPEED_BOOTS_UUID = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
    private static final UUID SPEED_LEGGINGS_UUID = UUID.fromString("b2c3d4e5-f607-8901-bcde-f12345678901");
    private static final UUID SPEED_CHESTPLATE_UUID = UUID.fromString("c3d4e5f6-a7b8-9012-cdef-123456789012");
    private static final UUID SPEED_HELMET_UUID = UUID.fromString("d4e5f6a7-b8c9-0123-def1-234567890123");

    private static final String SPEED_MODIFIER_NAME = "Speed Effect";

    /**
     * Obtient l'UUID approprié selon le type d'armure pour permettre le stacking
     */
    private static UUID getUUIDForItem(ItemStack stack) {
        String itemName = stack.getItem().toString().toLowerCase();
        if (itemName.contains("boots")) {
            return SPEED_BOOTS_UUID;
        } else if (itemName.contains("leggings")) {
            return SPEED_LEGGINGS_UUID;
        } else if (itemName.contains("chestplate")) {
            return SPEED_CHESTPLATE_UUID;
        } else if (itemName.contains("helmet")) {
            return SPEED_HELMET_UUID;
        }
        return SPEED_BOOTS_UUID; // Fallback
    }

    @Override
    public String getId() {
        return "speed";
    }

    @Override
    public void applyEffect(ItemStack stack, CompoundTag tag, float effectValue) {
        // Stocker la valeur de l'effet dans le tag NBT
        tag.putFloat("SpeedEffect", effectValue);
        
        // Ajouter l'ID de l'effet à la liste des effets
        ListTag effectsList = tag.getList("CustomEffects", 8);
        effectsList.add(StringTag.valueOf(getId()));
        tag.put("CustomEffects", effectsList);
    }

    @Override
    public void removeEffect(ItemStack weapon, CompoundTag tag) {
        // Supprimer la valeur spécifique de l'effet
        if (tag.contains("SpeedEffect")) {
            tag.remove("SpeedEffect");
        }
    }
    
    // Méthode pour nettoyer l'attribut d'un joueur
    public static void cleanupMomentumEffect(LivingEntity entity) {
        AttributeInstance movementSpeed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed != null) {
            // Nettoyer tous les modificateurs Speed possibles
            movementSpeed.removeModifier(SPEED_BOOTS_UUID);
            movementSpeed.removeModifier(SPEED_LEGGINGS_UUID);
            movementSpeed.removeModifier(SPEED_CHESTPLATE_UUID);
            movementSpeed.removeModifier(SPEED_HELMET_UUID);
        }
    }

    @Override
    public void onTick(LivingEntity holder, ItemStack stack, Rarity rarity) {
        float value = getValueByRarity(stack);
        UUID itemUUID = getUUIDForItem(stack);
        
        AttributeInstance movementSpeed = holder.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed != null) {
            // Supprimer l'ancien modificateur pour cet item spécifique
            movementSpeed.removeModifier(itemUUID);
            
            // Ajouter le nouveau modificateur avec l'UUID spécifique à cet item
            if (value != 0) {
                AttributeModifier modifier = new AttributeModifier(
                    itemUUID,
                    SPEED_MODIFIER_NAME + " (" + stack.getItem().toString() + ")",
                    value, // Valeur exacte (ex: -0.15 pour -15%)
                    AttributeModifier.Operation.MULTIPLY_TOTAL // Utiliser MULTIPLY_TOTAL pour un effet plus visible
                );
                movementSpeed.addPermanentModifier(modifier);
            }
        }
    }

    @Override
    public boolean isApplicableTo(ItemStack stack) {
        boolean result = EffectConfigHelper.isItemValidForEffect(stack, getId());
        System.out.println("DEBUG Momentum isApplicableTo: " + stack.getItem().toString() + " = " + result);
        return result;
    }

    @Override
    public float getValueByRarity(ItemStack weapon) {
        if (!weapon.hasTag()) return 0.0f;
        
        CompoundTag tag = weapon.getTag();
        if (tag == null || !tag.contains("SpeedEffect")) return 0.0f;
        
        return tag.getFloat("SpeedEffect");
    }
}
