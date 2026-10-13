package at.donmatzos.prismaticrealms.armor.bonus;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;

public final class Melee {
	private Melee() {
	}

	/** The living entity that hit directly in melee (mob or player attack), or null for projectiles, magic, fire, etc. */
	public static LivingEntity attacker(DamageSource source) {
		boolean melee = source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK_NO_AGGRO);
		return melee && source.getDirectEntity() instanceof LivingEntity attacker ? attacker : null;
	}
}
