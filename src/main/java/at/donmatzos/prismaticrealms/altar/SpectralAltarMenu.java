package at.donmatzos.prismaticrealms.altar;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.block.ModBlocks;
import at.donmatzos.prismaticrealms.item.ModItems;
import at.donmatzos.prismaticrealms.particle.ModParticles;
import at.donmatzos.prismaticrealms.pouch.ModMenus;
import at.donmatzos.prismaticrealms.recipe.ModRecipes;
import at.donmatzos.prismaticrealms.tag.ModTags;

/**
 * The Spectral Altar's crafting menu: five recipe slots in a diamond, a fuel slot (shards of any family) and a result
 * slot. Like the smithing table, the altar stores nothing: inputs live in this menu and go back to the player on close,
 * and the result is computed on the server only (the client has no recipes) and synced through the result slot. The
 * result can only be taken with at least AltarRules.FUEL_PER_CRAFT fuel shards; the client knows the synced fuel count
 * and greys the result out otherwise.
 */
public class SpectralAltarMenu extends AbstractContainerMenu {
	public static final int CRYSTAL_SLOT = 0;
	public static final int LEFT_SHARD_SLOT = 1;
	public static final int RIGHT_SHARD_SLOT = 2;
	public static final int SETTING_SLOT = 3;
	public static final int CATALYST_SLOT = 4;
	public static final int FUEL_SLOT = 5;
	public static final int RESULT_SLOT = 6;
	private static final int INVENTORY_START = 7;   // 27 main inventory slots
	private static final int HOTBAR_START = 34;     // 9 hotbar slots
	private static final int HOTBAR_END = 43;
	public static final Identifier FUEL_SLOT_ICON = PrismaticRealms.id("container/spectral_altar/fuel_slot");

	private final ContainerLevelAccess access;
	private final Level level;
	private final ResultContainer result = new ResultContainer();
	private final SimpleContainer inputs = new SimpleContainer(6) {
		@Override
		public void setChanged() {
			super.setChanged();
			SpectralAltarMenu.this.slotsChanged(this);
		}
	};

	/** Client side: the server drives the menu. */
	public SpectralAltarMenu(int containerId, Inventory inventory) {
		this(containerId, inventory, ContainerLevelAccess.NULL);
	}

	public SpectralAltarMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
		super(ModMenus.SPECTRAL_ALTAR, containerId);
		this.access = access;
		this.level = inventory.player.level();
		// Positions follow vanilla's crafting table (the plus of its 3x3 grid, its result slot), so the screen reuses its art.
		// addSlot order is the slot index: the right shard (2) comes before the setting (3) though it sits right of it
		this.addSlot(new Slot(this.inputs, CRYSTAL_SLOT, 48, 17));
		this.addSlot(new Slot(this.inputs, LEFT_SHARD_SLOT, 30, 35));
		this.addSlot(new Slot(this.inputs, RIGHT_SHARD_SLOT, 66, 35));
		this.addSlot(new Slot(this.inputs, SETTING_SLOT, 48, 35));
		this.addSlot(new Slot(this.inputs, CATALYST_SLOT, 48, 53));
		this.addSlot(new Slot(this.inputs, FUEL_SLOT, 8, 53) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.is(ModTags.SHARDS);
			}

			@Override
			public @Nullable Identifier getNoItemIcon() {
				return FUEL_SLOT_ICON;
			}
		});
		this.addSlot(new Slot(this.result, 0, 124, 35) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}

			@Override
			public boolean mayPickup(Player player) {
				return SpectralAltarMenu.this.hasFuel();
			}

			@Override
			public void onTake(Player player, ItemStack carried) {
				carried.onCraftedBy(player, carried.getCount());
				SpectralAltarMenu.this.consumeInputs();
				SpectralAltarMenu.this.access.execute(SpectralAltarMenu::celebrate);
				super.onTake(player, carried);
			}
		});
		this.addStandardInventorySlots(inventory, 8, 84);
	}

	public boolean hasFuel() {
		return AltarRules.hasFuel(this.inputs.getItem(FUEL_SLOT).getCount());
	}

	/** A recipe matches, but there isn't enough fuel to take its result. */
	public boolean resultLocked() {
		return !this.result.getItem(0).isEmpty() && !this.hasFuel();
	}

	private AltarRecipeInput recipeInput() {
		return new AltarRecipeInput(this.inputs.getItem(CRYSTAL_SLOT), this.inputs.getItem(LEFT_SHARD_SLOT),
				this.inputs.getItem(RIGHT_SHARD_SLOT), this.inputs.getItem(SETTING_SLOT), this.inputs.getItem(CATALYST_SLOT));
	}

	@Override
	public void slotsChanged(Container container) {
		super.slotsChanged(container);
		if (this.level instanceof ServerLevel serverLevel) {
			AltarRecipeInput input = this.recipeInput();
			this.result.setItem(0, serverLevel.recipeAccess().getRecipeFor(ModRecipes.ALTAR, input, serverLevel)
					.map(recipe -> recipe.value().assemble(input))
					.orElse(ItemStack.EMPTY));
		}
	}

	private void consumeInputs() {
		for (int slot = CRYSTAL_SLOT; slot <= CATALYST_SLOT; slot++) {
			this.inputs.removeItem(slot, 1);
		}
		this.inputs.removeItem(FUEL_SLOT, AltarRules.FUEL_PER_CRAFT);
	}

	private static void celebrate(Level level, BlockPos pos) {
		level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
		if (level instanceof ServerLevel serverLevel) {
			serverLevel.sendParticles(ModParticles.FROST_FLURRY, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, 20, 0.3, 0.2, 0.3, 0.02);
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(this.access, player, ModBlocks.SPECTRAL_ALTAR);
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		this.access.execute((level, pos) -> this.clearContainer(player, this.inputs));
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		Slot slot = this.slots.get(index);
		if (!slot.hasItem()) {
			return ItemStack.EMPTY;
		}
		ItemStack stack = slot.getItem();
		ItemStack original = stack.copy();
		if (index == RESULT_SLOT) {
			// vanilla repeats quick-move on the result while it stays the same item and checks mayPickup only once
			if (!this.hasFuel()) {
				return ItemStack.EMPTY;
			}
			stack.getItem().onCraftedBy(stack, player);
			if (!this.moveItemStackTo(stack, INVENTORY_START, HOTBAR_END, true)) {
				return ItemStack.EMPTY;
			}
			slot.onQuickCraft(stack, original);
		} else if (index >= INVENTORY_START) {
			if (!this.moveToAltar(stack)) {
				if (index < HOTBAR_START) {
					if (!this.moveItemStackTo(stack, HOTBAR_START, HOTBAR_END, false)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(stack, INVENTORY_START, HOTBAR_START, false)) {
					return ItemStack.EMPTY;
				}
			}
		} else if (!this.moveItemStackTo(stack, INVENTORY_START, HOTBAR_END, false)) {
			return ItemStack.EMPTY;
		}
		if (stack.isEmpty()) {
			slot.setByPlayer(ItemStack.EMPTY);
		} else {
			slot.setChanged();
		}
		if (stack.getCount() == original.getCount()) {
			return ItemStack.EMPTY;
		}
		slot.onTake(player, stack);
		return original;
	}

	/** Tries the altar slot groups AltarRules picks for this item, in order; true if anything moved. */
	private boolean moveToAltar(ItemStack stack) {
		int before = stack.getCount();
		List<AltarRules.Target> targets = AltarRules.quickMoveTargets(stack.is(ModTags.SHARDS), stack.is(ModTags.CRYSTALS), stack.is(ModItems.SPECTRAL_SETTING));
		for (AltarRules.Target target : targets) {
			if (stack.isEmpty()) {
				break;
			}
			switch (target) {
				case CRYSTAL -> this.moveItemStackTo(stack, CRYSTAL_SLOT, CRYSTAL_SLOT + 1, false);
				case SHARDS -> this.moveItemStackTo(stack, LEFT_SHARD_SLOT, RIGHT_SHARD_SLOT + 1, false);
				case SETTING -> this.moveItemStackTo(stack, SETTING_SLOT, SETTING_SLOT + 1, false);
				case CATALYST -> this.moveItemStackTo(stack, CATALYST_SLOT, CATALYST_SLOT + 1, false);
				case FUEL -> this.moveItemStackTo(stack, FUEL_SLOT, FUEL_SLOT + 1, false);
			}
		}
		return stack.getCount() != before;
	}
}
