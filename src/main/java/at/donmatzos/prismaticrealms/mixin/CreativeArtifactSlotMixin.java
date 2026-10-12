package at.donmatzos.prismaticrealms.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.artifact.ArtifactRules;

/**
 * Creative inventory edits arrive as "set slot N" packets, and vanilla only accepts inventory-menu slots 1-45. The
 * artifact bar's slots (46-48) are accepted here under the artifact rules (ArtifactSlot#mayPlace); a refused edit resyncs
 * the client.
 */
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class CreativeArtifactSlotMixin {
	@Unique
	private static final int FIRST_ARTIFACT_SLOT = 46;

	@Shadow
	public ServerPlayer player;

	@Inject(method = "handleSetCreativeModeSlot", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/server/level/ServerLevel;)V",
			shift = At.Shift.AFTER), cancellable = true)
	private void prismaticrealms$setArtifactSlot(ServerboundSetCreativeModeSlotPacket packet, CallbackInfo ci) {
		int index = packet.slotNum();
		if (index < FIRST_ARTIFACT_SLOT || index >= FIRST_ARTIFACT_SLOT + ArtifactRules.SLOTS || !this.player.hasInfiniteMaterials()) {
			return;
		}
		ci.cancel();
		InventoryMenu menu = this.player.inventoryMenu;
		Slot slot = menu.getSlot(index);
		ItemStack stack = packet.itemStack();
		boolean valid = stack.isItemEnabled(this.player.level().enabledFeatures()) && stack.getCount() <= 1;
		if (valid && (stack.isEmpty() || slot.mayPlace(stack))) {
			slot.setByPlayer(stack);
			menu.setRemoteSlot(index, stack);
			menu.broadcastChanges();
		} else {
			menu.sendAllDataToRemote();
		}
	}
}
