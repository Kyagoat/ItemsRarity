package net.Kyap.ItemsRarity.block.custom;

import net.Kyap.ItemsRarity.block.entity.EnhancedAnvilBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class EnhancedAnvilBlock extends BaseEntityBlock {

    public static final VoxelShape SHAPE = makeShape();

    public EnhancedAnvilBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

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

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EnhancedAnvilBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof EnhancedAnvilBlockEntity enhancedAnvil) {
                
                // Vérifier si le joueur tient une plume
                ItemStack heldItem = pPlayer.getItemInHand(pHand);
                boolean hasFeather = heldItem.getItem() == Items.FEATHER;
                if (hasFeather) {
                    // Clic droit avec une plume = Améliorer l'item
                    boolean canUpgrade = enhancedAnvil.canUpgrade();
                    if (canUpgrade) {
                        boolean success = enhancedAnvil.performUpgrade();
                        if (success) {
                            // Créer des particules selon la rareté de l'item amélioré
                            spawnRarityParticles(pLevel, pPos, enhancedAnvil);
                            
                            // Jouer des sons de forge réalistes
                            pLevel.playSound(null, pPos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.2f, 1.0f); // Son principal de marteau
                            pLevel.playSound(null, pPos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.8f, 1.3f); // Impact métallique
                            pLevel.playSound(null, pPos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 0.6f, 1.5f); // Son de succès
                            
                            return InteractionResult.SUCCESS;
                        } else {
                            // Son d'échec
                            pLevel.playSound(null, pPos, SoundEvents.ANVIL_DESTROY, SoundSource.BLOCKS, 0.5f, 0.8f);
                            return InteractionResult.FAIL;
                        }
                    } else {
                        return InteractionResult.FAIL;
                    }
                } else {
                    NetworkHooks.openScreen((ServerPlayer) pPlayer, enhancedAnvil, pPos);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof EnhancedAnvilBlockEntity enhancedAnvil) {
                // Droper les items quand le block est détruit
                for (int i = 0; i < enhancedAnvil.getItemHandler().getSlots(); i++) {
                    net.minecraft.world.Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(),
                            enhancedAnvil.getItemHandler().getStackInSlot(i));
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    /**
     * Crée des particules d'éclats métalliques autour de l'enclume comme si l'item était frappé
     */
    private void spawnRarityParticles(Level level, BlockPos pos, EnhancedAnvilBlockEntity anvil) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        // Récupérer l'item du slot gear (milieu) pour déterminer sa rareté
        ItemStack gearItem = anvil.getItemHandler().getStackInSlot(1);
        if (gearItem.isEmpty()) {
            return;
        }

        // Déterminer la couleur des particules selon la rareté
        Vec3 particleColor = getParticleColorForRarity(gearItem);
        
        // Déterminer le nombre de particules selon la rareté
        int particleMultiplier = getParticleMultiplierForRarity(gearItem);
        
        // Position de l'enclume (surface supérieure où l'item est frappé)
        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 1.0; // Surface de l'enclume
        double centerZ = pos.getZ() + 0.5;

        RandomSource random = level.getRandom();

        // Créer des éclats de métal qui volent dans toutes les directions
        // Plus la rareté est élevée, plus il y a de particules
        for (int i = 0; i < 15 * particleMultiplier; i++) {
            // Direction aléatoire pour les éclats
            double velocityX = (random.nextDouble() - 0.5) * 0.4;
            double velocityY = random.nextDouble() * 0.3 + 0.1; // Toujours vers le haut
            double velocityZ = (random.nextDouble() - 0.5) * 0.4;
            
            // Position de départ légèrement aléatoire sur la surface de l'enclume
            double startX = centerX + (random.nextDouble() - 0.5) * 0.3;
            double startY = centerY + random.nextDouble() * 0.1;
            double startZ = centerZ + (random.nextDouble() - 0.5) * 0.3;

            // Petites particules d'éclats métalliques colorées
            DustParticleOptions dustOptions = new DustParticleOptions(
                new Vector3f(
                    (float) particleColor.x, 
                    (float) particleColor.y, 
                    (float) particleColor.z
                ), 
                0.5f // Taille plus petite pour des éclats
            );

            serverLevel.sendParticles(
                dustOptions,
                startX, startY, startZ,
                1, // count
                velocityX, velocityY, velocityZ, // velocities as spread
                0.2 // speed multiplier
            );
        }

        // Ajouter des particules de crit pour l'effet d'impact
        for (int i = 0; i < 6 * particleMultiplier; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 0.6;
            double offsetY = random.nextDouble() * 0.2;
            double offsetZ = (random.nextDouble() - 0.5) * 0.6;
            
            serverLevel.sendParticles(
                ParticleTypes.CRIT,
                centerX + offsetX, centerY + offsetY, centerZ + offsetZ,
                1,
                (random.nextDouble() - 0.5) * 0.3, // velocity X
                random.nextDouble() * 0.2 + 0.1,   // velocity Y
                (random.nextDouble() - 0.5) * 0.3, // velocity Z
                0.15
            );
        }

        // Quelques particules d'étincelles pour l'effet de forge
        for (int i = 0; i < 4 * particleMultiplier; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 0.4;
            double offsetY = random.nextDouble() * 0.3;
            double offsetZ = (random.nextDouble() - 0.5) * 0.4;
            
            serverLevel.sendParticles(
                ParticleTypes.LAVA,
                centerX + offsetX, centerY + offsetY, centerZ + offsetZ,
                1,
                (random.nextDouble() - 0.5) * 0.2,
                random.nextDouble() * 0.15,
                (random.nextDouble() - 0.5) * 0.2,
                0.1
            );
        }

        // Pour les raretés très élevées, ajouter des particules spéciales
        if (particleMultiplier >= 3) { // LEGENDARY et MYTHIC
            // Particules d'enchantement supplémentaires
            for (int i = 0; i < 10 * (particleMultiplier - 2); i++) {
                double offsetX = (random.nextDouble() - 0.5);
                double offsetY = random.nextDouble() * 0.5;
                double offsetZ = (random.nextDouble() - 0.5);
                
                serverLevel.sendParticles(
                    ParticleTypes.ENCHANT,
                    centerX + offsetX, centerY + offsetY, centerZ + offsetZ,
                    1,
                    (random.nextDouble() - 0.5) * 0.1,
                    random.nextDouble() * 0.1,
                    (random.nextDouble() - 0.5) * 0.1,
                    0.05
                );
            }
        }

        if (particleMultiplier >= 4) { // MYTHIC seulement
            // Explosion de particules tourbillonnantes pour MYTHIC
            for (int i = 0; i < 20; i++) {
                double angle = (i * Math.PI * 2) / 10.0;
                double radius = 0.8 + random.nextDouble() * 0.4;
                
                double offsetX = Math.cos(angle) * radius;
                double offsetZ = Math.sin(angle) * radius;
                double offsetY = random.nextDouble() * 0.8;
                
                serverLevel.sendParticles(
                    ParticleTypes.DRAGON_BREATH,
                    centerX + offsetX, centerY + offsetY, centerZ + offsetZ,
                    1,
                    -offsetX * 0.1, // Velocity vers le centre
                    random.nextDouble() * 0.1,
                    -offsetZ * 0.1,
                    0.02
                );
            }
        }
    }

    /**
     * Détermine la couleur des particules selon la rareté de l'item
     */
    private Vec3 getParticleColorForRarity(ItemStack stack) {
        // Vérifier d'abord si l'item a une rareté personnalisée
        if (stack.hasTag() && stack.getTag().contains("custom_rarity")) {
            String customRarityName = stack.getTag().getString("custom_rarity");
            return switch (customRarityName) {
                case "common" -> new Vec3(0.6, 0.6, 0.6);      // Gris
                case "uncommon" -> new Vec3(0.2, 0.8, 0.2);    // Vert
                case "rare" -> new Vec3(0.2, 0.2, 1.0);        // Bleu
                case "epic" -> new Vec3(1.0, 1.0, 0.0);        // Jaune
                case "legendary" -> new Vec3(1.0, 0.5, 0.0);   // Orange
                case "mythic" -> new Vec3(1.0, 0.0, 0.0);      // Rouge
                default -> throw new IllegalStateException("Unexpected value: " + customRarityName);
            };
        }

        // Sinon utiliser la rareté vanilla de l'item
        Rarity rarity = stack.getRarity();
        return switch (rarity) {
            case COMMON -> new Vec3(0.6, 0.6, 0.6);      // Gris
            case UNCOMMON -> new Vec3(0.2, 0.8, 0.2);    // Vert
            case RARE -> new Vec3(0.2, 0.2, 1.0);        // Bleu
            case EPIC -> new Vec3(1.0, 0.0, 1.0);        // Violet/Magenta
        };
    }

    /**
     * Détermine le multiplicateur de particules selon la rareté de l'item
     * Plus la rareté est élevée, plus il y a de particules
     */
    private int getParticleMultiplierForRarity(net.minecraft.world.item.ItemStack stack) {
        // Vérifier d'abord si l'item a une rareté personnalisée
        if (stack.hasTag() && stack.getTag().contains("custom_rarity")) {
            String customRarityName = stack.getTag().getString("custom_rarity");
            return switch (customRarityName) {
                case "common" -> 1;      // 15 particules de base
                case "uncommon" -> 1;    // 15 particules
                case "rare" -> 2;        // 30 particules
                case "epic" -> 2;        // 30 particules
                case "legendary" -> 3;   // 45 particules
                case "mythic" -> 4;      // 60 particules
                default -> throw new IllegalStateException("Unexpected value: " + customRarityName);
            };
        }

        // Sinon utiliser la rareté vanilla de l'item
        Rarity rarity = stack.getRarity();
        return switch (rarity) {
            case COMMON -> 1;      // 15 particules de base
            case UNCOMMON -> 1;    // 15 particules
            case RARE, EPIC -> 2;        // 30 particules
        };
    }
}

