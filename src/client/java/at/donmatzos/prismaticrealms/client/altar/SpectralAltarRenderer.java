package at.donmatzos.prismaticrealms.client.altar;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.altar.SpectralAltarAnimation;
import at.donmatzos.prismaticrealms.altar.SpectralAltarBlockEntity;
import at.donmatzos.prismaticrealms.block.ModBlocks;

/**
 * Draws the Spectral Altar's floating parts: a full-bright Spectral Binder octahedron (8 textured triangles) and the
 * family shards orbiting it as real items. Sizes are in blocks; tune them here after the in-game check.
 */
public class SpectralAltarRenderer implements BlockEntityRenderer<SpectralAltarBlockEntity, SpectralAltarRenderState> {
	private static final Identifier BINDER_TEXTURE = PrismaticRealms.id("textures/entity/spectral_altar/binder.png");
	private static final RenderType BINDER_RENDER_TYPE = RenderTypes.entityCutout(BINDER_TEXTURE);
	private static final float CENTER_Y = 17.0F / 16.0F;    // 5 px above the 12 px tall body
	private static final float HALF_HEIGHT = 3.0F / 16.0F;  // 6 px tall
	private static final float HALF_WIDTH = 2.5F / 16.0F;   // 5 px wide
	private static final float RING_RADIUS = 0.4F;
	private static final float SHARD_SCALE = 0.5F;          // the ground display halves it again: about 1/4 block
	private static final float TEXTURE_WIDTH = 32.0F;
	private static final float TEXTURE_HEIGHT = 16.0F;

	private final ItemModelResolver itemModelResolver;
	// Built on first use: renderers are created during resource loading, before item components are bound.
	private @Nullable List<ItemStack> shards;

	public SpectralAltarRenderer(BlockEntityRendererProvider.Context context) {
		this.itemModelResolver = context.itemModelResolver();
	}

	@Override
	public SpectralAltarRenderState createRenderState() {
		return new SpectralAltarRenderState();
	}

	@Override
	public void extractRenderState(SpectralAltarBlockEntity altar, SpectralAltarRenderState state, float partialTicks, Vec3 cameraPosition,
			ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(altar, state, partialTicks, cameraPosition, breakProgress);
		state.time = SpectralAltarAnimation.wrapTime(altar.time, partialTicks);
		state.orbit = Mth.lerp(partialTicks, altar.oOrbit, altar.orbit);
		if (this.shards == null) {
			this.shards = ModBlocks.CRYSTAL_ORES.stream().map(ore -> new ItemStack(ore.shard())).toList();
		}
		while (state.shards.size() < this.shards.size()) {
			state.shards.add(new ItemStackRenderState());
		}
		int seed = (int) altar.getBlockPos().asLong();
		for (int i = 0; i < this.shards.size(); i++) {
			this.itemModelResolver.updateForTopItem(state.shards.get(i), this.shards.get(i), ItemDisplayContext.GROUND, altar.getLevel(), null, seed + i);
		}
	}

	@Override
	public void submit(SpectralAltarRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.translate(0.5F, CENTER_Y + SpectralAltarAnimation.binderBob(state.time), 0.5F);

		poseStack.pushPose();
		poseStack.rotate(Axis.YP, SpectralAltarAnimation.binderSpin(state.time));
		submitNodeCollector.submitCustomGeometry(poseStack, BINDER_RENDER_TYPE, SpectralAltarRenderer::octahedron);
		poseStack.popPose();

		int count = state.shards.size();
		for (int i = 0; i < count; i++) {
			float angle = SpectralAltarAnimation.shardAngle(i, count, state.orbit);
			poseStack.pushPose();
			poseStack.translate(Mth.cos(angle) * RING_RADIUS, SpectralAltarAnimation.shardBob(i, count, state.time), Mth.sin(angle) * RING_RADIUS);
			// turn the item's front (+Z) to face outwards, away from the binder
			poseStack.rotate(Axis.YP, Mth.HALF_PI - angle);
			poseStack.scale(SHARD_SCALE, SHARD_SCALE, SHARD_SCALE);
			state.shards.get(i).submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}
		poseStack.popPose();
	}

	/** Eight faces: four around the upper apex, four around the lower one. Each triangle is a quad with its last corner doubled. */
	private static void octahedron(PoseStack.Pose pose, VertexConsumer buffer) {
		float[][] equator = {{HALF_WIDTH, 0, 0}, {0, 0, HALF_WIDTH}, {-HALF_WIDTH, 0, 0}, {0, 0, -HALF_WIDTH}};
		float[] top = {0, HALF_HEIGHT, 0};
		float[] bottom = {0, -HALF_HEIGHT, 0};
		for (int k = 0; k < 4; k++) {
			float[] a = equator[k];
			float[] b = equator[(k + 1) % 4];
			float u0 = 8 * k;
			face(pose, buffer, top, a, b, u0 + 4, 0, u0, u0 + 8, 8);
			face(pose, buffer, bottom, a, b, u0 + 4, 16, u0, u0 + 8, 8);
		}
	}

	/** One triangle; texture coordinates in pixels of the 32x16 texture. */
	private static void face(PoseStack.Pose pose, VertexConsumer buffer, float[] apex, float[] a, float[] b, float uApex, float vApex, float uA, float uB, float vBase) {
		// face normal from the cross product, flipped to point away from the centre (the octahedron is centred on the origin)
		float ax = a[0] - apex[0], ay = a[1] - apex[1], az = a[2] - apex[2];
		float bx = b[0] - apex[0], by = b[1] - apex[1], bz = b[2] - apex[2];
		float nx = ay * bz - az * by, ny = az * bx - ax * bz, nz = ax * by - ay * bx;
		float cx = apex[0] + a[0] + b[0], cy = apex[1] + a[1] + b[1], cz = apex[2] + a[2] + b[2];
		if (nx * cx + ny * cy + nz * cz < 0) {
			nx = -nx;
			ny = -ny;
			nz = -nz;
		}
		float length = Mth.sqrt(nx * nx + ny * ny + nz * nz);
		nx /= length;
		ny /= length;
		nz /= length;
		vertex(pose, buffer, apex, uApex, vApex, nx, ny, nz);
		vertex(pose, buffer, a, uA, vBase, nx, ny, nz);
		vertex(pose, buffer, b, uB, vBase, nx, ny, nz);
		vertex(pose, buffer, b, uB, vBase, nx, ny, nz);
	}

	private static void vertex(PoseStack.Pose pose, VertexConsumer buffer, float[] p, float u, float v, float nx, float ny, float nz) {
		buffer.addVertex(pose, p[0], p[1], p[2])
				.setColor(255, 255, 255, 255)
				.setUv(u / TEXTURE_WIDTH, v / TEXTURE_HEIGHT)
				.setOverlay(OverlayTexture.NO_OVERLAY)
				.setLight(LightCoordsUtil.FULL_BRIGHT)
				.setNormal(pose, nx, ny, nz);
	}
}
