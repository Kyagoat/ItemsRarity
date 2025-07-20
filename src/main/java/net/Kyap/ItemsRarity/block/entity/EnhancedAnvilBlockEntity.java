package net.Kyap.ItemsRarity.block.entity;

import net.Kyap.ItemsRarity.screen.EnhancedAnvilBlockMenu;
import net.Kyap.ItemsRarity.util.ModRarities;
import net.Kyap.ItemsRarity.item.ItemTransformationManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
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

import static net.Kyap.ItemsRarity.util.RarityManager.*;

public class EnhancedAnvilBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler item_handler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();

            // Effet de particules quand on place un item (côté serveur seulement)
            if (level != null && !level.isClientSide()) {
                spawnSlotChangeParticles(slot);
            }

            super.onContentsChanged(slot);
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

    protected final ContainerData data;
    private static final int RESOURCE_SLOT = 0;
    private static final int GEAR_SLOT = 1;
    private static final int MOD_MATERIAL_SLOT = 2;
    private int enhancementStatus = 0; // 0 = en attente, 1 = réussie, 2 = échouée
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

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
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull net.minecraft.world.entity.player.Inventory pInventory, @NotNull net.minecraft.world.entity.player.Player pPlayer) {
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
        if (!isCustomModResource(modMaterialItem)) {
            return false;
        }

        return true;
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

        // Obtenir la rareté actuelle de l'item
        ModRarities.ModRarity currentRarity = net.Kyap.ItemsRarity.item.ItemTransformationManager.getCurrentRarity(gearItem);
        if (currentRarity == null) {
            currentRarity = ModRarities.ModRarity.COMMON; // Rareté par défaut
        }

        // Vérifier si l'item est déjà MYTHIC (rareté maximale)
        if (currentRarity == ModRarities.ModRarity.MYTHIC) {
            // L'item est déjà à la rareté maximale, impossible d'améliorer
            enhancementStatus = 2; // Échec

            // Consommer quand même les ressources (risque du craft)
            resourceItem.shrink(1);
            modMaterialItem.shrink(1);

            setChanged();
            return false;
        }

        // Faire un roll pour déterminer le nouveau tier selon la ressource
        String newTierName = rollNewTier(currentRarity, modMaterialItem);
        if (newTierName == null) {
            // Échec du roll - aucune amélioration
            enhancementStatus = 2; // Échec

            // Consommer quand même les ressources (risque du craft)
            resourceItem.shrink(1);
            modMaterialItem.shrink(1);

            setChanged();
            return false;
        }

        // Créer l'item amélioré avec le nouveau système de tiers
        ItemStack upgradedItem = net.Kyap.ItemsRarity.item.ItemTransformationManager.transformItemWithTier(gearItem, newTierName);

        // Remplacer l'item dans le slot
        item_handler.setStackInSlot(GEAR_SLOT, upgradedItem);

        // Consommer les ressources (enlever 1 item de chaque slot)
        resourceItem.shrink(1);
        modMaterialItem.shrink(1);

        // Marquer comme réussi
        enhancementStatus = 1;

        // Marquer le chunk comme modifié pour sauvegarder les changements
        setChanged();

        // Synchroniser avec le client si on est côté serveur
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }

        return true;
    }

    /**
     * Fait un roll pour déterminer le nouveau tier selon la ressource utilisée
     * Plus le tier est élevé, plus c'est rare à obtenir
     */
    private String rollNewTier(ModRarities.ModRarity currentRarity, ItemStack resourceStack) {
        if (level == null || resourceStack.isEmpty()) return null;

        String itemId = resourceStack.getItem().getDescriptionId();

        // Définir les probabilités cumulatives selon la ressource
        double roll = level.getRandom().nextDouble();

        if (itemId.contains("flourite")) {
            // Flourite : UNCOMMON (70%), RARE (25%), EPIC (5%)
            if (roll < 0.70) {
                return "uncommon";
            } else if (roll < 0.95) {
                return "rare";
            } else {
                return "epic";
            }
        } else if (itemId.contains("sulfur")) {
            // Sulfur : UNCOMMON (50%), RARE (30%), EPIC (15%), LEGENDARY (5%)
            if (roll < 0.50) {
                return "uncommon";
            } else if (roll < 0.80) {
                return "rare";
            } else if (roll < 0.95) {
                return "epic";
            } else {
                return "legendary";
            }
        } else if (itemId.contains("bismuth")) {
            // Bismuth : RARE (40%), EPIC (35%), LEGENDARY (20%), MYTHIC (5%)
            if (roll < 0.40) {
                return "rare";
            } else if (roll < 0.75) {
                return "epic";
            } else if (roll < 0.95) {
                return "legendary";
            } else {
                return "mythic";
            }
        }

        return null; // Ressource inconnue
    }

    // Obtient la rareté ModRarity actuelle d'un item
    private ModRarities.ModRarity getCurrentModRarity(ItemStack stack) {
        // Essayer d'abord de récupérer la rareté personnalisée
        ModRarities.ModRarity customRarity = net.Kyap.ItemsRarity.item.ItemTransformationManager.getCurrentRarity(stack);
        if (customRarity != null) {
            return customRarity;
        }

        // Si pas de rareté personnalisée, convertir depuis la rareté Minecraft
        for (ModRarities.ModRarity modRarity : ModRarities.ModRarity.values()) {
            if (modRarity.getMinecraftRarity().equals(stack.getRarity())) {
                return modRarity;
            }
        }
        return null;
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

    /**
     * Crée de petites particules quand un item est placé dans un slot
     */
    private void spawnSlotChangeParticles(int slot) {
        if (level == null || level.isClientSide()) {
            return;
        }

        // Position légèrement différente selon le slot
        double offsetX = switch (slot) {
            case 0 -> -0.3; // Slot de gauche
            case 1 -> 0.0;  // Slot du milieu
            case 2 -> 0.3;  // Slot de droite
            default -> 0.0;
        };

        double particleX = worldPosition.getX() + 0.5 + offsetX;
        double particleY = worldPosition.getY() + 1.2;
        double particleZ = worldPosition.getZ() + 0.5;

        // Créer quelques particules d'enchantement
        if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    net.minecraft.core.particles.ParticleTypes.ENCHANT,
                    particleX, particleY, particleZ,
                    5, // count
                    0.2, 0.1, 0.2, // spread
                    0.1 // speed
            );

            // Son léger de placement
            level.playSound(null, worldPosition,
                    net.minecraft.sounds.SoundEvents.ITEM_PICKUP,
                    net.minecraft.sounds.SoundSource.BLOCKS,
                    0.3f, 1.0f + (slot * 0.1f));
        }
    }
}