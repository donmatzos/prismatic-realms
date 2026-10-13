package at.donmatzos.prismaticrealms.item;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.effect.ModEffects;
import at.donmatzos.prismaticrealms.particle.ModParticles;

/**
 * The Shallow Waters: fully charged hits give the wielder Tidecaller for 8 seconds; while it lasts, every hit with this
 * scythe heals the wielder 2 health while they're wet (in water or rain), otherwise 1. No vanilla effects come with it
 * any more: the Abyssal Heart covers breathing and swimming, so the scythe adds sustain instead.
 */
public class ShallowWatersItem extends ChargedHitScytheItem {
	private static final int TIDECALLER_TICKS = 8 * 20;
	private static final float WET_HEAL = 2.0F;
	private static final float DRY_HEAL = 1.0F;

	public ShallowWatersItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public Holder<MobEffect> chargedHitEffect() {
		return ModEffects.TIDECALLER;
	}

	@Override
	protected void onChargedHit(ServerLevel level, ItemStack stack, LivingEntity target, LivingEntity attacker) {
		attacker.addEffect(new MobEffectInstance(ModEffects.TIDECALLER, TIDECALLER_TICKS), attacker);
		level.sendParticles(ModParticles.TIDE_MOTE, attacker.getX(), attacker.getY(0.5), attacker.getZ(), 14,
				attacker.getBbWidth() * 0.35, attacker.getBbHeight() * 0.35, attacker.getBbWidth() * 0.35, 0.0);
	}

	@Override
	public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		// checked before super, so the very hit that grants Tidecaller doesn't also heal
		boolean tidecaller = attacker.hasEffect(ModEffects.TIDECALLER);
		super.hurtEnemy(stack, target, attacker);
		if (tidecaller && attacker.level() instanceof ServerLevel level) {
			attacker.heal(attacker.isInWaterOrRain() ? WET_HEAL : DRY_HEAL);
			level.sendParticles(ModParticles.TIDE_MOTE, attacker.getX(), attacker.getY(0.5), attacker.getZ(), 8,
					attacker.getBbWidth() * 0.4, attacker.getBbHeight() * 0.3, attacker.getBbWidth() * 0.4, 0.0);
		}
	}
}
