package at.donmatzos.prismaticrealms.armor.bonus;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import at.donmatzos.prismaticrealms.effect.VenomStackEffect;

/** Venom Crown set: immune to Poison, melee attackers get a Venom Stack (caused by the wearer); +2 max stacks (VenomStackEffect.maxStacksFor). */
public class ToxicCoat implements SetBonus {
	@Override
	public void tick(ServerPlayer wearer) {
		if (wearer.hasEffect(MobEffects.POISON)) {
			wearer.removeEffect(MobEffects.POISON);
		}
	}

	@Override
	public void onHurt(ServerPlayer wearer, DamageSource source, float damage) {
		LivingEntity attacker = Melee.attacker(source);
		if (attacker != null && attacker != wearer) {
			VenomStackEffect.addStack(attacker, wearer);
		}
	}
}
