package at.donmatzos.prismaticrealms.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;

import at.donmatzos.prismaticrealms.client.artifact.ArtifactBarToggle;

/** Opening the recipe book in the inventory closes the artifact bar: both want the strip left of the window. */
@Mixin(RecipeBookComponent.class)
public abstract class RecipeBookComponentArtifactMixin {
	@Inject(method = "setVisible", at = @At("HEAD"))
	private void prismaticrealms$closeArtifactBar(boolean visible, CallbackInfo ci) {
		if (visible && Minecraft.getInstance().gui.screen() instanceof InventoryScreen) {
			ArtifactBarToggle.setOpen(false);
		}
	}
}
