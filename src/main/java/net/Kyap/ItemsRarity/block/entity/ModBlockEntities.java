package net.Kyap.ItemsRarity.block.entity;

import net.Kyap.ItemsRarity.ItemsRarity;
import net.Kyap.ItemsRarity.block.ModBlocks;
import net.Kyap.ItemsRarity.block.entity.custom.EnhancedAnvilBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ItemsRarity.MOD_ID);

    public static final RegistryObject<BlockEntityType<EnhancedAnvilBlockEntity>> ENHANCED_ANVIL_BE =
            BLOCK_ENTITIES.register("enhanced_anvil_be", () ->
                    BlockEntityType.Builder.of(EnhancedAnvilBlockEntity::new,
                            ModBlocks.ENHANCED_ANVIL_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
