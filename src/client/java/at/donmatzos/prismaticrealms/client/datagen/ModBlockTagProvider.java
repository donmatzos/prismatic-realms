package at.donmatzos.prismaticrealms.client.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import at.donmatzos.prismaticrealms.block.ModBlocks;
import at.donmatzos.prismaticrealms.tag.ModTags;

public class ModBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
	public ModBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		for (ModBlocks.CrystalOre ore : ModBlocks.CRYSTAL_ORES) {
			for (Block block : new Block[] {ore.stone(), ore.deepslate()}) {
				ResourceKey<Block> key = BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
				// Any pickaxe mines them, so no needs_*_tool tag.
				this.builder(BlockTags.MINEABLE_WITH_PICKAXE).add(key);
				this.builder(ModTags.CRYSTAL_ORES).add(key);
			}
		}
		// Any pickaxe, like the enchanting table.
		this.builder(BlockTags.MINEABLE_WITH_PICKAXE).add(BuiltInRegistries.BLOCK.getResourceKey(ModBlocks.SPECTRAL_ALTAR).orElseThrow());
	}
}
