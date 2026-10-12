package at.donmatzos.prismaticrealms.altar;

/**
 * The Spectral Altar's animation maths as plain Java (unit-tested). Angles in radians, offsets in blocks, time in ticks.
 * Every periodic motion repeats within ANIMATION_PERIOD ticks, so callers feed wrapTime(...) in and floats never lose
 * precision on long-running worlds.
 */
public final class SpectralAltarAnimation {
	public static final float TWO_PI = (float) (Math.PI * 2.0);
	/** Ticks after which every motion repeats: the spin period, a multiple of the bob period. */
	public static final int ANIMATION_PERIOD = 400;
	/** A player within this many blocks of the altar's centre speeds the orbit up. */
	public static final double NEAR_RANGE = 3.0;
	public static final float BINDER_BOB = 0.02F;
	public static final float SHARD_BOB = 0.04F;
	private static final float NEARBY_STEP = 0.1F;
	private static final int ORBIT_TICKS = 200;         // one turn per 10 s while nobody is near
	private static final int BOB_TICKS = 80;
	private static final int SPIN_TICKS = ANIMATION_PERIOD;

	private SpectralAltarAnimation() {
	}

	/** Eases `nearby` (0..1) one step towards 1 while a player is near, towards 0 otherwise. */
	public static float nextNearby(float current, boolean playerNear) {
		float next = current + (playerNear ? NEARBY_STEP : -NEARBY_STEP);
		return Math.max(0.0F, Math.min(1.0F, next));
	}

	/** Orbit angle per tick: one turn per 10 s, up to twice as fast at full `nearby`. */
	public static float orbitStep(float nearby) {
		return TWO_PI / ORBIT_TICKS * (1.0F + nearby);
	}

	/** One tick of the orbit: returns {orbit, oOrbit}. Both drop by a full turn together, so interpolation never jumps. */
	public static float[] advanceOrbit(float orbit, float oOrbit, float nearby) {
		float previous = orbit;
		float next = orbit + orbitStep(nearby);
		if (next >= TWO_PI) {
			next -= TWO_PI;
			previous -= TWO_PI;
		}
		return new float[] {next, previous};
	}

	public static float shardAngle(int index, int count, float orbit) {
		return orbit + index * TWO_PI / count;
	}

	/** Each shard bobs on its own phase, so the ring moves like a wave. */
	public static float shardBob(int index, int count, float time) {
		return SHARD_BOB * (float) Math.sin(time * TWO_PI / BOB_TICKS + index * TWO_PI / count);
	}

	/** The binder turns slowly against the orbit's direction. */
	public static float binderSpin(float time) {
		return -time * TWO_PI / SPIN_TICKS;
	}

	public static float binderBob(float time) {
		return BINDER_BOB * (float) Math.sin(time * TWO_PI / BOB_TICKS);
	}

	/** The block entity's tick counter reduced to one animation period, plus the partial tick. */
	public static float wrapTime(int ticks, float partialTicks) {
		return Math.floorMod(ticks, ANIMATION_PERIOD) + partialTicks;
	}
}
