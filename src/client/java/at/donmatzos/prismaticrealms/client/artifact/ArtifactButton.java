package at.donmatzos.prismaticrealms.client.artifact;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.artifact.OpenArtifactsPayload;
import at.donmatzos.prismaticrealms.client.mixin.AbstractContainerScreenAccessor;

/**
 * The artifact button in the survival inventory screen, right of the recipe-book button (vanilla puts that at
 * leftPos + 104, height / 2 - 22). leftPos moves when the recipe book opens, so the button follows it every frame.
 */
public final class ArtifactButton {
	private static final WidgetSprites SPRITES = new WidgetSprites(PrismaticRealms.id("artifacts/button"), PrismaticRealms.id("artifacts/button_highlighted"));
	private static final int OFFSET_X = 104 + 22;

	private ArtifactButton() {
	}

	public static void initialize() {
		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			if (!(screen instanceof InventoryScreen inventory)) {
				return;
			}
			AbstractContainerScreenAccessor accessor = (AbstractContainerScreenAccessor) inventory;
			ImageButton button = new ImageButton(accessor.prismaticrealms$getLeftPos() + OFFSET_X, height / 2 - 22, 20, 18, SPRITES,
					b -> ClientPlayNetworking.send(OpenArtifactsPayload.INSTANCE), Component.translatable("container.prismaticrealms.artifacts"));
			Screens.getWidgets(screen).add(button);
			ScreenEvents.beforeExtract(screen).register((s, graphics, mouseX, mouseY, delta) ->
					button.setX(accessor.prismaticrealms$getLeftPos() + OFFSET_X));
		});
	}
}
