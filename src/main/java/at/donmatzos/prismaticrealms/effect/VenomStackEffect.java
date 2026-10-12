package at.donmatzos.prismaticrealms.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import at.donmatzos.prismaticrealms.particle.ModParticles;

/**
 * Venom Stack: the amplifier is the number of stacks minus one. Once a second the target loses {@link #DAMAGE_PER_STACK}
 * health per stack, and violet and acid-green venom drips from it, more with more stacks. Every fully charged Scorpion Tail hit adds a
 * stack (up to {@link #MAX_STACKS}) and resets the duration; when it runs out, all stacks go at once. A kill by the venom
 * counts for whoever applied it.
 */
public class VenomStackEffect extends MobEffect {
	public static final int DURATION_TICKS = 6 * 20;
	public static final int MAX_STACKS = 5;
	private static final int INTERVAL_TICKS = 20;
	private static final float DAMAGE_PER_STACK = 0.5F;

	private static final EffectSources CAUSERS = new EffectSources();

	public VenomStackEffect(MobEffectCategory category, int color) {
		super(category, color, ModParticles.VENOM_DRIP);
	}

	/** Adds one stack (up to the maximum) to the target and resets the duration; returns the new number of stacks. */
	public static int addStack(LivingEntity target, LivingEntity causer) {
		MobEffectInstance current = target.getEffect(ModEffects.VENOM_STACK);
		int amplifier = current == null ? 0 : Math.min(current.getAmplifier() + 1, MAX_STACKS - 1);
		CAUSERS.put(target, causer, DURATION_TICKS);
		target.addEffect(new MobEffectInstance(ModEffects.VENOM_STACK, DURATION_TICKS, amplifier), causer);
		return amplifier + 1;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int remainingTicks, int amplification) {
		return remainingTicks % INTERVAL_TICKS == 0;
	}

	@Override
	public boolean applyEffectTick(ServerLevel level, LivingEntity target, int amplification) {
		int stacks = amplification + 1;
		Entity causer = CAUSERS.get(level, target);
		DamageSource source = causer != null ? level.damageSources().indirectMagic(causer, causer) : level.damageSources().magic();
		target.hurtServer(level, source, DAMAGE_PER_STACK * stacks);
		level.sendParticles(ModParticles.VENOM_DRIP, target.getX(), target.getY(0.6), target.getZ(), 2 + 2 * stacks,
				target.getBbWidth() * 0.35, target.getBbHeight() * 0.25, target.getBbWidth() * 0.35, 0.0);
		return true;
	}

	@Override
	public void onMobRemoved(ServerLevel level, LivingEntity mob, int amplifier, Entity.RemovalReason reason) {
		CAUSERS.remove(mob);
	}
}
