package at.donmatzos.prismaticrealms.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FrostedIceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import org.jspecify.annotations.Nullable;

/**
 * Lava frozen by the Frostwalker Star: ages and melts exactly like frosted ice, but always back into a lava source,
 * also when a player breaks it and in the Nether (IceBlock would leave water, or air where water evaporates).
 */
public class RimedMagmaBlock extends FrostedIceBlock {
	public RimedMagmaBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected void melt(BlockState state, Level level, BlockPos pos) {
		level.setBlockAndUpdate(pos, Blocks.LAVA.defaultBlockState());
		level.neighborChanged(pos, Blocks.LAVA, null);
		level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
	}

	// Block#playerDestroy minus the drops (there are none), without IceBlock's water handling.
	@Override
	public void playerDestroy(ServerLevel level, ServerPlayer player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack destroyedWith) {
		player.awardStat(Stats.BLOCK_MINED.get(this));
		player.causeFoodExhaustion(0.005F);
		level.setBlockAndUpdate(pos, Blocks.LAVA.defaultBlockState());
	}
}
