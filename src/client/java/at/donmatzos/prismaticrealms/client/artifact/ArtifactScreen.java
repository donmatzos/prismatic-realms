package at.donmatzos.prismaticrealms.client.artifact;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.artifact.ArtifactItem;
import at.donmatzos.prismaticrealms.artifact.ArtifactMenu;
import at.donmatzos.prismaticrealms.artifact.ArtifactRules;
import at.donmatzos.prismaticrealms.client.ModTooltips;

/**
 * The artifact screen: three artifact slots (hopper-style) above the player inventory, and beside the GUI a panel
 * listing the equipped artifacts' bonuses (word-wrapped like the mod's tooltips).
 */
public class ArtifactScreen extends AbstractContainerScreen<ArtifactMenu> {
	private static final Identifier BACKGROUND = PrismaticRealms.id("textures/gui/container/artifacts.png");
	private static final int PANEL_GAP = 4;
	private static final int PANEL_PADDING = 4;

	public ArtifactScreen(ArtifactMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title, 176, 133);
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		super.extractBackground(graphics, mouseX, mouseY, partialTick);
		graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
		this.extractBonusPanel(graphics);
	}

	private List<Component> bonusLines() {
		List<Component> lines = new ArrayList<>();
		for (int i = 0; i < ArtifactRules.SLOTS; i++) {
			ItemStack stack = this.menu.artifacts().getItem(i);
			if (stack.getItem() instanceof ArtifactItem artifact) {
				lines.addAll(ModTooltips.wrap(List.of(new ModTooltips.Segment(Component.translatable(artifact.bonusKey()).getString(),
						Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE)))));
			}
		}
		if (lines.isEmpty()) {
			lines.add(Component.translatable("container.prismaticrealms.artifacts.none").withStyle(ChatFormatting.GRAY));
		}
		return lines;
	}

	private void extractBonusPanel(GuiGraphicsExtractor graphics) {
		List<Component> lines = this.bonusLines();
		int width = 0;
		for (Component line : lines) {
			width = Math.max(width, this.font.width(line));
		}
		int x = this.leftPos + this.imageWidth + PANEL_GAP;
		int y = this.topPos;
		int height = lines.size() * this.font.lineHeight + 2 * PANEL_PADDING;
		graphics.fill(x, y, x + width + 2 * PANEL_PADDING, y + height, 0xC0101018);
		for (int i = 0; i < lines.size(); i++) {
			graphics.text(this.font, lines.get(i), x + PANEL_PADDING, y + PANEL_PADDING + i * this.font.lineHeight, 0xFFFFFFFF, true);
		}
	}
}
