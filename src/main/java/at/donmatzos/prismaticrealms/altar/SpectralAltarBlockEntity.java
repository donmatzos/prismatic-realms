package at.donmatzos.prismaticrealms.altar;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import at.donmatzos.prismaticrealms.block.ModBlockEntities;

/** Holds only the client-side animation state (nothing is saved); see SpectralAltarAnimation for the maths. */
public class SpectralAltarBlockEntity extends BlockEntity {
	public int time;
	public float nearby;
	public float oNearby;
	public float orbit;
	public float oOrbit;

	public SpectralAltarBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.SPECTRAL_ALTAR, pos, state);
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, SpectralAltarBlockEntity altar) {
		altar.oNearby = altar.nearby;
		boolean playerNear = level.getNearestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SpectralAltarAnimation.NEAR_RANGE, false) != null;
		altar.nearby = SpectralAltarAnimation.nextNearby(altar.nearby, playerNear);
		float[] orbit = SpectralAltarAnimation.advanceOrbit(altar.orbit, altar.oOrbit, altar.nearby);
		altar.orbit = orbit[0];
		altar.oOrbit = orbit[1];
		altar.time++;
	}
}
