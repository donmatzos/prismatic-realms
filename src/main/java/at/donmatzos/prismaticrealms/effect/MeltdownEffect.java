package at.donmatzos.prismaticrealms.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import at.donmatzos.prismaticrealms.particle.ModParticles;

/**
 * Meltdown: Melting Sunray's fully charged hits melt the target's armor (the -50 % ARMOR modifier is added in
 * ModEffects). This class only adds the molten drops falling off the target.
 */
public class MeltdownEffect extends MobEffect {
	public MeltdownEffect(MobEffectCategory category, int color) {
		super(category, color, ModParticles.MOLTEN_DRIP);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int remainingTicks, int amplification) {
		return remainingTicks % 4 == 0;
	}

	@Override
	public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
		level.sendParticles(ModParticles.MOLTEN_DRIP, mob.getX(), mob.getY(0.6), mob.getZ(), 3,
				mob.getBbWidth() * 0.35, mob.getBbHeight() * 0.25, mob.getBbWidth() * 0.35, 0.0);
		return true;
	}
}
