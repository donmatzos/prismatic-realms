package at.donmatzos.prismaticrealms.client.artifact;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.artifact.ArtifactBarLayout;
import at.donmatzos.prismaticrealms.artifact.ArtifactBarState;
import at.donmatzos.prismaticrealms.artifact.ArtifactItem;
import at.donmatzos.prismaticrealms.artifact.ArtifactSlots;
import at.donmatzos.prismaticrealms.client.ModTooltips;
import at.donmatzos.prismaticrealms.client.mixin.AbstractContainerScreenAccessor;
import at.donmatzos.prismaticrealms.client.mixin.AbstractRecipeBookScreenAccessor;

/**
 * Draws the artifact bar's panel behind its slots (survival inventory, creative inventory tab), shows the bonus tooltip
 * when the bar's frame (not a slot) is hovered, and adds the button that opens and closes the bar. The bar sits left of the
 * window, like the recipe book, and the two exclude each other.
 */
public final class ArtifactBar {
	private static final Identifier PANEL = PrismaticRealms.id("container/artifacts/bar");

	private ArtifactBar() {
	}

	private static ArtifactBarLayout.Layout layoutFor(Screen screen) {
		if (screen instanceof InventoryScreen) {
			return ArtifactBarLayout.SURVIVAL;
		}
		if (screen instanceof CreativeModeInventoryScreen creative && creative.isInventoryOpen()) {
			return ArtifactBarLayout.CREATIVE;
		}
		return null;
	}

	public static void initialize() {
		ArtifactBarToggle.load();
		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			if (!(screen instanceof InventoryScreen) && !(screen instanceof CreativeModeInventoryScreen)) {
				return;
			}
			AbstractContainerScreenAccessor accessor = (AbstractContainerScreenAccessor) screen;
			ArtifactToggleButton toggle = new ArtifactToggleButton(0, 0, button -> toggle(screen, accessor));
			Screens.getWidgets(screen).add(toggle);
			if (recipeBookOf(screen) != null && recipeBookOf(screen).isVisible()) {
				ArtifactBarToggle.setOpen(false);   // a recipe book that is open wins
			}
			ScreenEvents.beforeExtract(screen).register((s, graphics, mouseX, mouseY, delta) -> placeToggle(toggle, s, accessor));
			ScreenEvents.afterBackground(screen).register((s, graphics, mouseX, mouseY, delta) -> {
				ArtifactBarLayout.Layout layout = layoutFor(s);
				if (layout != null && ArtifactBarState.isOpen()) {
					graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PANEL, accessor.prismaticrealms$getLeftPos() + layout.panelX(),
							accessor.prismaticrealms$getTopPos() + layout.panelY(), ArtifactBarLayout.PANEL_WIDTH, ArtifactBarLayout.PANEL_HEIGHT);
				}
			});
			ScreenEvents.afterExtract(screen).register((s, graphics, mouseX, mouseY, delta) -> {
				ArtifactBarLayout.Layout layout = layoutFor(s);
				int left = accessor.prismaticrealms$getLeftPos(), top = accessor.prismaticrealms$getTopPos();
				if (layout != null && ArtifactBarState.isOpen() && ArtifactBarLayout.contains(layout, mouseX, mouseY, left, top)
						&& !ArtifactBarLayout.overSlot(layout, mouseX, mouseY, left, top)) {
					graphics.setComponentTooltipForNextFrame(Minecraft.getInstance().font, bonusLines(), mouseX, mouseY);
				}
			});
		});
	}

	private static RecipeBookComponent<?> recipeBookOf(Screen screen) {
		return screen instanceof AbstractRecipeBookScreen<?> ? ((AbstractRecipeBookScreenAccessor) screen).prismaticrealms$getRecipeBookComponent() : null;
	}

	/** Survival: on the recipe book button's row, to its right. Creative: in the free area right of the player model. */
	private static void placeToggle(ArtifactToggleButton toggle, Screen screen, AbstractContainerScreenAccessor accessor) {
		ArtifactBarLayout.Layout layout = layoutFor(screen);
		toggle.visible = layout != null;
		int left = accessor.prismaticrealms$getLeftPos(), top = accessor.prismaticrealms$getTopPos();
		if (layout == ArtifactBarLayout.SURVIVAL) {
			toggle.setPosition(left + 128, screen.height / 2 - 22);
		} else if (layout == ArtifactBarLayout.CREATIVE) {
			toggle.setPosition(left + 126, top + 6);
		}
	}

	/** Opening the bar closes an open recipe book (and moves the window and the recipe button back, like its own button does). */
	private static void toggle(Screen screen, AbstractContainerScreenAccessor accessor) {
		boolean open = !ArtifactBarState.isOpen();
		ArtifactBarToggle.setOpen(open);
		RecipeBookComponent<?> recipeBook = recipeBookOf(screen);
		if (open && recipeBook != null && recipeBook.isVisible()) {
			recipeBook.toggleVisibility();
			accessor.prismaticrealms$setLeftPos(recipeBook.updateScreenPosition(screen.width, accessor.prismaticrealms$getImageWidth()));
			for (AbstractWidget widget : Screens.getWidgets(screen)) {
				if (widget instanceof ImageButton recipeButton && !(widget instanceof ArtifactToggleButton) && widget.getWidth() == 20 && widget.getHeight() == 18) {
					recipeButton.setPosition(accessor.prismaticrealms$getLeftPos() + 104, screen.height / 2 - 22);
				}
			}
		}
	}

	private static List<Component> bonusLines() {
		List<Component> lines = new ArrayList<>();
		lines.add(Component.translatable("container.prismaticrealms.artifacts"));
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.player != null) {
			for (ItemStack stack : ArtifactSlots.of(minecraft.player).stacks()) {
				if (stack.getItem() instanceof ArtifactItem artifact) {
					lines.addAll(ModTooltips.wrap(List.of(new ModTooltips.Segment(Component.translatable(artifact.bonusKey()).getString(),
							Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE)))));
				}
			}
		}
		if (lines.size() == 1) {
			lines.add(Component.translatable("container.prismaticrealms.artifacts.none").withStyle(ChatFormatting.GRAY));
		}
		return lines;
	}
}
