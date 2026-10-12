package at.donmatzos.prismaticrealms.client.artifact;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.artifact.ArtifactBarLayout;
import at.donmatzos.prismaticrealms.artifact.ArtifactItem;
import at.donmatzos.prismaticrealms.artifact.ArtifactSlots;
import at.donmatzos.prismaticrealms.client.ModTooltips;
import at.donmatzos.prismaticrealms.client.mixin.AbstractContainerScreenAccessor;

/**
 * Draws the artifact bar's panel behind its slots (survival inventory, creative inventory tab) and shows the bonus
 * tooltip when the bar's frame (not a slot) is hovered.
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
		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			if (!(screen instanceof InventoryScreen) && !(screen instanceof CreativeModeInventoryScreen)) {
				return;
			}
			AbstractContainerScreenAccessor accessor = (AbstractContainerScreenAccessor) screen;
			ScreenEvents.afterBackground(screen).register((s, graphics, mouseX, mouseY, delta) -> {
				ArtifactBarLayout.Layout layout = layoutFor(s);
				if (layout != null) {
					graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PANEL, accessor.prismaticrealms$getLeftPos() + layout.panelX(),
							accessor.prismaticrealms$getTopPos() + layout.panelY(), ArtifactBarLayout.PANEL_WIDTH, ArtifactBarLayout.PANEL_HEIGHT);
				}
			});
			ScreenEvents.afterExtract(screen).register((s, graphics, mouseX, mouseY, delta) -> {
				ArtifactBarLayout.Layout layout = layoutFor(s);
				int left = accessor.prismaticrealms$getLeftPos(), top = accessor.prismaticrealms$getTopPos();
				if (layout != null && ArtifactBarLayout.contains(layout, mouseX, mouseY, left, top)
						&& !ArtifactBarLayout.overSlot(layout, mouseX, mouseY, left, top)) {
					graphics.setComponentTooltipForNextFrame(Minecraft.getInstance().font, bonusLines(), mouseX, mouseY);
				}
			});
		});
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
