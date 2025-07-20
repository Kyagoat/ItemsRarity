package net.Kyap.ItemsRarity.item.custom;

import net.Kyap.ItemsRarity.util.ModRarities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

public class UncommonItem extends RarityItem {
    public UncommonItem(Item originalItem) {
        super(originalItem, ModRarities.ModRarity.UNCOMMON);
    }

    public @NotNull Rarity getRarity(@NotNull ItemStack itemStack) {
        return ModRarities.ModRarity.UNCOMMON.getMinecraftRarity();
    }
}
