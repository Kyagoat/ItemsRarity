package net.Kyap.ItemsRarity.item.custom;

import net.Kyap.ItemsRarity.util.ModRarities;
import net.minecraft.world.item.Item;

public class RareItem extends RarityItem {
    public RareItem(Item originalItem) {
        super(originalItem, ModRarities.ModRarity.RARE);
    }
}
