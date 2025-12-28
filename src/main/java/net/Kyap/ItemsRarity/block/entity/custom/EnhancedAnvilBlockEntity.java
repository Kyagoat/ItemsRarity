package net.Kyap.ItemsRarity.block.entity.custom;

import net.Kyap.ItemsRarity.block.entity.ModBlockEntities;
import net.Kyap.ItemsRarity.screen.EnhancedAnvilBlockMenu;
import net.Kyap.ItemsRarity.util.ModRarities;
import net.Kyap.ItemsRarity.util.UpgradeHelper;
import net.Kyap.ItemsRarity.util.rarity.data.RarityRatesDataManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static net.Kyap.ItemsRarity.util.UpgradeHelper.*;

public class EnhancedAnvilBlockEntity extends BlockEntity implements MenuProvider {
    protected final ContainerData data;
    private static final int RESOURCE_SLOT = 0;
    private static final int GEAR_SLOT = 1;
    private static final int MOD_MATERIAL_SLOT = 2;
    private int enhancementStatus = 0;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final ItemStackHandler item_handler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            super.onContentsChanged(slot);
        }
        
        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return super.extractItem(slot, amount, simulate);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case GEAR_SLOT -> isItemUpgradable(stack);
                case RESOURCE_SLOT -> {
                    ItemStack gearItem = getStackInSlot(GEAR_SLOT);
                    yield isValidRepairResource(gearItem, stack);
                }
                case MOD_MATERIAL_SLOT -> isCustomModResource(stack);
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    public EnhancedAnvilBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ENHANCED_ANVIL_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> enhancementStatus;
                    case 1 -> isItemUpgradable(item_handler.getStackInSlot(1)) ? 1 : 0;
                    case 2 -> hasValidResources(item_handler.getStackInSlot(0), item_handler.getStackInSlot(2)) ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                if (index == 0) {
                    enhancementStatus = value;
                }
            }

            @Override
            public int getCount() {
                return 3;
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

    public ItemStackHandler getItemHandler() {
        return item_handler;
    }

    @Override
    @NotNull
    public Component getDisplayName(){
        return Component.translatable("block.itemsrarity.enhanced_anvil_block");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pInventory, @NotNull Player pPlayer) {
        return new EnhancedAnvilBlockMenu(pContainerId, pInventory, this, this.data);
    }

    public boolean canUpgrade() {
        ItemStack gearItem = item_handler.getStackInSlot(GEAR_SLOT);
        ItemStack resourceItem = item_handler.getStackInSlot(RESOURCE_SLOT);
        ItemStack modMaterialItem = item_handler.getStackInSlot(MOD_MATERIAL_SLOT);

        if (gearItem.isEmpty() || resourceItem.isEmpty() || modMaterialItem.isEmpty()) {
            return false;
        }

        if (!isItemUpgradable(gearItem)) {
            return false;
        }

        if (!isValidRepairResource(gearItem, resourceItem)) {
            return false;
        }

        return isCustomModResource(modMaterialItem);
    }

    public boolean performUpgrade() {
        if (!canUpgrade()) {
            enhancementStatus = 2;
            setChanged();
            return false;
        }

        ItemStack gearItem = item_handler.getStackInSlot(GEAR_SLOT);
        ItemStack resourceItem = item_handler.getStackInSlot(RESOURCE_SLOT);
        ItemStack modMaterialItem = item_handler.getStackInSlot(MOD_MATERIAL_SLOT);

        String materialId = RarityRatesDataManager.getMaterialId(modMaterialItem);
        
        ModRarities.ModRarity rolled = RarityRatesDataManager.rollUpgradeRarity(materialId);

        boolean upgradeSuccessful = UpgradeHelper.upgradeItem(gearItem, rolled);
        
        if (upgradeSuccessful) {
            enhancementStatus = 1;
        } else {
            enhancementStatus = 2;
        }

        resourceItem.shrink(1);
        modMaterialItem.shrink(1);
        setChanged();

        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
        return upgradeSuccessful;
    }


    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", item_handler.serializeNBT());
        super.saveAdditional(pTag);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        item_handler.deserializeNBT(pTag.getCompound("inventory"));
    }
}