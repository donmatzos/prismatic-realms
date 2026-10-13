package at.donmatzos.prismaticrealms.altar;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

/** The five recipe slots of the Spectral Altar, in menu slot order (fuel isn't part of the recipe). */
public record AltarRecipeInput(ItemStack crystal, ItemStack leftShard, ItemStack rightShard, ItemStack setting, ItemStack catalyst) implements RecipeInput {
	@Override
	public ItemStack getItem(int index) {
		return switch (index) {
			case 0 -> this.crystal;
			case 1 -> this.leftShard;
			case 2 -> this.rightShard;
			case 3 -> this.setting;
			case 4 -> this.catalyst;
			default -> throw new IllegalArgumentException("Recipe does not contain slot " + index);
		};
	}

	@Override
	public int size() {
		return 5;
	}

	@Override
	public boolean isEmpty() {
		return this.crystal.isEmpty() && this.leftShard.isEmpty() && this.rightShard.isEmpty() && this.setting.isEmpty() && this.catalyst.isEmpty();
	}
}
