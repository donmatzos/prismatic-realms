package at.donmatzos.prismaticrealms.artifact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import at.donmatzos.prismaticrealms.PrismaticRealms;

/**
 * Applies equipped artifacts' bonuses. Attribute modifiers are transient and rebuilt by refresh() whenever the slots
 * change, on join and on respawn (every artifact modifier is removed first, so nothing doubles or lingers). The other
 * hooks are dispatched from Fabric events; `tick` runs once a second, `tickMovement` every tick.
 */
public final class ArtifactEffects {
	private static final int TICK_INTERVAL = 20;
	// Vanilla resets the attack charge before the damage event, so it is read when the attack starts and kept per player
	// (the same approach as ChargedHitScytheItem).
	private static final Map<UUID, Boolean> FULLY_CHARGED = new ConcurrentHashMap<>();
	private static final Identifier AFTER_ATTACHMENTS = PrismaticRealms.id("after_attachments");

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

	/**
	 * The first equipped artifact that saves from death breaks: it leaves its slot, its modifiers go, and the wearer is
	 * healed to the new full health. Returns true when death was prevented.
	 */
	private static boolean breakForDeath(ServerPlayer wearer, DamageSource source) {
		if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			return false;
		}
		ArtifactSlots slots = ArtifactSlots.of(wearer);
		for (int i = 0; i < ArtifactRules.SLOTS; i++) {
			ItemStack stack = slots.get(i);
			if (stack.getItem() instanceof ArtifactItem artifact && artifact.savesFromDeath(wearer, source)) {
				ArtifactSlots.set(wearer, slots.with(i, ItemStack.EMPTY));
				refresh(wearer);
				wearer.setHealth(wearer.getMaxHealth());
				ArtifactBreakPayload.broadcast(wearer, stack);
				return true;
			}
		}
		return false;
	}

	public static void initialize() {
		ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> refresh(listener.player));
		// Fabric copies the attachment (copyOnDeath) in its own AFTER_RESPAWN listener in the default phase; listener order
		// within a phase follows mod load order (shuffled in dev), so refresh in a phase that runs after the default one.
		ServerPlayerEvents.AFTER_RESPAWN.addPhaseOrdering(Event.DEFAULT_PHASE, AFTER_ATTACHMENTS);
		ServerPlayerEvents.AFTER_RESPAWN.register(AFTER_ATTACHMENTS, (oldPlayer, newPlayer, alive) -> refresh(newPlayer));
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			boolean everySecond = server.getTickCount() % TICK_INTERVAL == 0;
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				for (ArtifactItem artifact : equipped(player)) {
					artifact.tickMovement(player);
					if (everySecond) {
						artifact.tick(player);
					}
				}
			}
		});
		AttackEntityCallback.EVENT.register((player, level, hand, target, hitResult) -> {
			if (!level.isClientSide()) {
				FULLY_CHARGED.put(player.getUUID(), player.getAttackStrengthScale(0.5F) > 0.9F);
			}
			return InteractionResult.PASS;
		});
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) ->
				!(entity instanceof ServerPlayer wearer) || equipped(wearer).stream().noneMatch(artifact -> artifact.preventsDamage(wearer, source)));
		// vanilla checks held totems before die(), so a held Totem of Undying is used up first
		ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, amount) ->
				!(entity instanceof ServerPlayer wearer) || !breakForDeath(wearer, source));
		ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamage, damage, blocked) -> {
			if (entity instanceof ServerPlayer wearer) {
				equipped(wearer).forEach(artifact -> artifact.onHurt(wearer, source, damage));
			}
			if (source.getEntity() instanceof ServerPlayer attacker && attacker != entity) {
				equipped(attacker).forEach(artifact -> artifact.onAttack(attacker, entity, source, damage));
				if (source.is(DamageTypes.PLAYER_ATTACK) && source.getDirectEntity() == attacker && Boolean.TRUE.equals(FULLY_CHARGED.remove(attacker.getUUID()))) {
					equipped(attacker).forEach(artifact -> artifact.onChargedHit(attacker, entity));
				}
			}
		});
		// block loot only: its context carries the broken block's state, and THIS_ENTITY is whoever broke it
		LootTableEvents.MODIFY_DROPS.register((table, context, drops) -> {
			if (context.hasParameter(LootContextParams.BLOCK_STATE) && context.getOptional(LootContextParams.THIS_ENTITY) instanceof ServerPlayer wearer) {
				equipped(wearer).forEach(artifact -> artifact.modifyBlockDrops(wearer, context, drops));
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
