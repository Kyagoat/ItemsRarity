package net.Kyap.ItemsRarity.item.custom;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.Kyap.ItemsRarity.item.ModToolTiers;
import net.Kyap.ItemsRarity.util.ModRarities;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;

public class EmeraldHammer extends TieredItem {

    private static final float ATTACK_DAMAGE = 10.0F;
    private static final float ATTACK_SPEED = -3.4F;

    public EmeraldHammer() {
        super(ModToolTiers.EMERALD, new Item.Properties()
                .stacksTo(1)
                .durability(ModToolTiers.EMERALD.getUses())
                .rarity(ModRarities.ModRarity.EPIC.getRarity())
                .fireResistant());
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();

        if (slot == EquipmentSlot.MAINHAND) {
            modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", ATTACK_DAMAGE, AttributeModifier.Operation.ADDITION));
            modifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", ATTACK_SPEED, AttributeModifier.Operation.ADDITION));
        }

        return modifiers;
    }
}
