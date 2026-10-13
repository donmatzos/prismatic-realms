package at.donmatzos.prismaticrealms.client.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import net.minecraft.core.HolderLookup;

import at.donmatzos.prismaticrealms.block.ModBlocks;
import at.donmatzos.prismaticrealms.effect.ModEffects;
import at.donmatzos.prismaticrealms.item.ModCreativeTabs;
import at.donmatzos.prismaticrealms.item.ModItems;

// All English names live here; the generated en_us.json replaces the old hand-written one.
public class ModLanguageProvider extends FabricLanguageProvider {
	public ModLanguageProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	public void generateTranslations(HolderLookup.Provider registries, TranslationBuilder translations) {
		translations.addCreativeModeTab(ModCreativeTabs.PRISMATIC_REALMS_KEY, "Prismatic Realms");

		translations.add(ModItems.CRIMSON_SANGUIS_SHARD, "Crimson Sanguis Shard");
		translations.add(ModItems.CRIMSON_SANGUIS_CRYSTAL, "Crimson Sanguis Crystal");
		translations.add(ModItems.CRUEL_SUN_SHARD, "Cruel Sun Shard");
		translations.add(ModItems.CRUEL_SUN_CRYSTAL, "Cruel Sun Crystal");
		translations.add(ModItems.STORM_FANG_SHARD, "Storm Fang Shard");
		translations.add(ModItems.STORM_FANG_CRYSTAL, "Storm Fang Crystal");
		translations.add(ModItems.OVERGROWN_PROSPERITY_SHARD, "Overgrown Prosperity Shard");
		translations.add(ModItems.OVERGROWN_PROSPERITY_CRYSTAL, "Overgrown Prosperity Crystal");
		translations.add(ModItems.ICE_STAR_SHARD, "Ice Star Shard");
		translations.add(ModItems.ICE_STAR_CRYSTAL, "Ice Star Crystal");
		translations.add(ModItems.CERULEAN_ABYSS_SHARD, "Cerulean Abyss Shard");
		translations.add(ModItems.CERULEAN_ABYSS_CRYSTAL, "Cerulean Abyss Crystal");
		translations.add(ModItems.VENOM_CROWN_SHARD, "Venom Crown Shard");
		translations.add(ModItems.VENOM_CROWN_CRYSTAL, "Venom Crown Crystal");
		translations.add(ModItems.SPECTRAL_BINDER, "Spectral Binder");
		translations.add(ModItems.SPECTRAL_SETTING, "Spectral Setting");
		translations.add(ModItems.WOODEN_SCYTHE, "Wooden Scythe");
		translations.add(ModItems.STONE_SCYTHE, "Stone Scythe");
		translations.add(ModItems.COPPER_SCYTHE, "Copper Scythe");
		translations.add(ModItems.GOLDEN_SCYTHE, "Golden Scythe");
		translations.add(ModItems.IRON_SCYTHE, "Iron Scythe");
		translations.add(ModItems.DIAMOND_SCYTHE, "Diamond Scythe");
		translations.add(ModItems.NETHERITE_SCYTHE, "Netherite Scythe");
		translations.add(ModItems.SCYTHE_OF_THE_FROZEN_EXPANSE, "Scythe of the Frozen Expanse");
		translations.add(ModEffects.FREEZE.value(), "Freeze");
		translations.add(ModItems.OVERGROWN_REAPER, "Overgrown Reaper");
		translations.add(ModEffects.LEECH_SEED.value(), "Leech Seed");
		translations.add(ModItems.CRIMSON_DIABELL, "Crimson Diabell");
		translations.add(ModEffects.BLEED.value(), "Bleed");
		translations.add(ModItems.BLITZ_SLASHER, "Blitz Slasher");
		translations.add(ModEffects.BLITZ_RUSH.value(), "Blitz Rush");
		translations.add(ModItems.THE_SHALLOW_WATERS, "The Shallow Waters");
		translations.add(ModEffects.TIDECALLER.value(), "Tidecaller");
		translations.add(ModItems.MELTING_SUNRAY, "Melting Sunray");
		translations.add(ModEffects.RADIANCE.value(), "Radiance");
		translations.add(ModItems.SCORPION_TAIL, "Scorpion Tail");
		translations.add(ModItems.DOWSING_ROD, "Dowsing Rod");
		translations.add("item.prismaticrealms.dowsing_rod.tooltip", "Right-click to find the ore held in your off hand");
		translations.add(ModItems.CRYSTAL_POUCH, "Crystal Pouch");
		translations.add(ModItems.CRYSTAL_POUCH_PLUS_ONE, "Crystal Pouch +1");
		translations.add(ModItems.CRYSTAL_POUCH_PLUS_TWO, "Crystal Pouch +2");
		translations.add(ModItems.CRYSTAL_POUCH_UPGRADE_PLUS_ONE, "Crystal Pouch Upgrade +1");
		translations.add(ModItems.CRYSTAL_POUCH_UPGRADE_PLUS_TWO, "Crystal Pouch Upgrade +2");
		translations.add("item.prismaticrealms.crystal_pouch.capacity", "Holds %1$s shards and %2$s crystals per family");
		translations.add("item.prismaticrealms.crystal_pouch.stored", "Stored: %1$s shards · %2$s crystals");
		translations.add("item.prismaticrealms.crystal_pouch_upgrade.tooltip", "Smithing template for the Crystal Pouch");
		translations.add("container.prismaticrealms.crystal_pouch.amount", "%1$s: %2$s / %3$s");
		translations.add(ModItems.FROSTWALKER_STAR, "Frostwalker Star");
		translations.add("item.prismaticrealms.frostwalker_star.bonus", "Frostwalker Star – Freezes water and lava beneath you; immune to hot floors and freezing.");
		translations.add(ModItems.SUNBLESSED_TALISMAN, "Sunblessed Talisman");
		translations.add("item.prismaticrealms.sunblessed_talisman.bonus", "Sunblessed Talisman – Mined blocks drop smelted; immune to fire and lava.");
		translations.add("item.prismaticrealms.artifact", "Artifact (wear it in an artifact slot)");
		translations.add("container.prismaticrealms.artifacts", "Artifact bonuses");
		translations.add("container.prismaticrealms.artifacts.none", "No artifacts equipped");
		translations.add("container.prismaticrealms.spectral_altar", "Spectral Altar");
		translations.add("container.prismaticrealms.spectral_altar.needs_fuel", "Needs 3 shards of fuel");
		translations.add("dowsing.prismaticrealms.found", "%1$s: %2$s blocks · %3$s · %4$s");
		translations.add("dowsing.prismaticrealms.coords", " (x %1$s, y %2$s, z %3$s)");
		translations.add("dowsing.prismaticrealms.none", "No %1$s within %2$s blocks");
		translations.add("dowsing.prismaticrealms.hint", "Hold a shard or an ore in your off hand");
		translations.add("dowsing.prismaticrealms.direction.north", "north");
		translations.add("dowsing.prismaticrealms.direction.north_east", "north-east");
		translations.add("dowsing.prismaticrealms.direction.east", "east");
		translations.add("dowsing.prismaticrealms.direction.south_east", "south-east");
		translations.add("dowsing.prismaticrealms.direction.south", "south");
		translations.add("dowsing.prismaticrealms.direction.south_west", "south-west");
		translations.add("dowsing.prismaticrealms.direction.west", "west");
		translations.add("dowsing.prismaticrealms.direction.north_west", "north-west");
		translations.add("dowsing.prismaticrealms.direction.straight", "straight");
		translations.add("dowsing.prismaticrealms.height.above", "%s above");
		translations.add("dowsing.prismaticrealms.height.below", "%s below");
		translations.add("dowsing.prismaticrealms.height.level", "level");
		translations.add(ModEffects.VENOM_STACK.value(), "Venom Stack");
		// tooltips of the unique scythes (ChargedHitScytheItem)
		translations.add("item.prismaticrealms.charged_hit", "Charged hit:");
		translations.add("item.prismaticrealms.scythe.harvest", "Right-click ripe crops: double harvest, auto-replant");
		translations.add("item.prismaticrealms.scythe_of_the_frozen_expanse.effect", "Freeze – slows the target to 25% speed for 3s");
		translations.add("item.prismaticrealms.scorpion_tail.effect", "Venom Stack – each hit adds a stack (up to 5) for 6s; more stacks, more poison damage");
		translations.add("item.prismaticrealms.melting_sunray.effect", "Radiance – Fire Resistance for 6s; whoever hurts you catches fire");
		translations.add("item.prismaticrealms.the_shallow_waters.effect", "Tidecaller – Dolphin's Grace and Water Breathing for 8s; in water, hits heal you");
		translations.add("item.prismaticrealms.blitz_slasher.effect", "Blitz Rush – you move 50% faster for 3s");
		translations.add("item.prismaticrealms.crimson_diabell.effect", "Bleed – 1 damage per second for 5s, 2 while the target keeps moving");
		translations.add("item.prismaticrealms.overgrown_reaper.effect", "Leech Seed – drains 1 health per second for 5s and heals you by the same");

		for (ModBlocks.CrystalOre ore : ModBlocks.CRYSTAL_ORES) {
			String name = familyName(ore.family());
			translations.add(ore.stone(), name + " Ore");
			translations.add(ore.deepslate(), "Deepslate " + name + " Ore");
		}
		translations.add(ModBlocks.SPECTRAL_ALTAR, "Spectral Altar");
		translations.add(ModBlocks.RIMED_MAGMA, "Rimed Magma");
	}

	// "cerulean_abyss" -> "Cerulean Abyss"
	private static String familyName(String family) {
		StringBuilder name = new StringBuilder();
		for (String word : family.split("_")) {
			if (!name.isEmpty()) {
				name.append(' ');
			}
			name.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
		}
		return name.toString();
	}
}
