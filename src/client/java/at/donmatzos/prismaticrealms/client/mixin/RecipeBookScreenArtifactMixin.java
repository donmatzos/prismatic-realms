package at.donmatzos.prismaticrealms.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

import at.donmatzos.prismaticrealms.artifact.ArtifactBarLayout;

/** The survival inventory's artifact bar lies outside the window rectangle; clicks on it don't drop the held item. */
@Mixin(AbstractRecipeBookScreen.class)
public abstract class RecipeBookScreenArtifactMixin {
	@Inject(method = "hasClickedOutside", at = @At("RETURN"), cancellable = true)
	private void prismaticrealms$barIsInside(double mx, double my, int xo, int yo, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValueZ() && (Object) this instanceof InventoryScreen
				&& ArtifactBarLayout.contains(ArtifactBarLayout.SURVIVAL, mx, my, xo, yo)) {
			cir.setReturnValue(false);
		}
	}
}
