package at.donmatzos.prismaticrealms.item;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.effect.ModEffects;
import at.donmatzos.prismaticrealms.effect.VenomStackEffect;
import at.donmatzos.prismaticrealms.particle.ModParticles;

/** Scorpion Tail: every fully charged hit adds a stack of Venom Stack to the target, which hurts more per stack. */
public class ScorpionTailItem extends ChargedHitScytheItem {
	public ScorpionTailItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public Holder<MobEffect> chargedHitEffect() {
		return ModEffects.VENOM_STACK;
	}

	@Override
	protected void onChargedHit(ServerLevel level, ItemStack stack, LivingEntity target, LivingEntity attacker) {
		int stacks = VenomStackEffect.addStack(target, attacker);
		level.sendParticles(ModParticles.VENOM_DRIP, target.getX(), target.getY(0.6), target.getZ(), 6 + 3 * stacks,
				target.getBbWidth() * 0.4, target.getBbHeight() * 0.3, target.getBbWidth() * 0.4, 0.0);
	}
}
