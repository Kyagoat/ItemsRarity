package net.Kyap.ItemsRarity.item.custom;

import net.Kyap.ItemsRarity.util.ModRarities;
import net.minecraft.world.item.Item;

public class EpicItem extends RarityItem {
    public EpicItem(Item originalItem) {
        super(originalItem, ModRarities.ModRarity.EPIC);
    }
}
