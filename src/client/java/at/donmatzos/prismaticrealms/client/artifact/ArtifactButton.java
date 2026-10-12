package at.donmatzos.prismaticrealms.client.artifact;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.artifact.OpenArtifactsPayload;
import at.donmatzos.prismaticrealms.client.mixin.AbstractContainerScreenAccessor;

/**
 * The artifact button: in the survival inventory screen right of the recipe-book button, and in the creative inventory
 * on the "Survival Inventory" tab (see addCreativeButton). Survival: right of the recipe-book button (vanilla puts that at
 * leftPos + 104, height / 2 - 22). leftPos moves when the recipe book opens, so the button follows it every frame.
 */
public final class ArtifactButton {
	private static final WidgetSprites SPRITES = new WidgetSprites(PrismaticRealms.id("artifacts/button"), PrismaticRealms.id("artifacts/button_highlighted"));
	private static final int OFFSET_X = 104 + 22;
	// the off-hand-mirroring cell in the creative inventory tab: slot at (127, 20), its frame from (126, 19); button 20x18
	private static final int CREATIVE_X = 126 - 1;
	private static final int CREATIVE_Y = 19;

	private ArtifactButton() {
	}

	public static void initialize() {
		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			if (screen instanceof InventoryScreen inventory) {
				addSurvivalButton(inventory, height);
			} else if (screen instanceof CreativeModeInventoryScreen creative) {
				addCreativeButton(creative);
			}
		});
	}

	private static ImageButton button(int x, int y) {
		return new ImageButton(x, y, 20, 18, SPRITES, b -> ClientPlayNetworking.send(OpenArtifactsPayload.INSTANCE),
				Component.translatable("container.prismaticrealms.artifacts"));
	}

	private static void addSurvivalButton(InventoryScreen screen, int height) {
		AbstractContainerScreenAccessor accessor = (AbstractContainerScreenAccessor) screen;
		ImageButton button = button(accessor.prismaticrealms$getLeftPos() + OFFSET_X, height / 2 - 22);
		Screens.getWidgets(screen).add(button);
		ScreenEvents.beforeExtract(screen).register((s, graphics, mouseX, mouseY, delta) ->
				button.setX(accessor.prismaticrealms$getLeftPos() + OFFSET_X));
	}

	/**
	 * Creative inventory: only on the "Survival Inventory" tab, in the free spot that mirrors the off-hand slot (the off-hand
	 * slot sits left of the left armour column at x 35, so its mirror right of the right armour column is x 127; y 20 like
	 * the off-hand slot). The 20x18 button is placed over that 18x18 cell, one pixel wider on each side.
	 */
	private static void addCreativeButton(CreativeModeInventoryScreen screen) {
		AbstractContainerScreenAccessor accessor = (AbstractContainerScreenAccessor) screen;
		ImageButton button = button(accessor.prismaticrealms$getLeftPos() + CREATIVE_X, accessor.prismaticrealms$getTopPos() + CREATIVE_Y);
		button.visible = screen.isInventoryOpen();
		Screens.getWidgets(screen).add(button);
		ScreenEvents.beforeExtract(screen).register((s, graphics, mouseX, mouseY, delta) -> {
			button.visible = screen.isInventoryOpen();
			button.setPosition(accessor.prismaticrealms$getLeftPos() + CREATIVE_X, accessor.prismaticrealms$getTopPos() + CREATIVE_Y);
		});
	}
}
