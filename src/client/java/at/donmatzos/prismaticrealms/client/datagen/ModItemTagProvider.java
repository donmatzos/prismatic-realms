package at.donmatzos.prismaticrealms.client.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import at.donmatzos.prismaticrealms.item.ModItems;
import at.donmatzos.prismaticrealms.tag.ModTags;
import net.minecraft.tags.ItemTags;

public class ModItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
	public ModItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		this.builder(ModTags.LOW_TIER_PICKAXES).add(key(Items.WOODEN_PICKAXE), key(Items.STONE_PICKAXE), key(Items.COPPER_PICKAXE));
		// Golden counts as high tier on purpose, although it mines at wood level in vanilla.
		Item[] scythes = {ModItems.WOODEN_SCYTHE, ModItems.STONE_SCYTHE, ModItems.COPPER_SCYTHE, ModItems.GOLDEN_SCYTHE, ModItems.IRON_SCYTHE, ModItems.DIAMOND_SCYTHE, ModItems.NETHERITE_SCYTHE, ModItems.SCYTHE_OF_THE_FROZEN_EXPANSE, ModItems.OVERGROWN_REAPER, ModItems.CRIMSON_DIABELL, ModItems.BLITZ_SLASHER, ModItems.THE_SHALLOW_WATERS, ModItems.MELTING_SUNRAY, ModItems.SCORPION_TAIL};
		for (Item scythe : scythes) {
			this.builder(ModTags.SCYTHES).add(key(scythe));
		}
		// In 26.3 the sweep attack and the sword enchantments (Sharpness, Sweeping Edge, Looting, ...) key off #minecraft:swords.
		this.builder(ItemTags.SWORDS).addTag(ModTags.SCYTHES);
		// Dowsing Rod: Efficiency (adds coordinates) via the mining tag, which only Efficiency uses in 26.3;
		// Unbreaking and Mending via the durability tag.
		this.builder(ItemTags.MINING_ENCHANTABLE).add(key(ModItems.DOWSING_ROD));
		this.builder(ItemTags.DURABILITY_ENCHANTABLE).add(key(ModItems.DOWSING_ROD));
		this.builder(ModTags.HIGH_TIER_PICKAXES).add(key(Items.GOLDEN_PICKAXE), key(Items.IRON_PICKAXE), key(Items.DIAMOND_PICKAXE), key(Items.NETHERITE_PICKAXE));
	}

	private static ResourceKey<Item> key(Item item) {
		return BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow();
	}
}
