package at.donmatzos.prismaticrealms.artifact;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * The view of a player's artifact slots as a Container, on both sides: the server's copy is authoritative and refreshes
 * bonuses; the client's copy is the synced attachment, so click prediction matches.
 */
public class ArtifactContainer implements Container {
	private final Player player;

	public ArtifactContainer(Player player) {
		this.player = player;
	}

	/** Only the server recalculates bonuses; the client only mirrors the slots for click prediction. */
	private void refresh() {
		if (this.player instanceof ServerPlayer serverPlayer) {
			ArtifactEffects.refresh(serverPlayer);
		}
	}

	@Override
	public int getContainerSize() {
		return ArtifactRules.SLOTS;
	}

	@Override
	public boolean isEmpty() {
		return ArtifactSlots.of(this.player).stacks().stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getItem(int slot) {
		return ArtifactSlots.of(this.player).get(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack current = this.getItem(slot);
		if (current.isEmpty() || amount <= 0) {
			return ItemStack.EMPTY;
		}
		this.setItem(slot, ItemStack.EMPTY);
		return current;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return this.removeItem(slot, 1);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		ArtifactSlots.set(this.player, ArtifactSlots.of(this.player).with(slot, stack));
		this.refresh();
	}

	@Override
	public void setChanged() {
	}

	@Override
	public boolean stillValid(Player player) {
		return player == this.player && player.isAlive();
	}

	@Override
	public void clearContent() {
		ArtifactSlots.set(this.player, ArtifactSlots.EMPTY);
		this.refresh();
	}
}
