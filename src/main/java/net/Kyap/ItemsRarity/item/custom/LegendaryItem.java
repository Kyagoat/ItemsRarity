package net.Kyap.ItemsRarity.item.custom;

import net.Kyap.ItemsRarity.util.ModRarities;
import net.minecraft.world.item.Item;

public class LegendaryItem extends RarityItem {
    public LegendaryItem(Item originalItem) {
        super(originalItem, ModRarities.ModRarity.LEGENDARY);
    }
}
