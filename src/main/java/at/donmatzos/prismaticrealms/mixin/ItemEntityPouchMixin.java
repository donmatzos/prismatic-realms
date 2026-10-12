package at.donmatzos.prismaticrealms.mixin;

import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.pouch.PouchCollect;

/**
 * Crystal Pouch auto-collect: before vanilla adds a picked-up item to the inventory, shards and crystals go into the
 * player's pouches. What doesn't fit stays on the item entity, and vanilla's pickup carries on with it as usual.
 */
@Mixin(ItemEntity.class)
public abstract class ItemEntityPouchMixin {
	@Shadow
	private int pickupDelay;
	@Shadow
	private @Nullable UUID target;

	@Shadow
	public abstract ItemStack getItem();

	@Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true)
	private void prismaticrealms$collectIntoPouch(Player player, CallbackInfo ci) {
		if (player.level().isClientSide() || this.pickupDelay != 0 || (this.target != null && !this.target.equals(player.getUUID()))) {
			return;
		}
		ItemStack stack = this.getItem();
		Item item = stack.getItem();
		int original = stack.getCount();
		int stored = PouchCollect.collect(player, stack);
		if (stored == 0) {
			return;
		}
		ItemEntity self = (ItemEntity) (Object) this;
		player.take(self, stored);
		player.awardStat(Stats.ITEM_PICKED_UP.get(item), stored);
		if (player instanceof ServerPlayer serverPlayer) {
			// the item never lands in an inventory slot, so fire the trigger recipe unlocks rely on ourselves
			CriteriaTriggers.INVENTORY_CHANGED.trigger(serverPlayer, player.getInventory(), new ItemStack(item, stored));
		}
		if (stack.isEmpty()) {
			self.discard();
			stack.setCount(original);   // like vanilla: keep the count for the pickup animation
			player.onItemPickup(self);
			ci.cancel();
		}
	}
}
