package at.donmatzos.prismaticrealms.artifact;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.PrismaticRealms;

/** An artifact slot: artifacts only, each at most once across the three (ArtifactRules), one item per slot. */
public class ArtifactSlot extends Slot {
	public static final Identifier SILHOUETTE = PrismaticRealms.id("container/artifacts/slot");

	public ArtifactSlot(Container artifacts, int index, int x, int y) {
		super(artifacts, index, x, y);
	}

	static List<String> equippedIds(Container artifacts) {
		List<String> ids = new ArrayList<>();
		for (int i = 0; i < ArtifactRules.SLOTS; i++) {
			ItemStack stack = artifacts.getItem(i);
			ids.add(stack.isEmpty() ? "" : BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
		}
		return ids;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return ArtifactRules.mayPlace(equippedIds(this.container), this.getContainerSlot(),
				BuiltInRegistries.ITEM.getKey(stack.getItem()).toString(), stack.getItem() instanceof ArtifactItem);
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public Identifier getNoItemIcon() {
		return SILHOUETTE;
	}
}
