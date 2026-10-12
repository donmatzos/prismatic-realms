package at.donmatzos.prismaticrealms.client.artifact;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.artifact.OpenArtifactsPayload;

/** The "Open Artifacts" key (K by default) in the mod's own controls category; works when no screen is open. */
public final class ArtifactKeys {
	private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(PrismaticRealms.id("prismaticrealms"));
	public static final KeyMapping OPEN = KeyMappingHelper.registerKeyMapping(
			new KeyMapping("key.prismaticrealms.artifacts", InputConstants.KEY_K, CATEGORY));

	private ArtifactKeys() {
	}

	public static void initialize() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (OPEN.consumeClick()) {
				if (client.gui.screen() == null && client.player != null) {
					ClientPlayNetworking.send(OpenArtifactsPayload.INSTANCE);
				}
			}
		});
	}
}
