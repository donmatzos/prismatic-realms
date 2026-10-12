package at.donmatzos.prismaticrealms.client.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.BlockReplacement;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.HeightMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import at.donmatzos.prismaticrealms.block.ModBlocks;
import at.donmatzos.prismaticrealms.world.ModWorldGen;

/** Ore features and placed features for the crystal ores; see docs/superpowers/specs/2026-10-11-crystal-ore-worldgen-design.md. */
public class ModWorldGenProvider extends FabricDynamicRegistryProvider {
	// Tuning. Global: ~1/4 of diamond everywhere. Boosted: ~1.5x diamond (redstone level) on top, in the family's biomes.
	private static final int GLOBAL_COUNT = 2;
	private static final int GLOBAL_SIZE = 5;
	private static final float GLOBAL_AIR_DISCARD = 0.5F;
	private static final int BOOSTED_COUNT = 12;
	private static final int BOOSTED_SIZE = 8;
	private static final float BOOSTED_AIR_DISCARD = 0.3F;

	/** Height band (triangle: most common in the middle) and boosted biomes per family. */
	record OreGen(String family, int minY, int maxY, List<ResourceKey<Biome>> biomes) {
	}

	static final List<OreGen> ORE_GEN = List.of(
			new OreGen("crimson_sanguis", -64, 0, List.of(Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.ERODED_BADLANDS, Biomes.PALE_GARDEN)),
			new OreGen("cruel_sun", 0, 96, List.of(Biomes.DESERT, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA, Biomes.DAPPLED_FOREST)),
			new OreGen("storm_fang", 48, 320, List.of(Biomes.STONY_PEAKS, Biomes.JAGGED_PEAKS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.MEADOW)),
			new OreGen("overgrown_prosperity", -16, 64, List.of(Biomes.LUSH_CAVES, Biomes.JUNGLE, Biomes.BAMBOO_JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.DARK_FOREST, Biomes.FLOWER_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA)),
			new OreGen("ice_star", 16, 256, List.of(Biomes.FROZEN_PEAKS, Biomes.ICE_SPIKES, Biomes.SNOWY_SLOPES, Biomes.GROVE, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.FROZEN_RIVER, Biomes.SNOWY_BEACH)),
			new OreGen("cerulean_abyss", -64, 32, List.of(Biomes.DEEP_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.OCEAN, Biomes.COLD_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.WARM_OCEAN)),
			new OreGen("venom_crown", -48, 48, List.of(Biomes.SWAMP, Biomes.MANGROVE_SWAMP, Biomes.SULFUR_CAVES)));

	public ModWorldGenProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void configure(HolderLookup.Provider registries, Entries entries) {
		entries.addAll(registries.lookupOrThrow(Registries.FEATURE));
		entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
	}

	@Override
	public String getName() {
		return "Crystal ore world generation";
	}

	static OreGen oreGen(String family) {
		return ORE_GEN.stream().filter(gen -> gen.family().equals(family)).findFirst()
				.orElseThrow(() -> new IllegalStateException("No world generation entry for crystal ore family " + family));
	}

	public static void bootstrapFeatures(BootstrapContext<Feature> context) {
		// Same replaceable rules as vanilla ores: the stone variant above the deepslate transition, the deepslate variant below.
		RuleTest stone = RuleTest.either(new TagMatchTest(BlockTags.HEIGHT_SPECIFIC_ORE_REPLACEABLES), HeightMatchTest.min(0), new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES));
		RuleTest deepslate = RuleTest.either(new TagMatchTest(BlockTags.HEIGHT_SPECIFIC_ORE_REPLACEABLES), HeightMatchTest.max(8), new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES));
		for (ModBlocks.CrystalOre ore : ModBlocks.CRYSTAL_ORES) {
			oreGen(ore.family());   // fail early if a family has no world generation entry
			List<BlockReplacement> targets = List.of(
					BlockReplacement.replace(stone, ore.stone().defaultBlockState()),
					BlockReplacement.replace(deepslate, ore.deepslate().defaultBlockState()));
			context.register(ModWorldGen.oreFeature(ore.family(), false), new OreFeature(targets, GLOBAL_SIZE, GLOBAL_AIR_DISCARD));
			context.register(ModWorldGen.oreFeature(ore.family(), true), new OreFeature(targets, BOOSTED_SIZE, BOOSTED_AIR_DISCARD));
		}
	}

	public static void bootstrapPlacedFeatures(BootstrapContext<PlacedFeature> context) {
		HolderGetter<Feature> features = context.lookup(Registries.FEATURE);
		for (ModBlocks.CrystalOre ore : ModBlocks.CRYSTAL_ORES) {
			OreGen gen = oreGen(ore.family());
			HeightRangePlacement height = HeightRangePlacement.triangle(VerticalAnchor.absolute(gen.minY()), VerticalAnchor.absolute(gen.maxY()));
			context.register(ModWorldGen.orePlacement(ore.family(), false), new PlacedFeature(features.getOrThrow(ModWorldGen.oreFeature(ore.family(), false)),
					List.of(CountPlacement.of(GLOBAL_COUNT), InSquarePlacement.spread(), height, BiomeFilter.biome())));
			context.register(ModWorldGen.orePlacement(ore.family(), true), new PlacedFeature(features.getOrThrow(ModWorldGen.oreFeature(ore.family(), true)),
					List.of(CountPlacement.of(BOOSTED_COUNT), InSquarePlacement.spread(), height, BiomeFilter.biome())));
		}
	}
}
