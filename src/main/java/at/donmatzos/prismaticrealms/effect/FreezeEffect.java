package at.donmatzos.prismaticrealms.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import at.donmatzos.prismaticrealms.particle.ModParticles;

/** Freeze: the slow itself is an attribute modifier (see ModEffects); this adds the snow streaming upwards off the target. */
public class FreezeEffect extends MobEffect {
	public FreezeEffect(MobEffectCategory category, int color) {
		super(category, color, ModParticles.FROST_FLURRY);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
		return tickCount % 3 == 0;
	}

	@Override
	public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
		level.sendParticles(ModParticles.FROST_FLURRY, mob.getX(), mob.getY(0.4), mob.getZ(), 3,
				mob.getBbWidth() * 0.35, mob.getBbHeight() * 0.3, mob.getBbWidth() * 0.35, 0.0);
		return true;
	}
}
