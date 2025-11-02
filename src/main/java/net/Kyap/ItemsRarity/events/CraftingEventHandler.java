package net.Kyap.ItemsRarity.events;

import com.mojang.logging.LogUtils;
import net.Kyap.ItemsRarity.ItemsRarity;
import net.Kyap.ItemsRarity.util.ModRarities;
import net.Kyap.ItemsRarity.util.UpgradeHelper;
import net.Kyap.ItemsRarity.util.rarity.data.RarityRatesDataManager;
import net.minecraft.world.item.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import static net.Kyap.ItemsRarity.util.UpgradeHelper.*;

@Mod.EventBusSubscriber(modid = ItemsRarity.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CraftingEventHandler {
    
    private static final Logger LOGGER = LogUtils.getLogger();
    
    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack craftedItem = event.getCrafting();
        
        // Vérifier si l'item peut avoir une rareté ET n'en a pas déjà une
        if (isItemUpgradable(craftedItem) && !hasRarity(craftedItem)) {
            // Pour le craft en masse, on doit traiter chaque item individuellement
            handleMassCrafting(event, craftedItem);
        } else if (hasRarity(craftedItem)) {
            LOGGER.info("Crafted item already has rarity, skipping: {}", craftedItem.getItem().getDescriptionId());
        }
    }

    private static void handleMassCrafting(PlayerEvent.ItemCraftedEvent event, ItemStack craftedItem) {
        int stackSize = craftedItem.getCount();
        
        if (stackSize == 1) {
            // Cas simple : un seul item
            UpgradeHelper.upgradeItem(craftedItem, getRandomCraftingRarity());
        } else {
            // Vider le stack original
            craftedItem.setCount(0);
            
            // Créer des items individuels avec des raretés différentes
            for (int i = 0; i < stackSize; i++) {
                ItemStack individualItem = new ItemStack(craftedItem.getItem(), 1);
                UpgradeHelper.upgradeItem(individualItem, getRandomCraftingRarity());
                // Ajouter l'item à l'inventaire du joueur
                if (!event.getEntity().getInventory().add(individualItem)) {
                    // Si l'inventaire est plein, dropper l'item
                    event.getEntity().drop(individualItem, false);
                }
            }
        }
    }

    /**
     * Applique une rareté aléatoire à un item basée sur les données de configuration
     */
    protected static ModRarities.ModRarity getRandomCraftingRarity() {
        // Utiliser le système data-driven pour obtenir une rareté
        return RarityRatesDataManager.rollCraftingRarity();
    }
}
