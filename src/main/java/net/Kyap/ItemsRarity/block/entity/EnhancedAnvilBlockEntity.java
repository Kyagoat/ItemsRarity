package net.Kyap.ItemsRarity.block.custom;

import net.Kyap.ItemsRarity.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.Kyap.ItemsRarity.util.RarityManager.hasValidResources;
import static net.Kyap.ItemsRarity.util.RarityManager.isItemUpgradable;

public class EnhancedAnvilBlock extends BlockEntity {

    private final ItemStackHandler item_handler = new ItemStackHandler(3);

    protected final ContainerData data;
    private static final int RESOURCE_SLOT = 0;
    private static final int GEAR_SLOT = 1;
    private static final int MOD_MATERIAL_SLOT = 2;
    private int enhancementStatus = 0; // 0 = en attente, 1 = réussie, 2 = échouée
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public EnhancedAnvilBlock(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ENHANCED_ANVIL_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> enhancementStatus; // Statut de l'amélioration (0 = en attente, 1 = terminé, 2 = échouée)
                    case 1 -> isItemUpgradable(item_handler.getStackInSlot(1)) ? 1 : 0; // A un équipement valide dans GEAR_SLOT
                    case 2 -> hasValidResources(item_handler.getStackInSlot(0), item_handler.getStackInSlot(2)) ? 1 : 0; // A les ressources nécessaires (RESOURCE_SLOT + MOD_MATERIAL_SLOT)
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                if (index == 0) {
                    enhancementStatus = value; // Met à jour le statut de l'amélioration
                }
            }

            @Override
            public int getCount() {
                return 3; // Return the number of data entries
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(capability, side);
    }

    @Override
    public void onLoad(){
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> item_handler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    // Récupère l'ItemHandler pour l'accès externe (interfaces, etc.)
    public ItemStackHandler getItemHandler() {
        return item_handler;
    }

    public Component getDisplayName(){
        return Component.translatable("block.itemsrarity.enhanced_anvil_block");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull net.minecraft.world.entity.player.Inventory pInventory, @NotNull net.minecraft.world.entity.player.Player pPlayer) {
        return new EnhancedAnvilMenu(pContainerId, pInventory, this, this.data);
    }

    public static final VoxelShape SHAPE = makeShape();

    private static VoxelShape makeShape() {
        VoxelShape shape = Block.box(2, 0, 2, 14, 4, 14);
        shape = Shapes.or(shape, Block.box(4, 4, 3, 12, 5, 13));
        shape = Shapes.or(shape, Block.box(6, 5, 4, 10, 10, 12));
        shape = Shapes.or(shape, Block.box(3, 10, 0, 13, 16, 16));
        shape = Shapes.or(shape, Block.box(4, 16, 12, 7, 18, 15));
        shape = Shapes.or(shape, Block.box(4, 18, 12, 6, 19, 14));
        shape = Shapes.or(shape, Block.box(7, 4, 13, 10, 6, 15));
        shape = Shapes.or(shape, Block.box(7, 5, 12, 10, 6, 13));
        shape = Shapes.or(shape, Block.box(7, 6, 12, 9, 7, 14));
        shape = Shapes.or(shape, Block.box(10, 5, 5, 12, 7, 7));
        shape = Shapes.or(shape, Block.box(3, 0, 3, 4, 2, 4));
        shape = Shapes.or(shape, Block.box(3, 0, 12, 4, 2, 13));
        
        return shape;
    }
}
