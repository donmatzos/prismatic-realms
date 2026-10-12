package at.donmatzos.prismaticrealms.client.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

import at.donmatzos.prismaticrealms.artifact.ArtifactBarLayout;

/**
 * Active potion effects are listed right of the inventory window, exactly where the artifact bar sits; while the bar is
 * shown, the list starts past the bar instead (both where vanilla checks whether effects fit and where it draws them).
 */
@Mixin(EffectsInInventory.class)
public abstract class EffectsInInventoryArtifactMixin {
	@Shadow
	@Final
	private AbstractContainerScreen<?> screen;

	@ModifyVariable(method = {"canSeeEffects", "extractRenderState"}, at = @At(value = "STORE", ordinal = 0), ordinal = 0)
	private int prismaticrealms$clearArtifactBar(int xo) {
		AbstractContainerScreenAccessor accessor = (AbstractContainerScreenAccessor) this.screen;
		if (this.screen instanceof InventoryScreen) {
			return xo + ArtifactBarLayout.rightOverhang(ArtifactBarLayout.SURVIVAL, accessor.prismaticrealms$getImageWidth());
		}
		if (this.screen instanceof CreativeModeInventoryScreen creative && creative.isInventoryOpen()) {
			return xo + ArtifactBarLayout.rightOverhang(ArtifactBarLayout.CREATIVE, accessor.prismaticrealms$getImageWidth());
		}
		return xo;
	}
}
