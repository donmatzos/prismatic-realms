package at.donmatzos.prismaticrealms.client.pouch;

import org.joml.Vector2i;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.ScrollWheelHandler;
import net.minecraft.client.gui.ItemSlotMouseAction;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.item.CrystalPouchItem;
import at.donmatzos.prismaticrealms.pouch.SelectPouchEntryPayload;

/** Scrolling over a pouch in any container screen selects one of its entries (like vanilla's BundleMouseActions). */
public class PouchMouseActions implements ItemSlotMouseAction {
	private final ScrollWheelHandler scrollWheelHandler = new ScrollWheelHandler();

	@Override
	public boolean matches(Slot slot) {
		return slot.getItem().getItem() instanceof CrystalPouchItem;
	}

	@Override
	public boolean onMouseScrolled(double scrollX, double scrollY, int slotIndex, ItemStack pouch) {
		int shown = CrystalPouchItem.entries(pouch).size();
		if (shown == 0) {
			return false;
		}
		Vector2i wheelXY = this.scrollWheelHandler.onMouseScroll(scrollX, scrollY);
		int wheel = wheelXY.y == 0 ? -wheelXY.x : wheelXY.y;
		if (wheel != 0) {
			int selected = CrystalPouchItem.selected(pouch);
			int next = ScrollWheelHandler.getNextScrollWheelSelection(wheel, selected, shown);
			if (next != selected) {
				this.select(pouch, slotIndex, next);
			}
		}
		return true;
	}

	@Override
	public void onStopHovering(Slot slot) {
		this.select(slot.getItem(), slot.index, -1);
	}

	@Override
	public void onSlotClicked(Slot slot, ContainerInput input) {
		if (input == ContainerInput.QUICK_MOVE || input == ContainerInput.SWAP) {
			this.select(slot.getItem(), slot.index, -1);
		}
	}

	private void select(ItemStack pouch, int slotIndex, int selected) {
		if (pouch.getItem() instanceof CrystalPouchItem) {
			CrystalPouchItem.select(pouch, selected);
			ClientPlayNetworking.send(new SelectPouchEntryPayload(slotIndex, selected));
		}
	}
}
