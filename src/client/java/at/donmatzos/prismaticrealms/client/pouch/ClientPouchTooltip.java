package at.donmatzos.prismaticrealms.client.pouch;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.pouch.PouchText;
import at.donmatzos.prismaticrealms.pouch.PouchTooltip;

/** Draws a pouch's entries in a grid (up to 7 per row) with counts in the slot-fitting font; the selection is highlighted. */
public class ClientPouchTooltip implements ClientTooltipComponent {
	private static final Identifier SLOT = Identifier.withDefaultNamespace("container/bundle/slot_background");
	private static final Identifier SELECTED = Identifier.withDefaultNamespace("container/bundle/slot_highlight_back");
	private static final int CELL = 24;
	private static final int PER_ROW = 7;
	private final PouchTooltip data;

	public ClientPouchTooltip(PouchTooltip data) {
		this.data = data;
	}

	private int rows() {
		return (this.data.entries().size() + PER_ROW - 1) / PER_ROW;
	}

	@Override
	public int getHeight(Font font) {
		return this.rows() * CELL + 4;
	}

	@Override
	public int getWidth(Font font) {
		return Math.min(this.data.entries().size(), PER_ROW) * CELL;
	}

	@Override
	public void extractImage(Font font, int x, int y, int width, int height, GuiGraphicsExtractor graphics) {
		for (int i = 0; i < this.data.entries().size(); i++) {
			int cx = x + (i % PER_ROW) * CELL, cy = y + (i / PER_ROW) * CELL;
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, i == this.data.selected() ? SELECTED : SLOT, cx, cy, CELL, CELL);
			ItemStack stack = this.data.entries().get(i);
			graphics.item(stack, cx + 4, cy + 4, i);
			String label = PouchText.label(stack.getCount());
			if (!label.isEmpty()) {
				float scale = PouchText.scale(stack.getCount());
				graphics.pose().pushMatrix();
				graphics.pose().translate(cx + 21, cy + 21);
				graphics.pose().scale(scale, scale);
				graphics.text(font, label, -font.width(label), -font.lineHeight + 1, 0xFFFFFFFF, true);
				graphics.pose().popMatrix();
			}
		}
	}
}
