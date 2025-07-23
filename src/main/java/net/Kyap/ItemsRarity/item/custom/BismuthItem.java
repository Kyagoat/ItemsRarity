package net.Kyap.ItemsRarity.item.custom;

import net.Kyap.ItemsRarity.util.ModRarities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

public class BismuthItem extends Item {
    
    public BismuthItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack itemStack) {
        return ModRarities.ModRarity.LEGENDARY.getRarity();
    }
}
