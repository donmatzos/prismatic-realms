package at.donmatzos.prismaticrealms.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import at.donmatzos.prismaticrealms.particle.ModParticles;

/** Blitz Rush: the speed boost itself is an attribute modifier (see ModEffects); this radiates yellow static sparks. */
public class BlitzRushEffect extends MobEffect {
	public BlitzRushEffect(MobEffectCategory category, int color) {
		super(category, color, ModParticles.STATIC_SPARK);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int remainingTicks, int amplification) {
		return remainingTicks % 2 == 0;
	}

	@Override
	public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
		// the client gives each spark its own outward direction (StaticSparkParticle)
		level.sendParticles(ModParticles.STATIC_SPARK, mob.getX(), mob.getY(0.5), mob.getZ(), 3,
				mob.getBbWidth() * 0.3, mob.getBbHeight() * 0.3, mob.getBbWidth() * 0.3, 0.0);
		return true;
	}
}
