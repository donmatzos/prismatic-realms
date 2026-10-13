package at.donmatzos.prismaticrealms.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.resources.model.geometry.BakedQuad;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.artifact.ArtifactGlint;
import at.donmatzos.prismaticrealms.client.artifact.ArtifactGlintRenderTypes;

/**
 * Artifacts shine in their family colour: the glint render type of a quad whose sprite is an artifact texture is swapped
 * for one with the family's tinted glint texture. Every other quad keeps vanilla's choice (translucent ones too).
 */
@Mixin(ItemFeatureRenderer.class)
public abstract class ItemFeatureRendererGlintMixin {
	@Redirect(method = "prepareMainSubmit", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/resources/model/geometry/BakedQuad$MaterialInfo;itemGlintRenderType()Lnet/minecraft/client/renderer/rendertype/RenderType;"))
	private RenderType prismaticrealms$tintedGlint(BakedQuad.MaterialInfo material) {
		RenderType vanilla = material.itemGlintRenderType();
		if (vanilla == Sheets.cutoutItemGlintSheet() && material.sprite().contents().name().getNamespace().equals(PrismaticRealms.MOD_ID)) {
			String glint = ArtifactGlint.forSprite(material.sprite().contents().name().getPath());
			if (glint != null) {
				return ArtifactGlintRenderTypes.cutout(glint);
			}
		}
		return vanilla;
	}
}
