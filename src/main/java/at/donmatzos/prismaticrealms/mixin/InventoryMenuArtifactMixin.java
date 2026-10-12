package at.donmatzos.prismaticrealms.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import at.donmatzos.prismaticrealms.artifact.ArtifactBarLayout;
import at.donmatzos.prismaticrealms.artifact.ArtifactContainer;
import at.donmatzos.prismaticrealms.artifact.ArtifactItem;
import at.donmatzos.prismaticrealms.artifact.ArtifactRules;
import at.donmatzos.prismaticrealms.artifact.ArtifactSlot;

/**
 * The artifact bar: three ArtifactSlots appended to every player's inventory menu (after vanilla's 46 slots, so vanilla
 * indices stay unchanged), positioned on the bar right of the inventory window, and shift-click routing for artifacts.
 */
@Mixin(InventoryMenu.class)
public abstract class InventoryMenuArtifactMixin extends AbstractContainerMenu {
	@Unique
	private static final int FIRST_ARTIFACT_SLOT = 46;
	@Unique
	private static final int INVENTORY_START = 9;
	@Unique
	private static final int INVENTORY_END = 45;   // exclusive: main inventory 9-35 + hotbar 36-44

	protected InventoryMenuArtifactMixin(MenuType<?> type, int containerId) {
		super(type, containerId);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void prismaticrealms$addArtifactSlots(Inventory inventory, boolean active, Player owner, CallbackInfo ci) {
		ArtifactContainer artifacts = new ArtifactContainer(owner);
		for (int i = 0; i < ArtifactRules.SLOTS; i++) {
			this.addSlot(new ArtifactSlot(artifacts, i, ArtifactBarLayout.SURVIVAL.slotX(), ArtifactBarLayout.slotY(ArtifactBarLayout.SURVIVAL, i)));
		}
	}

	@Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
	private void prismaticrealms$quickMoveArtifact(Player player, int index, CallbackInfoReturnable<ItemStack> cir) {
		Slot slot = this.slots.get(index);
		if (!slot.hasItem()) {
			return;
		}
		ItemStack stack = slot.getItem();
		if (index >= FIRST_ARTIFACT_SLOT && index < FIRST_ARTIFACT_SLOT + ArtifactRules.SLOTS) {
			ItemStack moving = stack.copy();
			if (this.moveItemStackTo(moving, INVENTORY_START, INVENTORY_END, false)) {
				slot.set(ItemStack.EMPTY);
			}
			cir.setReturnValue(ItemStack.EMPTY);
			return;
		}
		if (index >= INVENTORY_START && index < INVENTORY_END && stack.getItem() instanceof ArtifactItem) {
			Slot first = this.slots.get(FIRST_ARTIFACT_SLOT);
			int target = ArtifactRules.firstFreeSlot(ArtifactSlot.equippedIds(first.container),
					BuiltInRegistries.ITEM.getKey(stack.getItem()).toString(), true);
			if (target >= 0) {
				this.slots.get(FIRST_ARTIFACT_SLOT + target).set(stack.split(1));
				slot.setChanged();
				cir.setReturnValue(ItemStack.EMPTY);
			}
			// no free slot (or already equipped): fall through to vanilla's inventory/hotbar shuffle
		}
	}
}
