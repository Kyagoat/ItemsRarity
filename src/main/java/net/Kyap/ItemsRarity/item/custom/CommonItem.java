package net.Kyap.ItemsRarity.item.custom;

import net.Kyap.ItemsRarity.util.ModRarities;
import net.minecraft.world.item.Item;

public class CommonItem extends RarityItem {
    public CommonItem(Item originalItem) {
        super(originalItem, ModRarities.ModRarity.COMMON);
    }
}
