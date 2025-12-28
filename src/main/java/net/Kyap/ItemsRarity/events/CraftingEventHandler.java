package net.Kyap.ItemsRarity.events;

import net.Kyap.ItemsRarity.ItemsRarity;
import net.Kyap.ItemsRarity.util.ModRarities;
import net.Kyap.ItemsRarity.util.UpgradeHelper;
import net.Kyap.ItemsRarity.util.rarity.data.RarityRatesDataManager;
import net.minecraft.world.item.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.Kyap.ItemsRarity.util.UpgradeHelper.*;

@Mod.EventBusSubscriber(modid = ItemsRarity.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CraftingEventHandler {
    

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack craftedItem = event.getCrafting();
        
        if (isItemUpgradable(craftedItem) && !hasRarity(craftedItem)) {
            handleMassCrafting(event, craftedItem);
        }
    }

    private static void handleMassCrafting(PlayerEvent.ItemCraftedEvent event, ItemStack craftedItem) {
        int stackSize = craftedItem.getCount();
        
        if (stackSize == 1) {
            UpgradeHelper.upgradeItem(craftedItem, getRandomCraftingRarity());
        } else {
            craftedItem.setCount(0);
            for (int i = 0; i < stackSize; i++) {
                ItemStack individualItem = new ItemStack(craftedItem.getItem(), 1);
                UpgradeHelper.upgradeItem(individualItem, getRandomCraftingRarity());
                if (!event.getEntity().getInventory().add(individualItem)) {
                    event.getEntity().drop(individualItem, false);
                }
            }
        }
    }

    /**
     * Applique une rareté aléatoire à un item basée sur les données de configuration
     */
    protected static ModRarities.ModRarity getRandomCraftingRarity() {
        return RarityRatesDataManager.rollCraftingRarity();
    }
}
