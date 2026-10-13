package at.donmatzos.prismaticrealms.artifact;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

import org.jspecify.annotations.Nullable;

import at.donmatzos.prismaticrealms.block.ModBlocks;

/**
 * The Ice Star artifact: Frost Walker II for water and lava (lava becomes Rimed Magma), plus immunity to magma blocks
 * and powder-snow freezing. The disk follows vanilla's enchantment (ReplaceDisk) but runs every tick.
 */
public class FrostwalkerStarItem extends ArtifactItem {
	/** Vanilla Frost Walker's radius is perLevel(3, 1): 4 at level II. */
	public static final int RADIUS = 4;

	public FrostwalkerStarItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public void tickMovement(ServerPlayer wearer) {
		if (!wearer.onGround() || wearer.isPassenger() || wearer.isSpectator()) {
			return;
		}
		ServerLevel level = wearer.level();
		Vec3 position = wearer.position();
		BlockPos center = BlockPos.containing(position).below();
		for (BlockPos pos : BlockPos.betweenClosed(center.offset(-RADIUS, 0, -RADIUS), center.offset(RADIUS, 0, RADIUS))) {
			if (pos.distToCenterSqr(position.x(), pos.getY() + 0.5, position.z()) >= RADIUS * RADIUS) {
				continue;
			}
			BlockState frozen = frozenState(level.getBlockState(pos));
			if (frozen != null && level.getBlockState(pos.above()).is(BlockTags.AIR)
					&& level.isUnobstructed(null, Shapes.block().move(pos))
					&& level.setBlockAndUpdate(pos, frozen)) {
				level.gameEvent(wearer, GameEvent.BLOCK_PLACE, pos);
			}
		}
	}

	/** Water and lava sources only, like vanilla (flowing liquid stays). */
	private static @Nullable BlockState frozenState(BlockState state) {
		if (state.is(Blocks.WATER) && state.getFluidState().is(Fluids.WATER)) {
			return Blocks.FROSTED_ICE.defaultBlockState();
		}
		if (state.is(Blocks.LAVA) && state.getFluidState().is(Fluids.LAVA)) {
			return ModBlocks.RIMED_MAGMA.defaultBlockState();
		}
		return null;
	}

	@Override
	public boolean preventsDamage(ServerPlayer wearer, DamageSource source) {
		return source.is(DamageTypes.HOT_FLOOR);
	}

	@Override
	public boolean preventsFreezing() {
		return true;
	}
}
