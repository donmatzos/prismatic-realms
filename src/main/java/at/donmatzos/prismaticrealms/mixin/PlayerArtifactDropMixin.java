package at.donmatzos.prismaticrealms.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;

import at.donmatzos.prismaticrealms.artifact.ArtifactSlots;

/**
 * Equipped artifacts drop on death together with the inventory (same game-rule check, same drop method). The slots are
 * cleared here, before the attachment is copied to the respawned player, so only keepInventory carries them over.
 */
@Mixin(Player.class)
public abstract class PlayerArtifactDropMixin {
	@Inject(method = "dropEquipment", at = @At("HEAD"))
	private void prismaticrealms$dropArtifacts(ServerLevel level, CallbackInfo ci) {
		Player self = (Player) (Object) this;
		if (level.getGameRules().get(GameRules.KEEP_INVENTORY)) {
			return;
		}
		ArtifactSlots slots = ArtifactSlots.of(self);
		for (ItemStack stack : slots.stacks()) {
			if (!stack.isEmpty()) {
				ItemEntity drop = self.createItemStackToDrop(stack, true, false);
				if (drop != null) {
					level.addFreshEntity(drop);
				}
			}
		}
		ArtifactSlots.set(self, ArtifactSlots.EMPTY);
	}
}
