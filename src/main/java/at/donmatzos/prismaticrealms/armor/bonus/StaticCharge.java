package at.donmatzos.prismaticrealms.armor.bonus;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import at.donmatzos.prismaticrealms.particle.ModParticles;

/** Storm Fang set: after 3 s of unbroken sprinting the next melee hit deals +6 damage and discharges in sparks. */
public class StaticCharge implements SetBonus {
	private static final float BONUS_DAMAGE = 6.0F;

	private final Map<UUID, SprintCharge> charges = new ConcurrentHashMap<>();

	@Override
	public void tick(ServerPlayer wearer) {
		SprintCharge charge = this.charges.computeIfAbsent(wearer.getUUID(), id -> new SprintCharge());
		boolean wasCharged = charge.isCharged();
		charge.tick(wearer.isSprinting());
		if (charge.isCharged() && !wasCharged) {
			wearer.level().playSound(null, wearer.getX(), wearer.getY(), wearer.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.3F, 2.0F);
		}
		if (charge.isCharged() && wearer.level() instanceof ServerLevel level && wearer.tickCount % 4 == 0) {
			level.sendParticles(ModParticles.STATIC_SPARK, wearer.getX(), wearer.getY(0.5), wearer.getZ(), 1, 0.3, 0.5, 0.3, 0.0);
		}
	}

	@Override
	public float modifyAttackDamage(ServerPlayer attacker, LivingEntity target, DamageSource source, float amount) {
		// The damage mixin runs before vanilla's dead and invulnerability-frame checks; keep the charge for a hit that cannot land.
		if (!target.isAlive() || target.invulnerableTime > 10) {
			return amount;
		}
		SprintCharge charge = this.charges.get(attacker.getUUID());
		if (charge == null || !charge.consume()) {
			return amount;
		}
		if (attacker.level() instanceof ServerLevel level) {
			level.sendParticles(ModParticles.STATIC_SPARK, target.getX(), target.getY(0.5), target.getZ(), 14, 0.4, 0.5, 0.4, 0.0);
			level.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 0.4F, 1.6F);
		}
		return amount + BONUS_DAMAGE;
	}

	@Override
	public void onDeactivate(ServerPlayer wearer) {
		this.charges.remove(wearer.getUUID());
	}
}
