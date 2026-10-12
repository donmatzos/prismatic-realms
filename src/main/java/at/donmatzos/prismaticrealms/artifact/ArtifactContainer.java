package at.donmatzos.prismaticrealms.artifact;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** The server's view of a player's artifact slots as a Container: reads and replaces the attachment value. */
public class ArtifactContainer implements Container {
	private final ServerPlayer player;

	public ArtifactContainer(ServerPlayer player) {
		this.player = player;
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
		ArtifactEffects.refresh(this.player);
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
		ArtifactEffects.refresh(this.player);
	}
}
