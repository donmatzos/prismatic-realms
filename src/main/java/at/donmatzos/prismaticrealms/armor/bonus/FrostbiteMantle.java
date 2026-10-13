package at.donmatzos.prismaticrealms.armor.bonus;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import at.donmatzos.prismaticrealms.effect.ModEffects;

/** Ice Star set: melee attackers are frozen for 2 s (at most once per 3 s per wearer). The scythe adds the 5 s root. */
public class FrostbiteMantle implements SetBonus {
	private static final int FREEZE_TICKS = 2 * 20;
	private static final int COOLDOWN_TICKS = 3 * 20;

	private final Map<UUID, Long> lastUse = new ConcurrentHashMap<>();

	@Override
	public void onHurt(ServerPlayer wearer, DamageSource source, float damage) {
		LivingEntity attacker = Melee.attacker(source);
		if (attacker == null || attacker == wearer) {
			return;
		}
		long now = wearer.level().getGameTime();
		if (Cooldown.ready(this.lastUse.getOrDefault(wearer.getUUID(), Cooldown.NEVER), now, COOLDOWN_TICKS)) {
			attacker.addEffect(new MobEffectInstance(ModEffects.FREEZE, FREEZE_TICKS), wearer);
			this.lastUse.put(wearer.getUUID(), now);
		}
	}

	@Override
	public void onDeactivate(ServerPlayer wearer) {
		this.lastUse.remove(wearer.getUUID());
	}
}
