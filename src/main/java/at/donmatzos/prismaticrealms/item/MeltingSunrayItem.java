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

/** Melting Sunray: fully charged hits melt the target's armor (Meltdown: -50 % armor for 6 seconds; refreshes, doesn't stack). */
public class MeltingSunrayItem extends ChargedHitScytheItem {
	private static final int MELTDOWN_TICKS = 6 * 20;

	public MeltingSunrayItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public Holder<MobEffect> chargedHitEffect() {
		return ModEffects.MELTDOWN;
	}

	@Override
	protected void onChargedHit(ServerLevel level, ItemStack stack, LivingEntity target, LivingEntity attacker) {
		target.addEffect(new MobEffectInstance(ModEffects.MELTDOWN, MELTDOWN_TICKS), attacker);
		level.sendParticles(ModParticles.MOLTEN_DRIP, target.getX(), target.getY(0.6), target.getZ(), 14,
				target.getBbWidth() * 0.35, target.getBbHeight() * 0.3, target.getBbWidth() * 0.35, 0.0);
	}
}
