package at.donmatzos.prismaticrealms.item;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.effect.ModEffects;
import at.donmatzos.prismaticrealms.particle.ModParticles;

/** Melting Sunray: fully charged hits give the wielder Radiance (attackers catch fire) and Fire Resistance for 6 seconds. */
public class MeltingSunrayItem extends ChargedHitScytheItem {
	private static final int RADIANCE_TICKS = 6 * 20;

	public MeltingSunrayItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public Holder<MobEffect> chargedHitEffect() {
		return ModEffects.RADIANCE;
	}

	@Override
	protected void onChargedHit(ServerLevel level, ItemStack stack, LivingEntity target, LivingEntity attacker) {
		attacker.addEffect(new MobEffectInstance(ModEffects.RADIANCE, RADIANCE_TICKS), attacker);
		attacker.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, RADIANCE_TICKS), attacker);
		level.sendParticles(ModParticles.SUN_MOTE, attacker.getX(), attacker.getY(0.5), attacker.getZ(), 14,
				attacker.getBbWidth() * 0.35, attacker.getBbHeight() * 0.35, attacker.getBbWidth() * 0.35, 0.0);
	}
}
