package at.donmatzos.prismaticrealms.artifact;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.PrismaticRealms;

/**
 * Server -> client: the entity `entityId` was saved from death by `stack` breaking. Plays the totem animation with that
 * item (vanilla's totem event can only show a totem) for the wearer, with red particles and the totem sound for everyone
 * nearby.
 */
public record ArtifactBreakPayload(int entityId, ItemStack stack) implements CustomPacketPayload {
	public static final Type<ArtifactBreakPayload> TYPE = new Type<>(PrismaticRealms.id("artifact_break"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ArtifactBreakPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, ArtifactBreakPayload::entityId, ItemStack.STREAM_CODEC, ArtifactBreakPayload::stack, ArtifactBreakPayload::new);

	@Override
	public Type<ArtifactBreakPayload> type() {
		return TYPE;
	}

	/** Sent to the wearer and everyone tracking them. */
	public static void broadcast(ServerPlayer wearer, ItemStack stack) {
		ArtifactBreakPayload payload = new ArtifactBreakPayload(wearer.getId(), stack.copy());
		ServerPlayNetworking.send(wearer, payload);
		for (ServerPlayer player : PlayerLookup.tracking(wearer)) {
			ServerPlayNetworking.send(player, payload);
		}
	}

	public static void initialize() {
		PayloadTypeRegistry.clientboundPlay().register(TYPE, STREAM_CODEC);
	}
}
