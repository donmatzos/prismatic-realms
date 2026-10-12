package at.donmatzos.prismaticrealms.client.pouch;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.pouch.CrystalPouchMenu;
import at.donmatzos.prismaticrealms.pouch.PouchItems;
import at.donmatzos.prismaticrealms.pouch.PouchSlot;
import at.donmatzos.prismaticrealms.pouch.PouchStorage;
import at.donmatzos.prismaticrealms.pouch.PouchText;

/**
 * The Crystal Pouch screen: two rows of seven family slots above the player inventory. Empty slots show the item's grey
 * outline (PouchSlot#getNoItemIcon, drawn by vanilla); filled slots draw the item and its count, in a smaller font from
 * three digits so "2048" stays inside the slot. Hovering a slot shows "Name: amount / capacity".
 */
public class CrystalPouchScreen extends AbstractContainerScreen<CrystalPouchMenu> {
	private static final Identifier BACKGROUND = PrismaticRealms.id("textures/gui/container/crystal_pouch.png");

	public CrystalPouchScreen(CrystalPouchMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title, 176, 150);
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		super.extractBackground(graphics, mouseX, mouseY, partialTick);
		graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
	}

	@Override
	protected void extractSlot(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY) {
		if (!(slot instanceof PouchSlot pouchSlot) || !slot.hasItem()) {
			super.extractSlot(graphics, slot, mouseX, mouseY);
			return;
		}
		graphics.item(slot.getItem(), slot.x, slot.y, slot.x + slot.y * this.imageWidth);
		int count = this.menu.count(pouchSlot.pouchIndex());
		String label = PouchText.label(count);
		if (label.isEmpty()) {
			return;
		}
		float scale = PouchText.scale(count);
		graphics.pose().pushMatrix();
		graphics.pose().translate(slot.x + 17, slot.y + 17);
		graphics.pose().scale(scale, scale);
		graphics.text(this.font, label, -this.font.width(label), -this.font.lineHeight + 1, 0xFFFFFFFF, true);
		graphics.pose().popMatrix();
	}

	@Override
	protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		if (this.hoveredSlot instanceof PouchSlot pouchSlot && this.menu.getCarried().isEmpty()) {
			int index = pouchSlot.pouchIndex();
			Component amount = Component.translatable("container.prismaticrealms.crystal_pouch.amount",
					new ItemStack(PouchItems.item(index)).getHoverName(), PouchText.grouped(this.menu.count(index)),
					PouchText.grouped(PouchStorage.capacity(index, this.menu.tier())));
			graphics.setTooltipForNextFrame(this.font, amount, mouseX, mouseY);
			return;
		}
		super.extractTooltip(graphics, mouseX, mouseY);
	}
}
