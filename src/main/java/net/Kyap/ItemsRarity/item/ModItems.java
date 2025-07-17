package net.Kyap.ItemsRarity.item;

import net.Kyap.ItemsRarity.ItemsRarity;
import net.Kyap.ItemsRarity.item.custom.BismuthItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ItemsRarity.MOD_ID);

    public static final RegistryObject<Item> SULFUR =
            ITEMS.register("sulfur", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BISMUTH =
            ITEMS.register("bismuth", () -> new BismuthItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
