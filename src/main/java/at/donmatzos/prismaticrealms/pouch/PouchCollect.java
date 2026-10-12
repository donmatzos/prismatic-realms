package at.donmatzos.prismaticrealms.pouch;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.item.CrystalPouchItem;

/** Auto-collect: picked-up shards and crystals go into the first pouches with room (inventory and hotbar, then off hand). */
public final class PouchCollect {
	private PouchCollect() {
	}

	/** Stores as much of the stack as fits into the player's pouches, shrinking it; returns how many were stored. */
	public static int collect(Player player, ItemStack stack) {
		int slot = PouchItems.slotOf(stack.getItem());
		if (slot < 0) {
			return 0;
		}
		Inventory inventory = player.getInventory();
		int stored = 0;
		for (ItemStack pouch : inventory.getNonEquipmentItems()) {
			stored += store(pouch, slot, stack);
		}
		stored += store(inventory.getItem(Inventory.SLOT_OFFHAND), slot, stack);
		return stored;
	}

	private static int store(ItemStack pouch, int slot, ItemStack stack) {
		if (stack.isEmpty() || !(pouch.getItem() instanceof CrystalPouchItem)) {
			return 0;
		}
		PouchStorage storage = CrystalPouchItem.storage(pouch);
		int inserted = storage.insert(slot, stack.getCount());
		if (inserted > 0) {
			stack.shrink(inserted);
			CrystalPouchItem.save(pouch, storage);
		}
		return inserted;
	}
}
