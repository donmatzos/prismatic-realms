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

/** Blitz Slasher: fully charged hits give the wielder Blitz Rush (a short burst of speed). */
public class BlitzSlasherItem extends ChargedHitScytheItem {
	private static final int RUSH_TICKS = 3 * 20;

	public BlitzSlasherItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public Holder<MobEffect> chargedHitEffect() {
		return ModEffects.BLITZ_RUSH;
	}

	@Override
	protected void onChargedHit(ServerLevel level, ItemStack stack, LivingEntity target, LivingEntity attacker) {
		attacker.addEffect(new MobEffectInstance(ModEffects.BLITZ_RUSH, RUSH_TICKS), attacker);
		level.sendParticles(ModParticles.STATIC_SPARK, attacker.getX(), attacker.getY(0.5), attacker.getZ(), 16,
				attacker.getBbWidth() * 0.3, attacker.getBbHeight() * 0.35, attacker.getBbWidth() * 0.3, 0.0);
	}
}
