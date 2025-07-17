package net.Kyap.ItemsRarity.block.custom;

import net.Kyap.ItemsRarity.util.RarityManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
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
import org.jetbrains.annotations.Nullable;

public class EnhancedAnvilBlock extends BaseEntityBlock {

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

    public EnhancedAnvilBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return null;
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, net.minecraft.world.entity.player.Player player, InteractionHand hand, BlockHitResult hit) {
        return InteractionResult.SUCCESS;
    }
}
