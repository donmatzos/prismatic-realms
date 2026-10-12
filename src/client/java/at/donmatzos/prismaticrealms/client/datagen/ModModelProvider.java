package at.donmatzos.prismaticrealms.client.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelLocationUtils;

import at.donmatzos.prismaticrealms.block.ModBlocks;

// Item models for plain items stay hand-written in src/main/resources; this only covers blocks.
public class ModModelProvider extends FabricModelProvider {
	public ModModelProvider(FabricPackOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators generators) {
		for (ModBlocks.CrystalOre ore : ModBlocks.CRYSTAL_ORES) {
			generators.createTrivialCube(ore.stone());
			generators.createTrivialCube(ore.deepslate());
		}
		// The block model is hand-written (enchanting-table shape); only the blockstate and item definition are generated.
		generators.createNonTemplateModelBlock(ModBlocks.SPECTRAL_ALTAR);
		generators.registerSimpleItemModel(ModBlocks.SPECTRAL_ALTAR, ModelLocationUtils.getModelLocation(ModBlocks.SPECTRAL_ALTAR));
	}

	@Override
	public void generateItemModels(ItemModelGenerators generators) {
	}
}
