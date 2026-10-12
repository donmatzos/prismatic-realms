package at.donmatzos.prismaticrealms.artifact;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Applies equipped artifacts' bonuses. Attribute modifiers are transient and rebuilt by refresh() whenever the slots
 * change, on join and on respawn (every artifact modifier is removed first, so nothing doubles or lingers). The other
 * hooks are dispatched from Fabric events; the tick hook runs once a second.
 */
public final class ArtifactEffects {
	private static final int TICK_INTERVAL = 20;

	private ArtifactEffects() {
	}

	public static List<ArtifactItem> equipped(Player player) {
		List<ArtifactItem> artifacts = new ArrayList<>();
		for (ItemStack stack : ArtifactSlots.of(player).stacks()) {
			if (stack.getItem() instanceof ArtifactItem artifact) {
				artifacts.add(artifact);
			}
		}
		return artifacts;
	}

	/** Removes every artifact's modifiers, then adds those of the equipped artifacts. */
	public static void refresh(ServerPlayer player) {
		for (Item item : BuiltInRegistries.ITEM) {
			if (item instanceof ArtifactItem artifact) {
				artifact.addModifiers((attribute, modifier) -> {
					AttributeInstance instance = player.getAttribute(attribute);
					if (instance != null) {
						instance.removeModifier(modifier.id());
					}
				});
			}
		}
		for (ArtifactItem artifact : equipped(player)) {
			artifact.addModifiers((attribute, modifier) -> {
				AttributeInstance instance = player.getAttribute(attribute);
				if (instance != null) {
					instance.addTransientModifier(modifier);
				}
			});
		}
	}

	public static void initialize() {
		ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> refresh(listener.player));
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> refresh(newPlayer));
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (server.getTickCount() % TICK_INTERVAL == 0) {
				for (ServerPlayer player : server.getPlayerList().getPlayers()) {
					equipped(player).forEach(artifact -> artifact.tick(player));
				}
			}
		});
		ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamage, damage, blocked) -> {
			if (entity instanceof ServerPlayer wearer) {
				equipped(wearer).forEach(artifact -> artifact.onHurt(wearer, source, damage));
			}
			if (source.getEntity() instanceof ServerPlayer attacker && attacker != entity) {
				equipped(attacker).forEach(artifact -> artifact.onAttack(attacker, entity, source, damage));
			}
		});
		PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
			if (player instanceof ServerPlayer wearer) {
				equipped(wearer).forEach(artifact -> artifact.onBlockBreak(wearer, pos, state));
			}
		});
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((level, entity, killed, source) -> {
			if (entity instanceof ServerPlayer wearer) {
				equipped(wearer).forEach(artifact -> artifact.onKill(wearer, killed));
			}
		});
	}
}
