package at.donmatzos.prismaticrealms.armor;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.armor.bonus.DeepDiver;
import at.donmatzos.prismaticrealms.armor.bonus.FrostbiteMantle;
import at.donmatzos.prismaticrealms.armor.bonus.Photosynthesis;
import at.donmatzos.prismaticrealms.armor.bonus.SanguineVessel;
import at.donmatzos.prismaticrealms.armor.bonus.SetBonus;
import at.donmatzos.prismaticrealms.armor.bonus.SolarMantle;
import at.donmatzos.prismaticrealms.armor.bonus.StaticCharge;
import at.donmatzos.prismaticrealms.artifact.ArtifactItem;
import at.donmatzos.prismaticrealms.family.CrystalFamily;

/**
 * Applies the armor set bonuses. Every server tick the wearer's set is recomputed; when it changes, the old set's
 * attribute modifiers are removed and the new one's added (modifiers are transient, so join and respawn forget the
 * tracked set and the next tick re-adds them). The other hooks are dispatched from Fabric events and the damage mixin.
 */
public final class ArmorSetEffects {
	private static final Map<CrystalFamily, SetBonus> BONUSES = new EnumMap<>(CrystalFamily.class);
	private static final Map<UUID, CrystalFamily> ACTIVE = new ConcurrentHashMap<>();

	private ArmorSetEffects() {
	}

	public static void register(CrystalFamily family, SetBonus bonus) {
		BONUSES.put(family, bonus);
	}

	public static Identifier modifierId(CrystalFamily family, String name) {
		return PrismaticRealms.id("armor_set/" + family.id() + "/" + name);
	}

	private static void swap(ServerPlayer player, CrystalFamily from, CrystalFamily to) {
		SetBonus before = from == null ? null : BONUSES.get(from);
		if (before != null) {
			before.addModifiers((attribute, modifier) -> {
				AttributeInstance instance = player.getAttribute(attribute);
				if (instance != null) {
					instance.removeModifier(modifier.id());
				}
			});
			before.onDeactivate(player);
		}
		SetBonus after = to == null ? null : BONUSES.get(to);
		if (after != null) {
			after.addModifiers((attribute, modifier) -> {
				AttributeInstance instance = player.getAttribute(attribute);
				if (instance != null) {
					instance.removeModifier(modifier.id());
					instance.addTransientModifier(modifier);
				}
			});
		}
	}

	/** True when this wearer's set lets the just-saving artifact stay in its slot (the Sanguis set's Chalice roll). */
	public static boolean keepsArtifact(ServerPlayer wearer, ArtifactItem artifact) {
		CrystalFamily family = ArmorSets.of(wearer);
		SetBonus bonus = family == null ? null : BONUSES.get(family);
		return bonus != null && bonus.keepsArtifact(wearer, artifact);
	}

	/** Called by the damage mixin for every hurtServer: changes the amount of a player's melee hit when they wear a set. */
	public static float modifyAttackDamage(LivingEntity target, DamageSource source, float amount) {
		if (!source.is(DamageTypes.PLAYER_ATTACK) || !(source.getDirectEntity() instanceof ServerPlayer attacker)) {
			return amount;
		}
		CrystalFamily family = ArmorSets.of(attacker);
		SetBonus bonus = family == null ? null : BONUSES.get(family);
		return bonus == null ? amount : bonus.modifyAttackDamage(attacker, target, source, amount);
	}

	public static void initialize() {
		register(CrystalFamily.CRUEL_SUN, new SolarMantle());
		register(CrystalFamily.STORM_FANG, new StaticCharge());
		register(CrystalFamily.ICE_STAR, new FrostbiteMantle());
		register(CrystalFamily.OVERGROWN_PROSPERITY, new Photosynthesis());
		register(CrystalFamily.CERULEAN_ABYSS, new DeepDiver());
		register(CrystalFamily.CRIMSON_SANGUIS, new SanguineVessel());
		ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> ACTIVE.remove(listener.player.getUUID()));
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> ACTIVE.remove(newPlayer.getUUID()));
		ServerPlayConnectionEvents.DISCONNECT.register((listener, server) -> {
			CrystalFamily family = ACTIVE.remove(listener.player.getUUID());
			if (family != null && BONUSES.get(family) != null) {
				BONUSES.get(family).onDeactivate(listener.player);
			}
		});
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				CrystalFamily now = ArmorSets.of(player);
				CrystalFamily before = ACTIVE.get(player.getUUID());
				if (now != before) {
					swap(player, before, now);
					if (now == null) {
						ACTIVE.remove(player.getUUID());
					} else {
						ACTIVE.put(player.getUUID(), now);
					}
				}
				SetBonus bonus = now == null ? null : BONUSES.get(now);
				if (bonus != null) {
					bonus.tick(player);
				}
			}
		});
		ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamage, damage, blocked) -> {
			if (entity instanceof ServerPlayer wearer) {
				CrystalFamily family = ArmorSets.of(wearer);
				SetBonus bonus = family == null ? null : BONUSES.get(family);
				if (bonus != null) {
					bonus.onHurt(wearer, source, damage);
				}
			}
		});
	}
}
