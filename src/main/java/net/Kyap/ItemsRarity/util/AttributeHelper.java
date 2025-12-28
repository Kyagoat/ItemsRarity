package net.Kyap.ItemsRarity.util;

import dev.shadowsoffire.attributeslib.api.ALObjects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;

import java.util.UUID;

public class AttributeHelper {

    public static EquipmentSlot getSlotForItem(ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem armorItem) {
            return armorItem.getType().getSlot();
        }
        if (stack.getItem() instanceof ShieldItem) {
            return EquipmentSlot.OFFHAND;
        }
        return EquipmentSlot.MAINHAND;
    }

    public static void applyAttribute(ItemStack stack, Attribute attribute, String name, float value, AttributeModifier.Operation operation) {
        EquipmentSlot slot = getSlotForItem(stack);

        if (!stack.getOrCreateTag().contains("AttributeModifiers", 9)) {
            for (EquipmentSlot s : EquipmentSlot.values()) {
                stack.getItem().getDefaultAttributeModifiers(s).forEach((attr, mod) -> {
                    stack.addAttributeModifier(attr, mod, s);
                });
            }
        }

        UUID uuid = UUID.randomUUID();
        AttributeModifier modifier = new AttributeModifier(uuid, name, value, operation);
        stack.addAttributeModifier(attribute, modifier, slot);
    }

    public static void applyEffectIdAsAttribute(String effectId, ItemStack stack, float value) {
        switch (effectId) {
            case "crit_chance":
                applyAttribute(stack, ALObjects.Attributes.CRIT_CHANCE.get(), "Rarity Crit Bonus", value, AttributeModifier.Operation.ADDITION);
                break;

            case "damage":
                applyAttribute(stack, Attributes.ATTACK_DAMAGE, "Rarity Damage Bonus", value, AttributeModifier.Operation.ADDITION);
                break;

            case "health":
                applyAttribute(stack, Attributes.MAX_HEALTH, "Rarity HP Bonus", value, AttributeModifier.Operation.ADDITION);
                break;

            case "crit_damage":
                applyAttribute(stack, ALObjects.Attributes.CRIT_DAMAGE.get(), "Rarity Crit Dmg Bonus", value, AttributeModifier.Operation.ADDITION);
                break;

            case "armor_penetration":
                applyAttribute(stack, ALObjects.Attributes.ARMOR_PIERCE.get(), "Rarity Pierce Bonus", value, AttributeModifier.Operation.ADDITION);
                break;

            case "speed":
                applyAttribute(stack, Attributes.MOVEMENT_SPEED, "Rarity Speed Bonus", value, AttributeModifier.Operation.MULTIPLY_TOTAL);
                break;

            case "life_steal":
                applyAttribute(stack, ALObjects.Attributes.LIFE_STEAL.get(), "Rarity Life Steal", value, AttributeModifier.Operation.ADDITION);
                break;

            case "dodge":
                applyAttribute(stack, ALObjects.Attributes.DODGE_CHANCE.get(), "Rarity Dodge", value, AttributeModifier.Operation.ADDITION);
                break;

            case "armor_pierce":
                applyAttribute(stack, ALObjects.Attributes.ARMOR_PIERCE.get(), "Rarity Armor Pierce", value, AttributeModifier.Operation.ADDITION);
                break;

            case "arrow_damage":
                applyAttribute(stack, ALObjects.Attributes.ARROW_DAMAGE.get(), "Rarity Arrow Dmg", value, AttributeModifier.Operation.MULTIPLY_TOTAL);
                break;

            case "overheal":
                applyAttribute(stack, ALObjects.Attributes.OVERHEAL.get(), "Rarity Overheal", value, AttributeModifier.Operation.ADDITION);
                break;

            default:
                break;
        }
    }
}