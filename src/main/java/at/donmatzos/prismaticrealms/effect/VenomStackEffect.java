package at.donmatzos.prismaticrealms.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import at.donmatzos.prismaticrealms.armor.ArmorSets;
import at.donmatzos.prismaticrealms.artifact.VipersDiademItem;
import at.donmatzos.prismaticrealms.family.CrystalFamily;
import at.donmatzos.prismaticrealms.particle.ModParticles;

/**
 * Venom Stack: the amplifier is the number of stacks minus one. Once a second the target loses {@link #DAMAGE_PER_STACK}
 * health per stack, and violet and acid-green venom drips from it, more with more stacks. Every fully charged Scorpion Tail
 * hit adds a stack (up to {@link #MAX_STACKS}; the Venom Crown armor set raises each causer's maximum by 2, see
 * {@link #maxStacksFor}) and resets the duration; when it runs out, all stacks go at once. A kill by the venom counts for
 * whoever applied it.
 */
public class VenomStackEffect extends MobEffect {
	public static final int DURATION_TICKS = 6 * 20;
	public static final int MAX_STACKS = 5;
	/** The maximum for a wielder of the Scorpion Tail wearing the Viper's Diadem. */
	public static final int MAX_STACKS_BOOSTED = 10;
	/** Toxic Coat: the Venom Crown armor set raises the maximum by 2. */
	private static final int SET_BONUS_STACKS = 2;
	private static final int REFRESH_BELOW_TICKS = 40;
	private static final int INTERVAL_TICKS = 20;
	private static final float DAMAGE_PER_STACK = 0.5F;

	private static final EffectSources CAUSERS = new EffectSources();

	public VenomStackEffect(MobEffectCategory category, int color) {
		super(category, color, ModParticles.VENOM_DRIP);
	}

	/** The most stacks this causer can build up: 5, or 10 with the Scorpion Tail in hand and the Viper's Diadem worn, plus 2 for the Venom Crown set. */
	public static int maxStacksFor(LivingEntity causer) {
		int base = VipersDiademItem.boostsVenom(causer) ? MAX_STACKS_BOOSTED : MAX_STACKS;
		return base + (ArmorSets.has(causer, CrystalFamily.VENOM_CROWN) ? SET_BONUS_STACKS : 0);
	}

	private static int stacksOf(LivingEntity target) {
		MobEffectInstance current = target.getEffect(ModEffects.VENOM_STACK);
		return current == null ? 0 : current.getAmplifier() + 1;
	}

	private static void apply(LivingEntity target, LivingEntity causer, int stacks) {
		CAUSERS.put(target, causer, DURATION_TICKS);
		target.addEffect(new MobEffectInstance(ModEffects.VENOM_STACK, DURATION_TICKS, stacks - 1), causer);
	}

	/**
	 * Adds one stack (up to the causer's maximum) to the target and resets the duration; returns the new number of stacks.
	 * A target that already has more stacks than this causer's maximum (from a stronger causer) keeps them.
	 */
	public static int addStack(LivingEntity target, LivingEntity causer) {
		int current = stacksOf(target);
		int stacks = Math.max(current, Math.min(current + 1, maxStacksFor(causer)));
		apply(target, causer, stacks);
		return stacks;
	}

	/** Keeps the target at `minStacks` stacks or more: raises fewer stacks, and renews the duration when it is about to run out. */
	public static void ensureStacks(LivingEntity target, LivingEntity causer, int minStacks) {
		MobEffectInstance current = target.getEffect(ModEffects.VENOM_STACK);
		int stacks = stacksOf(target);
		if (current != null && stacks >= minStacks && current.getDuration() > REFRESH_BELOW_TICKS) {
			return;
		}
		apply(target, causer, Math.max(stacks, minStacks));
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
