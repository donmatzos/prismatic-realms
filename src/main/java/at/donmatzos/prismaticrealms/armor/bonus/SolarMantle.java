package at.donmatzos.prismaticrealms.armor.bonus;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

/** Cruel Sun set: melee attackers burn for 4 s; the wearer's hits on burning targets deal 20 % more. */
public class SolarMantle implements SetBonus {
	private static final float IGNITE_SECONDS = 4.0F;
	private static final float BURNING_DAMAGE_FACTOR = 1.2F;

	@Override
	public void onHurt(ServerPlayer wearer, DamageSource source, float damage) {
		LivingEntity attacker = Melee.attacker(source);
		if (attacker != null && attacker != wearer) {
			attacker.igniteForSeconds(IGNITE_SECONDS);
		}
	}

	@Override
	public float modifyAttackDamage(ServerPlayer attacker, LivingEntity target, DamageSource source, float amount) {
		return target.isOnFire() ? amount * BURNING_DAMAGE_FACTOR : amount;
	}
}
