package at.donmatzos.prismaticrealms.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

/** Run with {@code ./gradlew runDatagen}; writes to {@code src/main/generated/}. */
public class PrismaticRealmsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();
		pack.addProvider(ModBlockLootProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModLanguageProvider::new);
		pack.addProvider(ModWorldGenProvider::new);
		pack.addProvider(ModBiomeTagProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.FEATURE, ModWorldGenProvider::bootstrapFeatures);
		registryBuilder.add(Registries.PLACED_FEATURE, ModWorldGenProvider::bootstrapPlacedFeatures);
	}
}
