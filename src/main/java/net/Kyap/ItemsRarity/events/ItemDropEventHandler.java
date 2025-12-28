package net.Kyap.ItemsRarity.events;

import net.Kyap.ItemsRarity.ItemsRarity;
import net.Kyap.ItemsRarity.util.ModRarities;
import net.Kyap.ItemsRarity.util.UpgradeHelper;
import net.Kyap.ItemsRarity.util.rarity.data.RarityRatesDataManager;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.*;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.Kyap.ItemsRarity.util.UpgradeHelper.*;

@Mod.EventBusSubscriber(modid = ItemsRarity.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemDropEventHandler {
    
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        for (ItemEntity itemEntity : event.getDrops()) {
            ItemStack stack = itemEntity.getItem();
            if (isItemUpgradable(stack) && !hasRarity(stack)) {
                UpgradeHelper.upgradeItem(stack, getRandomMobDropRarity());
            }
        }
    }
    
    @SubscribeEvent
    public static void onItemEntitySpawn(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ItemEntity itemEntity) {
            ItemStack stack = itemEntity.getItem();
            if (isItemUpgradable(stack) && !hasRarity(stack)) {
                UpgradeHelper.upgradeItem(stack, getRandomMobDropRarity());
            }
        }
    }

    /**
     * Applique une rareté aléatoire à un item basée sur les données de configuration
     */
    private static ModRarities.ModRarity getRandomMobDropRarity() {
        return RarityRatesDataManager.rollMobDropRarity();
    }
}
