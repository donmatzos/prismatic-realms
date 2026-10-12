package at.donmatzos.prismaticrealms.artifact;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * The artifact screen's menu: three artifact slots (laid out like the middle three of a hopper) above the player
 * inventory. Shift-click moves an artifact into the first free allowed slot, or out into the inventory.
 */
public class ArtifactMenu extends AbstractContainerMenu {
	private static final int INVENTORY_START = ArtifactRules.SLOTS;
	private static final int INVENTORY_END = INVENTORY_START + 36;
	private final Container artifacts;

	public ArtifactMenu(int containerId, Inventory inventory) {
		this(containerId, inventory, new SimpleContainer(ArtifactRules.SLOTS));
	}

	public ArtifactMenu(int containerId, Inventory inventory, Container artifacts) {
		super(ModArtifactMenus.ARTIFACTS, containerId);
		this.artifacts = artifacts;
		for (int i = 0; i < ArtifactRules.SLOTS; i++) {
			this.addSlot(new ArtifactSlot(artifacts, i, 62 + i * 18, 20));
		}
		this.addStandardInventorySlots(inventory, 8, 51);
	}

	public Container artifacts() {
		return this.artifacts;
	}

	@Override
	public boolean stillValid(Player player) {
		return this.artifacts.stillValid(player) || this.artifacts instanceof SimpleContainer;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		Slot slot = this.slots.get(index);
		if (!slot.hasItem()) {
			return ItemStack.EMPTY;
		}
		ItemStack stack = slot.getItem();
		ItemStack original = stack.copy();
		if (index < INVENTORY_START) {
			if (!this.moveItemStackTo(stack, INVENTORY_START, INVENTORY_END, true)) {
				return ItemStack.EMPTY;
			}
			slot.set(ItemStack.EMPTY);
		} else {
			int target = ArtifactRules.firstFreeSlot(ArtifactSlot.equippedIds(this.artifacts),
					BuiltInRegistries.ITEM.getKey(stack.getItem()).toString(), stack.getItem() instanceof ArtifactItem);
			if (target < 0) {
				return ItemStack.EMPTY;
			}
			this.slots.get(target).set(stack.split(1));
			slot.setChanged();
		}
		return original;
	}
}
