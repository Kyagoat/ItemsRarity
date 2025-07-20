package net.Kyap.ItemsRarity.screen;

import net.Kyap.ItemsRarity.ItemsRarity;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, ItemsRarity.MOD_ID);

    public static final RegistryObject<MenuType<EnhancedAnvilBlockMenu>> ENHANCED_ANVIL_MENU =
            MENUS.register("enhanced_anvil_menu", () ->
                    IForgeMenuType.create(EnhancedAnvilBlockMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
