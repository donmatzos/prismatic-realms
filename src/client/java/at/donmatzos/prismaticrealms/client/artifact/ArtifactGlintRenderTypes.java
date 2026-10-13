package at.donmatzos.prismaticrealms.client.artifact;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.TextureTransform;
import net.minecraft.client.renderer.texture.TextureAtlas;

import at.donmatzos.prismaticrealms.PrismaticRealms;

/**
 * Vanilla's item glint render type (the item atlas with the glint scrolled over it) with a tinted glint texture
 * (`textures/misc/glint/<name>.png`, made by the local tools/textures/artifact_glint_gen.py), one per artifact family colour.
 */
public final class ArtifactGlintRenderTypes {
	private static final Map<String, RenderType> CUTOUT = new HashMap<>();

	private ArtifactGlintRenderTypes() {
	}

	public static RenderType cutout(String glintName) {
		return CUTOUT.computeIfAbsent(glintName, name -> RenderType.create("prismaticrealms_artifact_glint_" + name,
				RenderSetup.builder(RenderPipelines.ITEM_CUTOUT_GLINT)
						.withTexture("Sampler0", TextureAtlas.LOCATION_ITEMS)
						.withTexture("GlintSampler", PrismaticRealms.id("textures/misc/glint/" + name + ".png"))
						.setTextureTransform(TextureTransform.GLINT_TEXTURING)
						.useLightmap()
						.useOverlay()
						.affectsCrumbling()
						.setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
						.createRenderSetup()));
	}
}
