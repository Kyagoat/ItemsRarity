package net.Kyap.ItemsRarity.item.custom;

import net.Kyap.ItemsRarity.util.ModRarities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

public class FlouriteItem extends Item {

    public FlouriteItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack itemStack) {
        return ModRarities.ModRarity.RARE.getRarity();
    }
}
