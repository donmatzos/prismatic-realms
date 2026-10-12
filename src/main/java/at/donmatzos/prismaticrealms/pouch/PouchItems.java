package at.donmatzos.prismaticrealms.pouch;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.block.ModBlocks;

/** Slot index (see PouchStorage) <-> shard or crystal item, in ModBlocks.CRYSTAL_ORES (rainbow) order. */
public final class PouchItems {
	private static List<Item> items;

	private PouchItems() {
	}

	private static List<Item> items() {
		if (items == null) {
			List<Item> shards = ModBlocks.CRYSTAL_ORES.stream().map(ModBlocks.CrystalOre::shard).toList();
			List<Item> crystals = ModBlocks.CRYSTAL_ORES.stream()
					.map(ore -> BuiltInRegistries.ITEM.getValue(PrismaticRealms.id(ore.family() + "_crystal"))).toList();
			items = java.util.stream.Stream.concat(shards.stream(), crystals.stream()).toList();
		}
		return items;
	}

	public static Item item(int slot) {
		return items().get(slot);
	}

	/** The slot for this item, or -1 if the pouch doesn't store it. */
	public static int slotOf(Item item) {
		return items().indexOf(item);
	}
}
