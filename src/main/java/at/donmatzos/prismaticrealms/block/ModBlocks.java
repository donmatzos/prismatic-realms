package at.donmatzos.prismaticrealms.block;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.item.ModItems;

public final class ModBlocks {
	// Vanilla keeps its tab keys private, so build the key from the registry ID.
	private static final ResourceKey<CreativeModeTab> NATURAL_BLOCKS_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("natural_blocks"));

	/** One crystal family's ore pair and the shard it drops. */
	public record CrystalOre(String family, Block stone, Block deepslate, Item shard) {
	}

	// In rainbow order, matching the item and creative tab order.
	public static final List<CrystalOre> CRYSTAL_ORES = new ArrayList<>();

	public static final CrystalOre CRIMSON_SANGUIS_ORE = crystalOre("crimson_sanguis", ModItems.CRIMSON_SANGUIS_SHARD);
	public static final CrystalOre CRUEL_SUN_ORE = crystalOre("cruel_sun", ModItems.CRUEL_SUN_SHARD);
	public static final CrystalOre STORM_FANG_ORE = crystalOre("storm_fang", ModItems.STORM_FANG_SHARD);
	public static final CrystalOre OVERGROWN_PROSPERITY_ORE = crystalOre("overgrown_prosperity", ModItems.OVERGROWN_PROSPERITY_SHARD);
	public static final CrystalOre ICE_STAR_ORE = crystalOre("ice_star", ModItems.ICE_STAR_SHARD);
	public static final CrystalOre CERULEAN_ABYSS_ORE = crystalOre("cerulean_abyss", ModItems.CERULEAN_ABYSS_SHARD);
	public static final CrystalOre VENOM_CROWN_ORE = crystalOre("venom_crown", ModItems.VENOM_CROWN_SHARD);

	private ModBlocks() {
	}

	private static CrystalOre crystalOre(String family, Item shard) {
		// Same properties as vanilla's diamond ores: 3-7 XP, stone 3.0 / deepslate 4.5 hardness.
		Block stone = register(family + "_ore", BlockBehaviour.Properties.of()
				.mapColor(MapColor.STONE)
				.instrument(NoteBlockInstrument.BASEDRUM)
				.requiresCorrectToolForDrops()
				.strength(3.0F, 3.0F));
		Block deepslate = register("deepslate_" + family + "_ore", BlockBehaviour.Properties.of()
				.mapColor(MapColor.DEEPSLATE)
				.instrument(NoteBlockInstrument.BASEDRUM)
				.requiresCorrectToolForDrops()
				.strength(4.5F, 3.0F)
				.sound(SoundType.DEEPSLATE));
		CrystalOre ore = new CrystalOre(family, stone, deepslate, shard);
		CRYSTAL_ORES.add(ore);
		return ore;
	}

	private static Block register(String path, BlockBehaviour.Properties properties) {
		ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, PrismaticRealms.id(path));
		Block block = Registry.register(BuiltInRegistries.BLOCK, key, new DropExperienceBlock(UniformInt.of(3, 7), properties.setId(key)));
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, PrismaticRealms.id(path));
		Registry.register(BuiltInRegistries.ITEM, itemKey, new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));
		return block;
	}

	public static void initialize() {
		CreativeModeTabEvents.modifyOutputEvent(NATURAL_BLOCKS_TAB).register(output -> {
			Item after = Items.DEEPSLATE_EMERALD_ORE;
			for (CrystalOre ore : CRYSTAL_ORES) {
				output.insertAfter(after, ore.stone(), ore.deepslate());
				after = ore.deepslate().asItem();
			}
		});
	}
}
