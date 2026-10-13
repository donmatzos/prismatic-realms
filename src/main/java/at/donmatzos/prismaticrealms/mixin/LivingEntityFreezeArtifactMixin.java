package at.donmatzos.prismaticrealms.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import at.donmatzos.prismaticrealms.artifact.ArtifactEffects;
import at.donmatzos.prismaticrealms.artifact.ArtifactItem;

/** Artifacts that prevent freezing (Frostwalker Star) act like leather boots in powder snow. Runs on both sides. */
@Mixin(LivingEntity.class)
public abstract class LivingEntityFreezeArtifactMixin {
	@Inject(method = "canFreeze", at = @At("HEAD"), cancellable = true)
	private void prismaticrealms$artifactFreezeImmunity(CallbackInfoReturnable<Boolean> cir) {
		if ((Object) this instanceof Player player && ArtifactEffects.equipped(player).stream().anyMatch(ArtifactItem::preventsFreezing)) {
			cir.setReturnValue(false);
		}
	}
}
