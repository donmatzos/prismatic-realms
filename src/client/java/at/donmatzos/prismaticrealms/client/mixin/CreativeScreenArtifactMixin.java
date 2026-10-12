package at.donmatzos.prismaticrealms.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;

import at.donmatzos.prismaticrealms.artifact.ArtifactBarLayout;
import at.donmatzos.prismaticrealms.artifact.ArtifactRules;

/**
 * The artifact bar on the creative "Survival Inventory" tab: creative mirrors every inventory-menu slot (indices kept)
 * and positions them by index, so the three artifact slots (46-48) are moved onto the bar after each tab switch, and
 * clicks on the bar don't count as clicking outside the window.
 */
@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeScreenArtifactMixin {
	@Unique
	private static final int FIRST_ARTIFACT_SLOT = 46;

	@Shadow
	private boolean hasClickedOutside;

	@Inject(method = "selectTab", at = @At("TAIL"))
	private void prismaticrealms$placeArtifactBar(CreativeModeTab tab, CallbackInfo ci) {
		CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;
		if (!self.isInventoryOpen()) {
			return;
		}
		for (int i = 0; i < ArtifactRules.SLOTS; i++) {
			int index = FIRST_ARTIFACT_SLOT + i;
			if (index < self.getMenu().slots.size()) {
				Slot slot = self.getMenu().slots.get(index);
				((SlotPositionAccessor) slot).prismaticrealms$setX(ArtifactBarLayout.CREATIVE.slotX());
				((SlotPositionAccessor) slot).prismaticrealms$setY(ArtifactBarLayout.slotY(ArtifactBarLayout.CREATIVE, i));
			}
		}
	}

	@Inject(method = "hasClickedOutside", at = @At("RETURN"), cancellable = true)
	private void prismaticrealms$barIsInside(double mx, double my, int xo, int yo, CallbackInfoReturnable<Boolean> cir) {
		CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;
		if (cir.getReturnValueZ() && self.isInventoryOpen() && ArtifactBarLayout.contains(ArtifactBarLayout.CREATIVE, mx, my, xo, yo)) {
			this.hasClickedOutside = false;
			cir.setReturnValue(false);
		}
	}
}
