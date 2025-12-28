package net.Kyap.ItemsRarity;

import com.mojang.logging.LogUtils;
import net.Kyap.ItemsRarity.block.ModBlocks;
import net.Kyap.ItemsRarity.block.entity.ModBlockEntities;
import net.Kyap.ItemsRarity.block.entity.renderer.EnhancedBlockEntityRenderer;
import net.Kyap.ItemsRarity.item.ItemsRarityCreativeTabs;
import net.Kyap.ItemsRarity.item.ModItems;
import net.Kyap.ItemsRarity.screen.EnhancedAnvilBlockScreen;
import net.Kyap.ItemsRarity.screen.ModMenuTypes;
import net.Kyap.ItemsRarity.util.effects.EffectRegistry;
import net.Kyap.ItemsRarity.util.rarity.data.RarityDataManager;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

@Mod(ItemsRarity.MOD_ID)
public class ItemsRarity {
    public static final String MOD_ID = "itemsrarity";
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final RarityDataManager RARITY_DATA_MANAGER = new RarityDataManager();

    public ItemsRarity(FMLJavaModLoadingContext context) {

        IEventBus modEventBus = context.getModEventBus();
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.itemsrarity.json");
        ItemsRarityCreativeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("Items Rarity mod setup complete!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        EffectRegistry.initializeEffects();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void addReloadListeners(AddReloadListenerEvent event) {
            event.addListener(RARITY_DATA_MANAGER);
            LOGGER.info("RarityDataManager registered as reload listener");
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.ENHANCED_ANVIL_MENU.get(), EnhancedAnvilBlockScreen::new);
            BlockEntityRenderers.register(ModBlockEntities.ENHANCED_ANVIL_BE.get(),
                    EnhancedBlockEntityRenderer::new);
        }
    }
}