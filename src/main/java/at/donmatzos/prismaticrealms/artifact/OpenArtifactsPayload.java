package at.donmatzos.prismaticrealms.artifact;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;

import at.donmatzos.prismaticrealms.PrismaticRealms;

/** Client -> server: open my artifact screen (sent by the inventory button and the key). */
public record OpenArtifactsPayload() implements CustomPacketPayload {
	public static final OpenArtifactsPayload INSTANCE = new OpenArtifactsPayload();
	public static final Type<OpenArtifactsPayload> TYPE = new Type<>(PrismaticRealms.id("open_artifacts"));
	public static final StreamCodec<RegistryFriendlyByteBuf, OpenArtifactsPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	@Override
	public Type<OpenArtifactsPayload> type() {
		return TYPE;
	}

	public static void initialize() {
		PayloadTypeRegistry.serverboundPlay().register(TYPE, STREAM_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(TYPE, (payload, context) -> {
			ServerPlayer player = context.player();
			if (!player.isAlive() || player.isSpectator()) {
				return;
			}
			player.openMenu(new SimpleMenuProvider((id, inventory, opener) -> new ArtifactMenu(id, inventory, new ArtifactContainer(player)),
					Component.translatable("container.prismaticrealms.artifacts")));
		});
	}
}
