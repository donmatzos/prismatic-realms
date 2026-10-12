package at.donmatzos.prismaticrealms.pouch;

import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.PrismaticRealms;

/**
 * A display-only slot of the pouch screen: shows its item (count 1) while the pouch holds any, otherwise the item's grey
 * outline. All clicks are handled by CrystalPouchMenu#clicked; vanilla never moves items through this slot.
 */
public class PouchSlot extends Slot {
	private final CrystalPouchMenu menu;
	private final int pouchIndex;

	public PouchSlot(CrystalPouchMenu menu, int pouchIndex, int x, int y) {
		super(new SimpleContainer(1), 0, x, y);
		this.menu = menu;
		this.pouchIndex = pouchIndex;
	}

	public int pouchIndex() {
		return this.pouchIndex;
	}

	@Override
	public ItemStack getItem() {
		return this.menu.count(this.pouchIndex) > 0 ? new ItemStack(PouchItems.item(this.pouchIndex)) : ItemStack.EMPTY;
	}

	@Override
	public boolean hasItem() {
		return this.menu.count(this.pouchIndex) > 0;
	}

	@Override
	public void set(ItemStack stack) {
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}

	@Override
	public boolean mayPickup(Player player) {
		return false;
	}

	@Override
	public Identifier getNoItemIcon() {
		return PrismaticRealms.id("container/crystal_pouch/" + BuiltInRegistries.ITEM.getKey(PouchItems.item(this.pouchIndex)).getPath());
	}
}
