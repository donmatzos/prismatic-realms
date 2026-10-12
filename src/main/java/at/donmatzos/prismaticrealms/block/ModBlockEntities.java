package at.donmatzos.prismaticrealms.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.altar.SpectralAltarBlockEntity;

public final class ModBlockEntities {
	public static final BlockEntityType<SpectralAltarBlockEntity> SPECTRAL_ALTAR = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, PrismaticRealms.id("spectral_altar"),
			FabricBlockEntityTypeBuilder.create(SpectralAltarBlockEntity::new, ModBlocks.SPECTRAL_ALTAR).build());

	private ModBlockEntities() {
	}

	public static void initialize() {
	}
}
