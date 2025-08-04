package net.Kyap.ItemsRarity.block.entity.custom;

import net.Kyap.ItemsRarity.block.entity.ModBlockEntities;
import net.Kyap.ItemsRarity.screen.EnhancedAnvilBlockMenu;
import net.Kyap.ItemsRarity.util.ModRarities;
import net.Kyap.ItemsRarity.util.rarity.data.RarityConfigHelper;
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
import static net.Kyap.ItemsRarity.util.EffectPoolSystem.rollEffectsOnItem;

public class EnhancedAnvilBlockEntity extends BlockEntity implements MenuProvider {
    protected final ContainerData data;
    private static final int RESOURCE_SLOT = 0;
    private static final int GEAR_SLOT = 1;
    private static final int MOD_MATERIAL_SLOT = 2;
    private int enhancementStatus = 0; // 0 = en attente, 1 = réussie, 2 = échouée
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final ItemStackHandler item_handler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            super.onContentsChanged(slot);
        }
        
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return super.extractItem(slot, amount, simulate);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return switch (slot) {
                case GEAR_SLOT -> isItemUpgradable(stack); // Seuls les items upgradables dans le slot gear
                case RESOURCE_SLOT -> {
                    // Slot de gauche : ressource de réparation ou plume si pas de ressource connue
                    ItemStack gearItem = getStackInSlot(GEAR_SLOT);
                    yield isValidRepairResource(gearItem, stack);
                }
                case MOD_MATERIAL_SLOT -> isCustomModResource(stack); // Slot de droite : uniquement nos ressources personnalisées
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

    // Vérifie si l'amélioration est possible
    public boolean canUpgrade() {
        ItemStack gearItem = item_handler.getStackInSlot(GEAR_SLOT);
        ItemStack resourceItem = item_handler.getStackInSlot(RESOURCE_SLOT);
        ItemStack modMaterialItem = item_handler.getStackInSlot(MOD_MATERIAL_SLOT);

        // Vérifier que tous les slots ont des items
        if (gearItem.isEmpty() || resourceItem.isEmpty() || modMaterialItem.isEmpty()) {
            return false;
        }

        // Vérifier que l'item gear peut être amélioré
        if (!isItemUpgradable(gearItem)) {
            return false;
        }

        // Vérifier que la ressource de gauche est correcte pour l'item du milieu
        if (!isValidRepairResource(gearItem, resourceItem)) {
            return false;
        }

        // Vérifier que la ressource de droite est une de nos ressources personnalisées
        return isCustomModResource(modMaterialItem);
    }

    // Effectue l'amélioration de l'item
    public boolean performUpgrade() {
        if (!canUpgrade()) {
            enhancementStatus = 2; // Échec
            setChanged();
            return false;
        }

        ItemStack gearItem = item_handler.getStackInSlot(GEAR_SLOT);
        ItemStack resourceItem = item_handler.getStackInSlot(RESOURCE_SLOT);
        ItemStack modMaterialItem = item_handler.getStackInSlot(MOD_MATERIAL_SLOT);

        // Vérifier si déjà mythic
        if (gearItem.hasTag() && "mythic".equals(gearItem.getOrCreateTag().getString("custom_rarity"))) {
            enhancementStatus = 2; // Échec
            resourceItem.shrink(1);
            modMaterialItem.shrink(1);
            setChanged();
            return false;
        }

        ModRarities.ModRarity newTierName = RarityConfigHelper.rollNewTier(modMaterialItem);;
        if (newTierName == null) {
            enhancementStatus = 2;
            resourceItem.shrink(1);
            modMaterialItem.shrink(1);
            setChanged();
            return false;
        }

        // Appliquer directement le tag NBT sur l’item
        CompoundTag tag = gearItem.getOrCreateTag();
        tag.putString("custom_rarity", newTierName.getId().toLowerCase());
        rollEffectsOnItem(gearItem);
        // Marquer comme réussi
        enhancementStatus = 1;

        // Consommer les ressources
        resourceItem.shrink(1);
        modMaterialItem.shrink(1);

        setChanged();

        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }

        return true;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", item_handler.serializeNBT());
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        item_handler.deserializeNBT(pTag.getCompound("inventory"));
    }
}