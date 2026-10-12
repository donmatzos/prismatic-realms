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

/** Scythe of the Frozen Expanse: fully charged hits apply Freeze (3 s, 25 % movement speed) to the target. */
public class FrozenExpanseScytheItem extends ChargedHitScytheItem {
	private static final int FREEZE_TICKS = 3 * 20;

	public FrozenExpanseScytheItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public Holder<MobEffect> chargedHitEffect() {
		return ModEffects.FREEZE;
	}

	@Override
	protected void onChargedHit(ServerLevel level, ItemStack stack, LivingEntity target, LivingEntity attacker) {
		target.addEffect(new MobEffectInstance(ModEffects.FREEZE, FREEZE_TICKS), attacker);
		level.sendParticles(ModParticles.FROST_FLURRY, target.getX(), target.getY(0.5), target.getZ(), 12,
				target.getBbWidth() * 0.4, target.getBbHeight() * 0.35, target.getBbWidth() * 0.4, 0.0);
	}
}
