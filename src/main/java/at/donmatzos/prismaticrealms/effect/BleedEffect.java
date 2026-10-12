package at.donmatzos.prismaticrealms.effect;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import at.donmatzos.prismaticrealms.particle.ModParticles;

/**
 * Bleed: once a second the target loses {@link #DAMAGE} health, or {@link #MOVING_DAMAGE} if it moved more than
 * {@link #MOVING_DISTANCE} blocks since the last tick; dark red blood drops fall from it, more while it moves. A kill by
 * the bleed counts for whoever caused it.
 */
public class BleedEffect extends MobEffect {
	public static final int DURATION_TICKS = 5 * 20;
	private static final int INTERVAL_TICKS = 20;
	private static final float DAMAGE = 1.0F;
	private static final float MOVING_DAMAGE = 2.0F;
	private static final double MOVING_DISTANCE = 0.5;

	private static final EffectSources CAUSERS = new EffectSources();
	// where each bleeding entity was at the previous bleed tick, to tell whether it kept moving
	private static final Map<UUID, Vec3> LAST_POSITIONS = new ConcurrentHashMap<>();

	public BleedEffect(MobEffectCategory category, int color) {
		super(category, color, ModParticles.BLOOD_DRIP);
	}

	public static void inflict(LivingEntity target, LivingEntity causer) {
		CAUSERS.put(target, causer, DURATION_TICKS);
		LAST_POSITIONS.put(target.getUUID(), target.position());
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int remainingTicks, int amplification) {
		return remainingTicks % INTERVAL_TICKS == 0;
	}

	@Override
	public boolean applyEffectTick(ServerLevel level, LivingEntity target, int amplification) {
		Vec3 last = LAST_POSITIONS.put(target.getUUID(), target.position());
		boolean moving = last != null && last.distanceTo(target.position()) > MOVING_DISTANCE;
		Entity causer = CAUSERS.get(level, target);
		DamageSource source = causer != null ? level.damageSources().indirectMagic(causer, causer) : level.damageSources().magic();
		target.hurtServer(level, source, moving ? MOVING_DAMAGE : DAMAGE);
		level.sendParticles(ModParticles.BLOOD_DRIP, target.getX(), target.getY(0.6), target.getZ(), moving ? 10 : 4,
				target.getBbWidth() * 0.35, target.getBbHeight() * 0.25, target.getBbWidth() * 0.35, 0.0);
		return true;
	}

	@Override
	public void onMobRemoved(ServerLevel level, LivingEntity mob, int amplifier, Entity.RemovalReason reason) {
		CAUSERS.remove(mob);
		LAST_POSITIONS.remove(mob.getUUID());
	}
}
