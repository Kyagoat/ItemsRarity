package net.Kyap.ItemsRarity.events;

import com.mojang.logging.LogUtils;
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
import org.slf4j.Logger;

import static net.Kyap.ItemsRarity.util.UpgradeHelper.*;

@Mod.EventBusSubscriber(modid = ItemsRarity.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemDropEventHandler {
    
    private static final Logger LOGGER = LogUtils.getLogger();
    
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        // Appliquer une rareté aux items droppés par les mobs
        for (ItemEntity itemEntity : event.getDrops()) {
            ItemStack stack = itemEntity.getItem();
            
            // IMPORTANT : Seulement appliquer si l'item n'a pas déjà une rareté
            if (isItemUpgradable(stack) && !hasRarity(stack)) {
                UpgradeHelper.upgradeItem(stack, getRandomMobDropRarity());
                LOGGER.info("Applied rarity to dropped item: {}", stack.getItem().getDescriptionId());
            } else if (hasRarity(stack)) {
                LOGGER.info("Item already has rarity, skipping: {}", stack.getItem().getDescriptionId());
            }
        }
    }
    
    @SubscribeEvent
    public static void onItemEntitySpawn(EntityJoinLevelEvent event) {
        // Appliquer une rareté aux items qui apparaissent dans le monde
        // (ex: items trouvés dans les coffres, générés par des structures)
        if (event.getEntity() instanceof ItemEntity itemEntity) {
            ItemStack stack = itemEntity.getItem();
            if (isItemUpgradable(stack) && !hasRarity(stack)) {
                // Seulement appliquer si l'item n'a pas déjà une rareté
                UpgradeHelper.upgradeItem(stack, getRandomMobDropRarity());
            }
        }
    }

    /**
     * Applique une rareté aléatoire à un item basée sur les données de configuration
     */
    private static ModRarities.ModRarity getRandomMobDropRarity() {
        // Utiliser le système data-driven pour obtenir une rareté
        return RarityRatesDataManager.rollMobDropRarity();
    }
}
