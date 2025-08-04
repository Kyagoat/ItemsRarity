package net.Kyap.ItemsRarity.item.custom;

import net.Kyap.ItemsRarity.util.ModRarities;
import net.Kyap.ItemsRarity.util.rarity.data.RarityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BismuthItem extends Item {
    
    public BismuthItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack itemStack) {
        return ModRarities.ModRarity.LEGENDARY.getRarity();
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.itemsrarity.bismuth.desc1").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(Component.translatable("item.itemsrarity.bismuth.desc2").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(Component.translatable("item.itemsrarity.bismuth.desc3").withStyle(ChatFormatting.GRAY));
        
        RarityTooltipHelper.addRarityTooltips(pTooltipComponents, "bismuth");

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
