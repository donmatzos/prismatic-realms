package at.donmatzos.prismaticrealms.block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.altar.SpectralAltarBlock;
import at.donmatzos.prismaticrealms.item.ModItems;

public final class ModBlocks {
	// Vanilla keeps its tab keys private, so build the key from the registry ID.
	private static final ResourceKey<CreativeModeTab> NATURAL_BLOCKS_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("natural_blocks"));
	private static final ResourceKey<CreativeModeTab> FUNCTIONAL_BLOCKS_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("functional_blocks"));

	/** One crystal family's ore pair and the shard it drops. */
	public record CrystalOre(String family, Block stone, Block deepslate, Item shard) {
	}

	// In rainbow order, matching the item and creative tab order.
	public static final List<CrystalOre> CRYSTAL_ORES = new ArrayList<>();
	// Declared before the ores below: static fields initialise in order.
	private static final Function<BlockBehaviour.Properties, Block> ORE = properties -> new DropExperienceBlock(UniformInt.of(3, 7), properties);

	public static final CrystalOre CRIMSON_SANGUIS_ORE = crystalOre("crimson_sanguis", ModItems.CRIMSON_SANGUIS_SHARD);
	public static final CrystalOre CRUEL_SUN_ORE = crystalOre("cruel_sun", ModItems.CRUEL_SUN_SHARD);
	public static final CrystalOre STORM_FANG_ORE = crystalOre("storm_fang", ModItems.STORM_FANG_SHARD);
	public static final CrystalOre OVERGROWN_PROSPERITY_ORE = crystalOre("overgrown_prosperity", ModItems.OVERGROWN_PROSPERITY_SHARD);
	public static final CrystalOre ICE_STAR_ORE = crystalOre("ice_star", ModItems.ICE_STAR_SHARD);
	public static final CrystalOre CERULEAN_ABYSS_ORE = crystalOre("cerulean_abyss", ModItems.CERULEAN_ABYSS_SHARD);
	public static final CrystalOre VENOM_CROWN_ORE = crystalOre("venom_crown", ModItems.VENOM_CROWN_SHARD);

	// Same properties as vanilla's enchanting table (any pickaxe, obsidian-tier blast resistance, light 7).
	public static final Block SPECTRAL_ALTAR = register("spectral_altar", SpectralAltarBlock::new, BlockBehaviour.Properties.of()
			.mapColor(MapColor.COLOR_PURPLE)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresCorrectToolForDrops()
			.lightLevel(state -> 7)
			.strength(5.0F, 1200.0F));
	// Lava frozen by the Frostwalker Star (RimedMagmaBlock): like frosted ice, but opaque, normal friction, faint glow.
	public static final Block RIMED_MAGMA = registerBlockOnly("rimed_magma", RimedMagmaBlock::new, BlockBehaviour.Properties.of()
			.mapColor(MapColor.NETHER)
			.strength(0.5F)
			.sound(SoundType.GLASS)
			.lightLevel(state -> 3)
			.isValidSpawn(Blocks::never)
			.isRedstoneConductor(Blocks::never));

	private ModBlocks() {
	}

	private static CrystalOre crystalOre(String family, Item shard) {
		// Same properties as vanilla's diamond ores: 3-7 XP, stone 3.0 / deepslate 4.5 hardness.
		Block stone = register(family + "_ore", ORE, BlockBehaviour.Properties.of()
				.mapColor(MapColor.STONE)
				.instrument(NoteBlockInstrument.BASEDRUM)
				.requiresCorrectToolForDrops()
				.strength(3.0F, 3.0F));
		Block deepslate = register("deepslate_" + family + "_ore", ORE, BlockBehaviour.Properties.of()
				.mapColor(MapColor.DEEPSLATE)
				.instrument(NoteBlockInstrument.BASEDRUM)
				.requiresCorrectToolForDrops()
				.strength(4.5F, 3.0F)
				.sound(SoundType.DEEPSLATE));
		CrystalOre ore = new CrystalOre(family, stone, deepslate, shard);
		CRYSTAL_ORES.add(ore);
		return ore;
	}

	private static Block register(String path, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
		ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, PrismaticRealms.id(path));
		Block block = Registry.register(BuiltInRegistries.BLOCK, key, factory.apply(properties.setId(key)));
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, PrismaticRealms.id(path));
		Registry.register(BuiltInRegistries.ITEM, itemKey, new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));
		return block;
	}

	/** A block with no block item (not obtainable). */
	private static Block registerBlockOnly(String path, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
		ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, PrismaticRealms.id(path));
		return Registry.register(BuiltInRegistries.BLOCK, key, factory.apply(properties.setId(key)));
	}

	public static void initialize() {
		CreativeModeTabEvents.modifyOutputEvent(FUNCTIONAL_BLOCKS_TAB).register(output -> output.insertAfter(Items.ENCHANTING_TABLE, SPECTRAL_ALTAR));
		CreativeModeTabEvents.modifyOutputEvent(NATURAL_BLOCKS_TAB).register(output -> {
			Item after = Items.DEEPSLATE_EMERALD_ORE;
			for (CrystalOre ore : CRYSTAL_ORES) {
				output.insertAfter(after, ore.stone(), ore.deepslate());
				after = ore.deepslate().asItem();
			}
		});
	}
}
