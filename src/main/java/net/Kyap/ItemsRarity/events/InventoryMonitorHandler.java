package net.Kyap.ItemsRarity.events;

import com.mojang.logging.LogUtils;
import net.Kyap.ItemsRarity.ItemsRarity;
import net.Kyap.ItemsRarity.util.UpgradeHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.*;

import static net.Kyap.ItemsRarity.util.UpgradeHelper.*;

@Mod.EventBusSubscriber(modid = ItemsRarity.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InventoryMonitorHandler {
    
    private static final Logger LOGGER = LogUtils.getLogger();
    
    private static final Map<UUID, Map<Item, Integer>> playerItemCounts = new HashMap<>();
    
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) {
            return;
        }
        
        Player player = event.player;
        UUID playerId = player.getUUID();
        
        Map<Item, Integer> currentItemCounts = new HashMap<>();
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && isItemUpgradable(stack) && !hasRarity(stack)) {
                Item item = stack.getItem();
                currentItemCounts.put(item, currentItemCounts.getOrDefault(item, 0) + stack.getCount());
            }
        }
        Map<Item, Integer> previousCounts = playerItemCounts.getOrDefault(playerId, new HashMap<>());
        for (Map.Entry<Item, Integer> entry : currentItemCounts.entrySet()) {
            Item item = entry.getKey();
            int currentCount = entry.getValue();
            int previousCount = previousCounts.getOrDefault(item, 0);

            if (currentCount > previousCount) {
                int newItemsCount = currentCount - previousCount;
                applyRarityToNewItems(player, item, newItemsCount);
            }
        }
        playerItemCounts.put(playerId, currentItemCounts);
    }
    
    /**
     * Applique la rareté aux nouveaux items dans l'inventaire du joueur
     */
    private static void applyRarityToNewItems(Player player, Item item, int newItemsCount) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            
            if (!stack.isEmpty() && stack.getItem() == item && !hasRarity(stack)) {
                UpgradeHelper.upgradeItem(stack, CraftingEventHandler.getRandomCraftingRarity());
                LOGGER.info("Applied rarity to stack of {} items at slot {}", stack.getCount(), i);
            }
        }
    }
}
