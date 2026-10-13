package at.donmatzos.prismaticrealms.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import at.donmatzos.prismaticrealms.armor.ArmorSets;
import at.donmatzos.prismaticrealms.family.CrystalFamily;

/** Deep Diver (Cerulean Abyss set): undoes vanilla's "divide mining speed by 5 when not on the ground". */
@Mixin(Player.class)
public abstract class PlayerDestroySpeedMixin {
	@Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
	private void prismaticrealms$noAirbornePenalty(BlockState state, CallbackInfoReturnable<Float> cir) {
		Player player = (Player) (Object) this;
		if (!player.onGround() && ArmorSets.has(player, CrystalFamily.CERULEAN_ABYSS)) {
			cir.setReturnValue(cir.getReturnValueF() * 5.0F);
		}
	}
}
