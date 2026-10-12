package at.donmatzos.prismaticrealms.artifact;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.PrismaticRealms;

/**
 * A player's three artifact slots, stored as an immutable value in a Fabric data attachment: saved with the player,
 * synced to that player's client, copied to the respawned player (the death drop clears it first when the inventory
 * drops, so only keepInventory carries artifacts over). Always replace the value (set), never mutate its stacks.
 */
public record ArtifactSlots(List<ItemStack> stacks) {
	public static final ArtifactSlots EMPTY = new ArtifactSlots(List.of());
	public static final Codec<ArtifactSlots> CODEC = ItemStack.OPTIONAL_CODEC.listOf().xmap(ArtifactSlots::new, ArtifactSlots::stacks);
	public static final StreamCodec<RegistryFriendlyByteBuf, ArtifactSlots> STREAM_CODEC =
			ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list(ArtifactRules.SLOTS)).map(ArtifactSlots::new, ArtifactSlots::stacks);
	public static final AttachmentType<ArtifactSlots> TYPE = AttachmentRegistry.<ArtifactSlots>builder()
			.persistent(CODEC)
			.copyOnDeath()
			.syncWith(STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
			.buildAndRegister(PrismaticRealms.id("artifact_slots"));

	public ArtifactSlots {
		List<ItemStack> fixed = new ArrayList<>();
		for (int i = 0; i < ArtifactRules.SLOTS; i++) {
			fixed.add(i < stacks.size() ? stacks.get(i).copy() : ItemStack.EMPTY);
		}
		stacks = List.copyOf(fixed);
	}

	public static void initialize() {
	}

	public static ArtifactSlots of(Player player) {
		ArtifactSlots slots = player.getAttached(TYPE);
		return slots == null ? EMPTY : slots;
	}

	public static void set(Player player, ArtifactSlots slots) {
		player.setAttached(TYPE, slots);
	}

	public ItemStack get(int slot) {
		return this.stacks.get(slot);
	}

	public ArtifactSlots with(int slot, ItemStack stack) {
		List<ItemStack> copy = new ArrayList<>(this.stacks);
		copy.set(slot, stack.copy());
		return new ArtifactSlots(copy);
	}
}
