package at.donmatzos.prismaticrealms.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import at.donmatzos.prismaticrealms.tag.ModTags;

/**
 * PARKED (2026-10-12): not listed in prismaticrealms.client.mixins.json, so it is never applied. The user tested it and
 * the result wasn't right yet (the tilted scythe didn't sit as intended); kept to pick up again later. To re-enable, add
 * "ScytheTwoHandedPoseMixin" back to the "client" list.
 *
 * Two-handed scythe hold (third person): with a scythe in the main hand and nothing in the other hand,
 * <ul>
 * <li>both arms take the shaft: the main arm low and forward, the off arm reaching across the body (setupAnim, applied
 * after the vanilla arm poses and before setupAttackAnimation, so the normal swing still plays on top);</li>
 * <li>the scythe itself is tilted: rolled around the hand so its top leans outwards to the main-hand side and its lower end
 * comes down to the off hand (translateToHand, which places the held item; ItemInHandLayer renders it right after).</li>
 * </ul>
 * There is no Fabric API for arm poses or held-item placement.
 */
@Mixin(HumanoidModel.class)
public abstract class ScytheTwoHandedPoseMixin {
	// radians, for a right-handed entity (mirrored for left-handed ones)
	@Unique private static final float MAIN_ARM_X = -0.6F, MAIN_ARM_Y = 0.3F;
	@Unique private static final float OFF_ARM_X = -0.75F, OFF_ARM_Y = 0.6F, OFF_ARM_Z = 0.1F;
	@Unique private static final float WALK_SWAY = 0.3F;   // share of the vanilla walking swing that's kept
	// how far the scythe is rolled outwards around the grip, in degrees
	@Unique private static final float ITEM_TILT = 35.0F;
	// the grip in arm space: ItemInHandLayer's adult offset (1, 2, -10)/16, taken back through its X -90 / Y 180 rotation
	@Unique private static final float GRIP_X = 1.0F / 16.0F, GRIP_Y = 10.0F / 16.0F, GRIP_Z = -2.0F / 16.0F;

	@Unique
	private static boolean prismaticrealms$twoHandedScythe(HumanoidRenderState state) {
		boolean rightHanded = state.mainArm == HumanoidArm.RIGHT;
		ItemStack offHandItem = rightHanded ? state.leftHandItemStack : state.rightHandItemStack;
		HumanoidModel.ArmPose mainPose = rightHanded ? state.rightArmPose : state.leftArmPose;
		return state.getMainHandItemStack().is(ModTags.SCYTHES) && offHandItem.isEmpty() && mainPose == HumanoidModel.ArmPose.ITEM && !state.isUsingItem;
	}

	@Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/HumanoidModel;setupAttackAnimation(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V"))
	private void prismaticrealms$twoHandedScytheArms(HumanoidRenderState state, CallbackInfo ci) {
		if (!prismaticrealms$twoHandedScythe(state)) {
			return;
		}
		boolean rightHanded = state.mainArm == HumanoidArm.RIGHT;
		HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;
		ModelPart mainArm = rightHanded ? model.rightArm : model.leftArm;
		ModelPart offArm = rightHanded ? model.leftArm : model.rightArm;
		float side = rightHanded ? 1.0F : -1.0F;
		mainArm.xRot = mainArm.xRot * WALK_SWAY + MAIN_ARM_X;
		mainArm.yRot = -MAIN_ARM_Y * side;
		mainArm.zRot = 0.0F;
		offArm.xRot = offArm.xRot * WALK_SWAY + OFF_ARM_X;
		offArm.yRot = OFF_ARM_Y * side;
		offArm.zRot = OFF_ARM_Z * side;
	}

	@Inject(method = "translateToHand(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
			at = @At("TAIL"))
	private void prismaticrealms$tiltTwoHandedScythe(HumanoidRenderState state, HumanoidArm arm, PoseStack poseStack, CallbackInfo ci) {
		if (arm != state.mainArm || !prismaticrealms$twoHandedScythe(state)) {
			return;
		}
		// roll around the arm's forward axis, pivoting on the grip so the scythe stays in the hand.
		// Arm space: +y runs down the arm, +x is the entity's left; a negative roll moves the top towards -x (the right side).
		float side = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
		float gripX = -GRIP_X * side;
		poseStack.translate(gripX, GRIP_Y, GRIP_Z);
		poseStack.rotateDegrees(Axis.ZP, -ITEM_TILT * side);
		poseStack.translate(-gripX, -GRIP_Y, -GRIP_Z);
	}
}
