package at.donmatzos.prismaticrealms.pouch;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.item.CrystalPouchItem;

/**
 * The Crystal Pouch screen's menu: 14 display-only pouch slots (2 rows x 7, shards on top) above the player inventory.
 * The pouch stays in the player's hand; its counts are synced as data slots (shorts, enough for 2048). Every click on a
 * pouch slot is handled on the server here; the client only waits for the synced result. The held pouch can't be moved
 * while the screen is open.
 */
public class CrystalPouchMenu extends AbstractContainerMenu {
	public static final int POUCH_SLOTS = PouchStorage.SLOTS;
	private static final int INVENTORY_START = POUCH_SLOTS;          // 27 main inventory slots
	private static final int HOTBAR_START = INVENTORY_START + 27;     // 9 hotbar slots
	private static final int OFFHAND_BUTTON = 40;                     // number-key swap button for the off hand

	private final Player player;
	private final InteractionHand hand;
	private final ContainerData data;

	public CrystalPouchMenu(int containerId, Inventory inventory, InteractionHand hand) {
		super(ModMenus.CRYSTAL_POUCH, containerId);
		this.player = inventory.player;
		this.hand = hand;
		this.data = inventory.player.level().isClientSide() ? new SimpleContainerData(POUCH_SLOTS) : new ContainerData() {
			@Override
			public int get(int index) {
				return CrystalPouchItem.storage(CrystalPouchMenu.this.pouch()).count(index);
			}

			@Override
			public void set(int index, int value) {
			}

			@Override
			public int getCount() {
				return POUCH_SLOTS;
			}
		};
		for (int i = 0; i < POUCH_SLOTS; i++) {
			this.addSlot(new PouchSlot(this, i, 26 + (i % PouchStorage.FAMILIES) * 18, 18 + (i / PouchStorage.FAMILIES) * 18));
		}
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 67 + row * 18));
			}
		}
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(inventory, col, 8 + col * 18, 125));
		}
		this.addDataSlots(this.data);
	}

	public ItemStack pouch() {
		return this.player.getItemInHand(this.hand);
	}

	public int count(int slot) {
		return this.data.get(slot);
	}

	public int tier() {
		return this.pouch().getItem() instanceof CrystalPouchItem item ? item.tier() : 0;
	}

	@Override
	public boolean stillValid(Player player) {
		return this.pouch().getItem() instanceof CrystalPouchItem;
	}

	/** Clicks that would move the held pouch itself (its hotbar slot, number-key swaps onto it, throwing it). */
	private boolean isLockedClick(int slotId, int button, ContainerInput input) {
		if (this.hand == InteractionHand.MAIN_HAND) {
			int held = this.player.getInventory().getSelectedSlot();
			return slotId == HOTBAR_START + held || (input == ContainerInput.SWAP && button == held);
		}
		return input == ContainerInput.SWAP && button == OFFHAND_BUTTON;
	}

	@Override
	public void clicked(int slotId, int button, ContainerInput input, Player player) {
		if (slotId >= 0 && slotId < POUCH_SLOTS) {
			if (player instanceof ServerPlayer serverPlayer && this.stillValid(player)) {
				this.clickPouchSlot(slotId, input, serverPlayer);
			}
			return;
		}
		if (this.isLockedClick(slotId, button, input)) {
			return;
		}
		super.clicked(slotId, button, input, player);
	}

	private void clickPouchSlot(int slot, ContainerInput input, ServerPlayer player) {
		ItemStack pouch = this.pouch();
		PouchStorage storage = CrystalPouchItem.storage(pouch);
		Item item = PouchItems.item(slot);
		ItemStack carried = this.getCarried();
		if (input == ContainerInput.PICKUP) {
			if (!carried.isEmpty()) {
				if (carried.is(item)) {
					carried.shrink(storage.insert(slot, carried.getCount()));
				}
			} else {
				int taken = storage.takeStack(slot);
				if (taken > 0) {
					this.setCarried(new ItemStack(item, taken));
				}
			}
		} else if (input == ContainerInput.QUICK_MOVE) {
			this.moveToInventory(storage, slot, item, player);
		}
		CrystalPouchItem.save(pouch, storage);
		this.broadcastChanges();
	}

	/** Moves as much of the slot as fits into the player's inventory; only what was actually added leaves the pouch. */
	private void moveToInventory(PouchStorage storage, int slot, Item item, Player player) {
		while (storage.count(slot) > 0) {
			ItemStack batch = new ItemStack(item, Math.min(PouchStorage.STACK, storage.count(slot)));
			int offered = batch.getCount();
			this.moveItemStackTo(batch, INVENTORY_START, HOTBAR_START + 9, true);
			int added = offered - batch.getCount();
			storage.take(slot, added);
			if (added < offered) {
				break;
			}
		}
	}

	/** Shift-click in the player inventory: shards and crystals go into the pouch (server side), as much as fits. */
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		if (index < POUCH_SLOTS || player.level().isClientSide() || !this.stillValid(player)) {
			return ItemStack.EMPTY;
		}
		Slot source = this.slots.get(index);
		ItemStack stack = source.getItem();
		int slot = PouchItems.slotOf(stack.getItem());
		if (slot >= 0) {
			ItemStack pouch = this.pouch();
			PouchStorage storage = CrystalPouchItem.storage(pouch);
			stack.shrink(storage.insert(slot, stack.getCount()));
			CrystalPouchItem.save(pouch, storage);
			source.setChanged();
		}
		return ItemStack.EMPTY;
	}
}
