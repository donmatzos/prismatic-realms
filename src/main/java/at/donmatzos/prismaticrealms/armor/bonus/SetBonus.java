package at.donmatzos.prismaticrealms.armor.bonus;

import java.util.function.BiConsumer;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import at.donmatzos.prismaticrealms.artifact.ArtifactItem;

/** A family's armor set bonus. All hooks run on the server, only while the wearer has that family's set. */
public interface SetBonus {
	/** Every server tick. Keep it cheap. */
	default void tick(ServerPlayer wearer) {
	}

	/** The wearer was hurt (after damage). */
	default void onHurt(ServerPlayer wearer, DamageSource source, float damage) {
	}

	/** The wearer's melee attack is about to deal `amount` to `target`; returns the new amount. */
	default float modifyAttackDamage(ServerPlayer attacker, LivingEntity target, DamageSource source, float amount) {
		return amount;
	}

	/** Attribute modifiers while the set is worn; use ArmorSetEffects.modifierId for ids. */
	default void addModifiers(BiConsumer<Holder<Attribute>, AttributeModifier> out) {
	}

	/** The set stopped being worn (piece removed, logout). Drop any per-player state. */
	default void onDeactivate(ServerPlayer wearer) {
	}

	/** True when this artifact, having just saved the wearer from death, stays in its slot. */
	default boolean keepsArtifact(ServerPlayer wearer, ArtifactItem artifact) {
		return false;
	}
}
