package at.donmatzos.prismaticrealms;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import at.donmatzos.prismaticrealms.artifact.ArtifactEffects;
import at.donmatzos.prismaticrealms.artifact.ArtifactSlots;
import at.donmatzos.prismaticrealms.block.ModBlockEntities;
import at.donmatzos.prismaticrealms.block.ModBlocks;
import at.donmatzos.prismaticrealms.effect.ModEffects;
import at.donmatzos.prismaticrealms.item.ChargedHitScytheItem;
import at.donmatzos.prismaticrealms.item.ScytheEnchantments;
import at.donmatzos.prismaticrealms.item.ScytheHarvest;
import at.donmatzos.prismaticrealms.particle.ModParticles;
import at.donmatzos.prismaticrealms.item.ModCreativeTabs;
import at.donmatzos.prismaticrealms.item.ModItems;
import at.donmatzos.prismaticrealms.pouch.ModComponents;
import at.donmatzos.prismaticrealms.pouch.ModMenus;
import at.donmatzos.prismaticrealms.pouch.SelectPouchEntryPayload;
import at.donmatzos.prismaticrealms.recipe.ModRecipes;
import at.donmatzos.prismaticrealms.world.ModWorldGen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrismaticRealms implements ModInitializer {
	public static final String MOD_ID = "prismaticrealms";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModParticles.initialize();
		ModEffects.initialize();
		ModComponents.initialize();
		ModItems.initialize();
		ModRecipes.initialize();
		ModMenus.initialize();
		ArtifactSlots.initialize();
		ArtifactEffects.initialize();
		SelectPouchEntryPayload.initialize();
		ChargedHitScytheItem.initialize();
		ScytheHarvest.initialize();
		ScytheEnchantments.initialize();
		ModBlocks.initialize();
		ModBlockEntities.initialize();
		ModCreativeTabs.initialize();
		ModWorldGen.initialize();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
