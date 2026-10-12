package at.donmatzos.prismaticrealms.effect;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

/**
 * Mob effects don't remember who applied them, so effects that need their source (damage credit, healing) keep it here:
 * target -> source + the game time the effect runs out. Expired entries are ignored and pruned, so the same effect from
 * another source (e.g. /effect) is never credited to an old attacker. In memory only: after a restart there is no source.
 */
public final class EffectSources {
	private record Source(UUID id, long expiresAt) {
	}

	private final Map<UUID, Source> sources = new ConcurrentHashMap<>();

	public void put(LivingEntity target, Entity source, int durationTicks) {
		long now = target.level().getGameTime();
		this.sources.values().removeIf(entry -> entry.expiresAt() < now);
		this.sources.put(target.getUUID(), new Source(source.getUUID(), now + durationTicks));
	}

	/** The entity that applied the effect to the target, or null if unknown, expired or no longer loaded. */
	public Entity get(ServerLevel level, LivingEntity target) {
		Source entry = this.sources.get(target.getUUID());
		return entry == null || entry.expiresAt() < level.getGameTime() ? null : level.getEntity(entry.id());
	}

	public void remove(LivingEntity target) {
		this.sources.remove(target.getUUID());
	}
}
