package net.Kyap.ItemsRarity.item;

import net.Kyap.ItemsRarity.ItemsRarity;
import net.Kyap.ItemsRarity.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.ItemStack;

public class ItemsRarityCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ItemsRarity.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ITEMS_RARITY_TAB = CREATIVE_MODE_TABS.register("items_rarity_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.BISMUTH.get()))
            .title(Component.translatable("creativemodetab.items_rarity_tab"))
            .displayItems((parameters, output) -> {
                output.accept(ModItems.FLOURITE.get());
                output.accept(ModItems.SULFUR.get());
                output.accept(ModItems.BISMUTH.get());
                output.accept(ModBlocks.ENHANCED_ANVIL_BLOCK.get());
                output.accept(ModItems.EMERALD_HAMMER.get());
            })
            .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
