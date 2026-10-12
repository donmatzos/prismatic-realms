package at.donmatzos.prismaticrealms.item;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.effect.BleedEffect;
import at.donmatzos.prismaticrealms.effect.ModEffects;
import at.donmatzos.prismaticrealms.particle.ModParticles;

/** Crimson Diabell: fully charged hits make the target Bleed, which hurts more while it keeps moving. */
public class CrimsonDiabellItem extends ChargedHitScytheItem {
	public CrimsonDiabellItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public Holder<MobEffect> chargedHitEffect() {
		return ModEffects.BLEED;
	}

	@Override
	protected void onChargedHit(ServerLevel level, ItemStack stack, LivingEntity target, LivingEntity attacker) {
		BleedEffect.inflict(target, attacker);
		target.addEffect(new MobEffectInstance(ModEffects.BLEED, BleedEffect.DURATION_TICKS), attacker);
		level.sendParticles(ModParticles.BLOOD_DRIP, target.getX(), target.getY(0.6), target.getZ(), 14,
				target.getBbWidth() * 0.4, target.getBbHeight() * 0.3, target.getBbWidth() * 0.4, 0.0);
	}
}
