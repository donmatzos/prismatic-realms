package at.donmatzos.prismaticrealms.client.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;

import at.donmatzos.prismaticrealms.world.ModWorldGen;

// has_crystal_ore/<family>: the biomes where a family's ore gets its boosted placement.
public class ModBiomeTagProvider extends FabricTagsProvider<Biome> {
	public ModBiomeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, Registries.BIOME, registries);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		for (ModWorldGenProvider.OreGen gen : ModWorldGenProvider.ORE_GEN) {
			this.builder(ModWorldGen.boostedBiomes(gen.family())).addAll(gen.biomes());
		}
	}
}
