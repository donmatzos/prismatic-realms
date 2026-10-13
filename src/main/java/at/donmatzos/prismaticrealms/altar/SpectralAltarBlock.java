package at.donmatzos.prismaticrealms.altar;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jspecify.annotations.Nullable;

import at.donmatzos.prismaticrealms.block.ModBlockEntities;

/**
 * The Spectral Altar: shaped like the enchanting table, with a floating Spectral Binder and orbiting shards drawn by the
 * client. Right-click opens the altar's crafting menu (SpectralAltarMenu).
 */
public class SpectralAltarBlock extends BaseEntityBlock {
	private static final VoxelShape SHAPE = Block.column(16.0, 0.0, 12.0);
	private static final Component CONTAINER_TITLE = Component.translatable("container.prismaticrealms.spectral_altar");

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
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (!level.isClientSide()) {
			player.openMenu(state.getMenuProvider(level, pos));
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		return new SimpleMenuProvider((containerId, inventory, player) -> new SpectralAltarMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
}
