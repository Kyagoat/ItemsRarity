package net.Kyap.ItemsRarity.item;

import net.Kyap.ItemsRarity.ItemsRarity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraft.world.item.Items;

import java.util.List;

public class ModToolTiers {

    public static final Tier EMERALD = TierSortingRegistry.registerTier(
            new ForgeTier(
                    2,
                    32,
                    6.0F,
                    6.0F,
                    15,
                    BlockTags.NEEDS_IRON_TOOL,
                    () -> Ingredient.of(Items.EMERALD)
            ),
            new ResourceLocation(ItemsRarity.MOD_ID, "emerald"),
            List.of(Tiers.GOLD),
            List.of(Tiers.DIAMOND)
    );
}
