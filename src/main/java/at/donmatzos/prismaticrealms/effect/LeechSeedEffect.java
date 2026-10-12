package at.donmatzos.prismaticrealms.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import at.donmatzos.prismaticrealms.particle.ModParticles;

/**
 * Leech Seed: once a second the target loses {@link #DRAIN} health and whoever planted the seed is healed by the same
 * amount, with green spores streaming from the target to them. A kill by the drain counts for the planter.
 */
public class LeechSeedEffect extends MobEffect {
	public static final int DURATION_TICKS = 5 * 20;
	private static final int INTERVAL_TICKS = 20;
	private static final float DRAIN = 1.0F;
	private static final double MAX_HEAL_DISTANCE = 32.0;
	private static final int SPORE_TRAVEL_TICKS = 16;   // matches LeechSporeParticle's lifetime

	private static final EffectSources PLANTERS = new EffectSources();

	public LeechSeedEffect(MobEffectCategory category, int color) {
		super(category, color, ModParticles.VERDANT_MOTE);
	}

	public static void plant(LivingEntity target, LivingEntity planter) {
		PLANTERS.put(target, planter, DURATION_TICKS);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int remainingTicks, int amplification) {
		return remainingTicks % INTERVAL_TICKS == 0;
	}

	@Override
	public boolean applyEffectTick(ServerLevel level, LivingEntity target, int amplification) {
		Entity planter = PLANTERS.get(level, target);
		DamageSource source = planter != null ? level.damageSources().indirectMagic(planter, planter) : level.damageSources().magic();
		target.hurtServer(level, source, DRAIN);
		if (planter instanceof LivingEntity living && living.isAlive() && living.distanceTo(target) <= MAX_HEAL_DISTANCE) {
			living.heal(DRAIN);
			this.sporeStream(level, target, living);
		}
		return true;
	}

	/** A short burst of spores flying from the target to the planter (count 0 = the offsets are the velocity). */
	private void sporeStream(ServerLevel level, LivingEntity from, LivingEntity to) {
		Vec3 start = from.position().add(0, from.getBbHeight() * 0.6, 0);
		Vec3 end = to.position().add(0, to.getBbHeight() * 0.6, 0);
		Vec3 velocity = end.subtract(start).scale(1.0 / SPORE_TRAVEL_TICKS);
		for (int i = 0; i < 6; i++) {
			double jitter = (level.getRandom().nextDouble() - 0.5) * 0.3;
			level.sendParticles(ModParticles.LEECH_SPORE, start.x + jitter, start.y + jitter, start.z - jitter, 0,
					velocity.x, velocity.y, velocity.z, 1.0);
		}
	}

	@Override
	public void onMobRemoved(ServerLevel level, LivingEntity mob, int amplifier, Entity.RemovalReason reason) {
		PLANTERS.remove(mob);
	}
}
