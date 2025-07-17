package net.Kyap.ItemsRarity;

import com.mojang.logging.LogUtils;
import net.Kyap.ItemsRarity.block.ModBlocks;
import net.Kyap.ItemsRarity.item.ItemsRarityCreativeTabs;
import net.Kyap.ItemsRarity.item.ModItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ItemsRarity.MOD_ID)
public class ItemsRarity {
    public static final String MOD_ID = "itemsrarity";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ItemsRarity(FMLJavaModLoadingContext context) {
        
        IEventBus modEventBus = context.getModEventBus();

        ItemsRarityCreativeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("Items Rarity mod setup complete!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
