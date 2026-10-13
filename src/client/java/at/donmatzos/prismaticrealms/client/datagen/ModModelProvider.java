package at.donmatzos.prismaticrealms.client.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

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
		// Four age stages like vanilla's frosted ice (textures rimed_magma_0..3).
		generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.RIMED_MAGMA)
				.with(PropertyDispatch.<Integer>initial(BlockStateProperties.AGE_3)
						.select(0, BlockModelGenerators.plainVariant(generators.createSuffixedVariant(ModBlocks.RIMED_MAGMA, "_0", ModelTemplates.CUBE_ALL, TextureMapping::cube)))
						.select(1, BlockModelGenerators.plainVariant(generators.createSuffixedVariant(ModBlocks.RIMED_MAGMA, "_1", ModelTemplates.CUBE_ALL, TextureMapping::cube)))
						.select(2, BlockModelGenerators.plainVariant(generators.createSuffixedVariant(ModBlocks.RIMED_MAGMA, "_2", ModelTemplates.CUBE_ALL, TextureMapping::cube)))
						.select(3, BlockModelGenerators.plainVariant(generators.createSuffixedVariant(ModBlocks.RIMED_MAGMA, "_3", ModelTemplates.CUBE_ALL, TextureMapping::cube)))));
	}

	@Override
	public void generateItemModels(ItemModelGenerators generators) {
	}
}
