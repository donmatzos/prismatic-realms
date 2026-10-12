package at.donmatzos.prismaticrealms.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import at.donmatzos.prismaticrealms.particle.ModParticles;

/**
 * Tidecaller: a marker buff from The Shallow Waters' fully charged hits. It comes with vanilla Dolphin's Grace and Water
 * Breathing (applied alongside, see ShallowWatersItem), and while it lasts the scythe's hits heal its wielder in water.
 * This class only adds the glowing teal motes rising off the wielder.
 */
public class TidecallerEffect extends MobEffect {
	public TidecallerEffect(MobEffectCategory category, int color) {
		super(category, color, ModParticles.TIDE_MOTE);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int remainingTicks, int amplification) {
		return remainingTicks % 4 == 0;
	}

	@Override
	public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
		level.sendParticles(ModParticles.TIDE_MOTE, mob.getX(), mob.getY(0.4), mob.getZ(), 2,
				mob.getBbWidth() * 0.35, mob.getBbHeight() * 0.3, mob.getBbWidth() * 0.35, 0.0);
		return true;
	}
}
