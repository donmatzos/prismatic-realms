package at.donmatzos.prismaticrealms.altar;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jspecify.annotations.Nullable;

import at.donmatzos.prismaticrealms.block.ModBlockEntities;

/**
 * The Spectral Altar: shaped like the enchanting table, with a floating Spectral Binder and orbiting shards drawn by the
 * client. No function yet (crafting comes later); right-click falls through to vanilla.
 */
public class SpectralAltarBlock extends BaseEntityBlock {
	private static final VoxelShape SHAPE = Block.column(16.0, 0.0, 12.0);

	public SpectralAltarBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SpectralAltarBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		// the animation is client-only; the server never ticks the altar
		return level.isClientSide() ? createTickerHelper(type, ModBlockEntities.SPECTRAL_ALTAR, SpectralAltarBlockEntity::clientTick) : null;
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
}
