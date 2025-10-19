package net.Kyap.ItemsRarity.block.custom;

import net.Kyap.ItemsRarity.block.entity.custom.EnhancedAnvilBlockEntity;
import net.Kyap.ItemsRarity.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.Kyap.ItemsRarity.block.custom.particle.ParticleAnvilBlock.spawnRarityParticles;

public class EnhancedAnvilBlock extends BaseEntityBlock {

    public static final VoxelShape SHAPE = makeShape();

    public EnhancedAnvilBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    private static VoxelShape makeShape() {
        VoxelShape shape = Block.box(2, 10, 2, 14, 16, 14);
        shape = Shapes.or(shape, Block.box(5, 4, 5, 11, 10, 11));
        shape = Shapes.or(shape, Block.box(2, 0, 2, 14, 4, 14));
        shape = Shapes.or(shape, Block.box(5, 11, 0, 11, 15, 2));
        shape = Shapes.or(shape, Block.box(0, 11, 5, 2, 15, 11));
        shape = Shapes.or(shape, Block.box(14, 11, 5, 16, 15, 11));
        shape = Shapes.or(shape, Block.box(5, 11, 14, 11, 15, 16));
        return shape;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EnhancedAnvilBlockEntity(blockPos, blockState);
    }

    @Override
    public @NotNull InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof EnhancedAnvilBlockEntity enhancedAnvil) {
                ItemStack heldItem = pPlayer.getItemInHand(pHand);
                boolean hasEmeraldHammer = heldItem.getItem() == ModItems.EMERALD_HAMMER.get();
                if (hasEmeraldHammer) {
                    boolean canUpgrade = enhancedAnvil.canUpgrade();
                    if (canUpgrade) {
                        boolean success = enhancedAnvil.performUpgrade();
                        if (success) {
                            playUpgradeAnimation(pLevel, pPos, enhancedAnvil, true);
                            return InteractionResult.SUCCESS;
                        }
                            playUpgradeAnimation(pLevel, pPos, enhancedAnvil, false);
                    }
                    return InteractionResult.FAIL;
                } else {
                    NetworkHooks.openScreen((ServerPlayer) pPlayer, enhancedAnvil, pPos);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    private void playUpgradeAnimation(Level pLevel, BlockPos pPos, EnhancedAnvilBlockEntity enhancedAnvilBlock, boolean success){
        if (success){
            spawnRarityParticles(pLevel, pPos, enhancedAnvilBlock);
            pLevel.playSound(null, pPos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.2f, 1.0f); // Hammer sound
            pLevel.playSound(null, pPos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.8f, 1.3f); // Impact sound
            pLevel.playSound(null, pPos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 0.6f, 1.5f); // Success sound
        }
        else {
            pLevel.playSound(null, pPos, SoundEvents.ANVIL_DESTROY, SoundSource.BLOCKS, 0.5f, 0.8f); // Fail sound
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof EnhancedAnvilBlockEntity enhancedAnvil) {
                for (int i = 0; i < enhancedAnvil.getItemHandler().getSlots(); i++) {
                    net.minecraft.world.Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(),
                            enhancedAnvil.getItemHandler().getStackInSlot(i));
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}

