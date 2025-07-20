package net.Kyap.ItemsRarity.item.custom;

import net.Kyap.ItemsRarity.util.ModRarities;
import net.minecraft.world.item.Item;

public class MythicItem extends RarityItem {
    public MythicItem(Item originalItem) {
        super(originalItem, ModRarities.ModRarity.MYTHIC);
    }
}
