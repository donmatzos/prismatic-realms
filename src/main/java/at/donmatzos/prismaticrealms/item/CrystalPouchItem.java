package at.donmatzos.prismaticrealms.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import at.donmatzos.prismaticrealms.pouch.CrystalPouchMenu;
import at.donmatzos.prismaticrealms.pouch.ModComponents;
import at.donmatzos.prismaticrealms.pouch.PouchContents;
import at.donmatzos.prismaticrealms.pouch.PouchItems;
import at.donmatzos.prismaticrealms.pouch.PouchStorage;
import at.donmatzos.prismaticrealms.pouch.PouchTooltip;

/**
 * Crystal Pouch (tier 0), +1 (tier 1) and +2 (tier 2): stores each family's shards and crystals (PouchStorage) in the
 * pouch_contents component. Opening the screen and the bundle-style clicks are added in later tasks.
 */
public class CrystalPouchItem extends Item {
	private final int tier;

	public CrystalPouchItem(Item.Properties properties, int tier) {
		super(properties);
		this.tier = tier;
	}

	public int tier() {
		return this.tier;
	}

	public static PouchStorage storage(ItemStack pouch) {
		int tier = pouch.getItem() instanceof CrystalPouchItem item ? item.tier : 0;
		return new PouchStorage(pouch.getOrDefault(ModComponents.POUCH_CONTENTS, PouchContents.EMPTY).toArray(), tier);
	}

	public static void save(ItemStack pouch, PouchStorage storage) {
		int selected = pouch.getOrDefault(ModComponents.POUCH_CONTENTS, PouchContents.EMPTY).selected();
		PouchContents contents = PouchContents.of(storage.counts());
		pouch.set(ModComponents.POUCH_CONTENTS, contents.withSelected(selected >= 0 && selected < entries(contents).size() ? selected : -1));
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack pouch = player.getItemInHand(hand);
		if (player instanceof ServerPlayer serverPlayer) {
			serverPlayer.openMenu(new ExtendedMenuProvider<InteractionHand>() {
				@Override
				public InteractionHand getScreenOpeningData(ServerPlayer opener) {
					return hand;
				}

				@Override
				public Component getDisplayName() {
					return pouch.getHoverName();
				}

				@Override
				public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player opener) {
					return new CrystalPouchMenu(containerId, inventory, hand);
				}
			});
		}
		return InteractionResult.SUCCESS;
	}

	/** Slot indices (see PouchStorage) that hold anything, in slot order: the entries of the bundle-style tooltip. */
	public static List<Integer> entries(ItemStack pouch) {
		return entries(pouch.getOrDefault(ModComponents.POUCH_CONTENTS, PouchContents.EMPTY));
	}

	private static List<Integer> entries(PouchContents contents) {
		List<Integer> counts = contents.counts();
		List<Integer> entries = new ArrayList<>();
		for (int i = 0; i < PouchStorage.SLOTS; i++) {
			if (counts.get(i) > 0) {
				entries.add(i);
			}
		}
		return entries;
	}

	/** The selected entry (index into entries), or -1. */
	public static int selected(ItemStack pouch) {
		int selected = pouch.getOrDefault(ModComponents.POUCH_CONTENTS, PouchContents.EMPTY).selected();
		return selected >= 0 && selected < entries(pouch).size() ? selected : -1;
	}

	public static void select(ItemStack pouch, int selected) {
		PouchContents contents = pouch.getOrDefault(ModComponents.POUCH_CONTENTS, PouchContents.EMPTY);
		int clamped = selected >= 0 && selected < entries(contents).size() ? selected : -1;
		if (clamped != contents.selected()) {
			pouch.set(ModComponents.POUCH_CONTENTS, contents.withSelected(clamped));
		}
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack pouch) {
		PouchStorage storage = storage(pouch);
		List<ItemStack> stacks = entries(pouch).stream().map(i -> new ItemStack(PouchItems.item(i), storage.count(i))).toList();
		return stacks.isEmpty() ? Optional.empty() : Optional.of(new PouchTooltip(stacks, selected(pouch)));
	}

	/**
	 * Bundle-style handling in any screen, with the pouch in a slot: clicking shards or crystals onto it puts them in;
	 * right-clicking it with an empty cursor takes one stack of the selected entry (the first one if none is selected).
	 */
	@Override
	public boolean overrideOtherStackedOnMe(ItemStack pouch, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess carried) {
		if (action == ClickAction.PRIMARY && other.isEmpty()) {
			select(pouch, -1);   // picking the pouch up drops the selection, like BundleItem
			return false;
		}
		if (action == ClickAction.PRIMARY && !other.isEmpty()) {
			int index = PouchItems.slotOf(other.getItem());
			if (index < 0) {
				return false;
			}
			PouchStorage storage = storage(pouch);
			int inserted = slot.allowModification(player) ? storage.insert(index, other.getCount()) : 0;
			other.shrink(inserted);
			save(pouch, storage);
			player.playSound(inserted > 0 ? SoundEvents.BUNDLE_INSERT : SoundEvents.BUNDLE_INSERT_FAIL, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
			return true;
		}
		if (action == ClickAction.SECONDARY && other.isEmpty()) {
			List<Integer> entries = entries(pouch);
			if (entries.isEmpty() || !slot.allowModification(player)) {
				return true;
			}
			int selected = selected(pouch);
			int index = entries.get(selected >= 0 ? selected : 0);
			PouchStorage storage = storage(pouch);
			int taken = storage.takeStack(index);
			save(pouch, storage);
			select(pouch, selected);   // re-validate: the entry may be gone now
			carried.set(new ItemStack(PouchItems.item(index), taken));
			player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
			return true;
		}
		return false;
	}
}
