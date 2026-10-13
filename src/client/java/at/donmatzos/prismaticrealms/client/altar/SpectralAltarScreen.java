package at.donmatzos.prismaticrealms.client.altar;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.altar.SpectralAltarMenu;

/**
 * The Spectral Altar screen: the diamond of recipe slots, the fuel slot and the result. Without enough fuel the result
 * is drawn greyed out and its tooltip says so (the slot itself refuses pickup).
 */
public class SpectralAltarScreen extends AbstractContainerScreen<SpectralAltarMenu> {
	private static final Identifier BACKGROUND = PrismaticRealms.id("textures/gui/container/spectral_altar.png");
	private static final int LOCKED_TINT = 0xB08B8B8B;
	private static final Component NEEDS_FUEL = Component.translatable("container.prismaticrealms.spectral_altar.needs_fuel");

	public SpectralAltarScreen(SpectralAltarMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title, 176, 166);
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		super.extractBackground(graphics, mouseX, mouseY, partialTick);
		graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
	}

	@Override
	protected void extractSlot(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY) {
		super.extractSlot(graphics, slot, mouseX, mouseY);
		if (slot.index == SpectralAltarMenu.RESULT_SLOT && this.menu.resultLocked()) {
			graphics.nextStratum();
			graphics.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, LOCKED_TINT);
		}
	}

	@Override
	protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		if (this.hoveredSlot != null && this.hoveredSlot.index == SpectralAltarMenu.RESULT_SLOT && this.menu.resultLocked() && this.menu.getCarried().isEmpty()) {
			graphics.setTooltipForNextFrame(this.font, NEEDS_FUEL, mouseX, mouseY);
			return;
		}
		super.extractTooltip(graphics, mouseX, mouseY);
	}
}
