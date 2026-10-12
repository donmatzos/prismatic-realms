package at.donmatzos.prismaticrealms.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import at.donmatzos.prismaticrealms.particle.ModParticles;

/**
 * Radiance: whoever damages the bearer is set on fire (including archers: it ignites the entity that caused the damage).
 * Comes with vanilla Fire Resistance (applied alongside, see MeltingSunrayItem). The bearer sheds rising golden sun motes;
 * "glowing" is shown with particles rather than vanilla Glowing, which would outline the bearer for their enemies.
 */
public class RadianceEffect extends MobEffect {
	private static final int IGNITE_TICKS = 4 * 20;

	public RadianceEffect(MobEffectCategory category, int color) {
		super(category, color, ModParticles.SUN_MOTE);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int remainingTicks, int amplification) {
		return remainingTicks % 4 == 0;
	}

	@Override
	public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
		level.sendParticles(ModParticles.SUN_MOTE, mob.getX(), mob.getY(0.4), mob.getZ(), 2,
				mob.getBbWidth() * 0.35, mob.getBbHeight() * 0.3, mob.getBbWidth() * 0.35, 0.0);
		return true;
	}

	@Override
	public void onMobHurt(ServerLevel level, LivingEntity mob, int amplifier, DamageSource source, float damage) {
		if (source.getEntity() instanceof LivingEntity attacker && attacker != mob && !attacker.fireImmune()) {
			attacker.igniteForTicks(IGNITE_TICKS);
			level.sendParticles(ParticleTypes.FLAME, attacker.getX(), attacker.getY(0.5), attacker.getZ(), 10,
					attacker.getBbWidth() * 0.35, attacker.getBbHeight() * 0.3, attacker.getBbWidth() * 0.35, 0.02);
		}
	}
}
