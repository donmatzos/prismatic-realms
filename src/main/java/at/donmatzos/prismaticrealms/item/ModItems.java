package at.donmatzos.prismaticrealms.item;

import java.util.function.Function;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.AttackRange;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.artifact.AbyssalHeartItem;
import at.donmatzos.prismaticrealms.artifact.FrostwalkerStarItem;
import at.donmatzos.prismaticrealms.artifact.StormfathersHeirloomItem;
import at.donmatzos.prismaticrealms.artifact.SunblessedTalismanItem;
import at.donmatzos.prismaticrealms.pouch.ModComponents;
import at.donmatzos.prismaticrealms.pouch.PouchContents;

public final class ModItems {
	// Vanilla keeps its tab keys private, so build the key from the registry ID.
	private static final ResourceKey<CreativeModeTab> INGREDIENTS_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("ingredients"));
	private static final ResourceKey<CreativeModeTab> COMBAT_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("combat"));
	private static final ResourceKey<CreativeModeTab> TOOLS_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("tools_and_utilities"));

	public static final Item CRIMSON_SANGUIS_SHARD = register("crimson_sanguis_shard", Item::new, new Item.Properties());
	public static final Item CRIMSON_SANGUIS_CRYSTAL = register("crimson_sanguis_crystal", Item::new, new Item.Properties());
	public static final Item CRUEL_SUN_SHARD = register("cruel_sun_shard", Item::new, new Item.Properties());
	public static final Item CRUEL_SUN_CRYSTAL = register("cruel_sun_crystal", Item::new, new Item.Properties());
	public static final Item STORM_FANG_SHARD = register("storm_fang_shard", Item::new, new Item.Properties());
	public static final Item STORM_FANG_CRYSTAL = register("storm_fang_crystal", Item::new, new Item.Properties());
	public static final Item OVERGROWN_PROSPERITY_SHARD = register("overgrown_prosperity_shard", Item::new, new Item.Properties());
	public static final Item OVERGROWN_PROSPERITY_CRYSTAL = register("overgrown_prosperity_crystal", Item::new, new Item.Properties());
	public static final Item ICE_STAR_SHARD = register("ice_star_shard", Item::new, new Item.Properties());
	public static final Item ICE_STAR_CRYSTAL = register("ice_star_crystal", Item::new, new Item.Properties());
	public static final Item CERULEAN_ABYSS_SHARD = register("cerulean_abyss_shard", Item::new, new Item.Properties());
	public static final Item CERULEAN_ABYSS_CRYSTAL = register("cerulean_abyss_crystal", Item::new, new Item.Properties());
	public static final Item VENOM_CROWN_SHARD = register("venom_crown_shard", Item::new, new Item.Properties());
	public static final Item VENOM_CROWN_CRYSTAL = register("venom_crown_crystal", Item::new, new Item.Properties());
	public static final Item SPECTRAL_BINDER = register("spectral_binder", Item::new, new Item.Properties());
	// Shared base of every artifact (centre slot of the Spectral Altar); crafted from gold around a binder.
	public static final Item SPECTRAL_SETTING = register("spectral_setting", Item::new, new Item.Properties());

	// Scythes: "heavy reaper" stats: sword damage +1, axe-like swing speed (1.0/s), +0.5 block reach. Sweeping comes from #minecraft:swords.
	public static final Item WOODEN_SCYTHE = scythe("wooden_scythe", ToolMaterial.WOOD);
	public static final Item STONE_SCYTHE = scythe("stone_scythe", ToolMaterial.STONE);
	public static final Item COPPER_SCYTHE = scythe("copper_scythe", ToolMaterial.COPPER);
	public static final Item GOLDEN_SCYTHE = scythe("golden_scythe", ToolMaterial.GOLD);
	public static final Item IRON_SCYTHE = scythe("iron_scythe", ToolMaterial.IRON);
	public static final Item DIAMOND_SCYTHE = scythe("diamond_scythe", ToolMaterial.DIAMOND);
	public static final Item NETHERITE_SCYTHE = register("netherite_scythe", Item::new, scytheProperties(ToolMaterial.NETHERITE).fireResistant());
	// Netherite scythe stats, repaired with Ice Star Crystals, plus Freeze on fully charged hits (FrozenExpanseScytheItem).
	public static final Item SCYTHE_OF_THE_FROZEN_EXPANSE = register("scythe_of_the_frozen_expanse", FrozenExpanseScytheItem::new, scytheProperties(ToolMaterial.NETHERITE).repairable(ICE_STAR_CRYSTAL));
	// Netherite scythe stats, repaired with Overgrown Prosperity Crystals, plus Leech Seed on fully charged hits (OvergrownReaperItem).
	public static final Item OVERGROWN_REAPER = register("overgrown_reaper", OvergrownReaperItem::new, scytheProperties(ToolMaterial.NETHERITE).repairable(OVERGROWN_PROSPERITY_CRYSTAL));
	// Netherite scythe stats, repaired with Crimson Sanguis Crystals, plus Bleed on fully charged hits (CrimsonDiabellItem).
	public static final Item CRIMSON_DIABELL = register("crimson_diabell", CrimsonDiabellItem::new, scytheProperties(ToolMaterial.NETHERITE).repairable(CRIMSON_SANGUIS_CRYSTAL));
	// Netherite scythe stats, repaired with Storm Fang Crystals, plus Blitz Rush for the wielder on fully charged hits (BlitzSlasherItem).
	public static final Item BLITZ_SLASHER = register("blitz_slasher", BlitzSlasherItem::new, scytheProperties(ToolMaterial.NETHERITE).repairable(STORM_FANG_CRYSTAL));
	// Netherite scythe stats, repaired with Cerulean Abyss Crystals, plus Tidecaller for the wielder on fully charged hits (ShallowWatersItem).
	public static final Item THE_SHALLOW_WATERS = register("the_shallow_waters", ShallowWatersItem::new, scytheProperties(ToolMaterial.NETHERITE).repairable(CERULEAN_ABYSS_CRYSTAL));
	// Netherite scythe stats, repaired with Cruel Sun Crystals, plus Radiance for the wielder on fully charged hits (MeltingSunrayItem).
	public static final Item MELTING_SUNRAY = register("melting_sunray", MeltingSunrayItem::new, scytheProperties(ToolMaterial.NETHERITE).repairable(CRUEL_SUN_CRYSTAL));
	// Netherite scythe stats, repaired with Venom Crown Crystals, plus Venom Stack on fully charged hits (ScorpionTailItem).
	public static final Item SCORPION_TAIL = register("scorpion_tail", ScorpionTailItem::new, scytheProperties(ToolMaterial.NETHERITE).repairable(VENOM_CROWN_CRYSTAL));
	// Finds the nearest ore matching the off-hand item (DowsingRodItem): iron-tool durability, repaired with the binder.
	public static final Item DOWSING_ROD = register("dowsing_rod", DowsingRodItem::new,
			new Item.Properties().durability(250).repairable(SPECTRAL_BINDER).enchantable(14));
	// Crystal Pouch (CrystalPouchItem): per-family shard and crystal storage; +1 and +2 double the capacity twice (smithing).
	public static final Item CRYSTAL_POUCH = register("crystal_pouch", p -> new CrystalPouchItem(p, 0),
			new Item.Properties().stacksTo(1).component(ModComponents.POUCH_CONTENTS, PouchContents.EMPTY));
	public static final Item CRYSTAL_POUCH_PLUS_ONE = register("crystal_pouch_plus_one", p -> new CrystalPouchItem(p, 1),
			new Item.Properties().stacksTo(1).component(ModComponents.POUCH_CONTENTS, PouchContents.EMPTY)
					.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));
	public static final Item CRYSTAL_POUCH_PLUS_TWO = register("crystal_pouch_plus_two", p -> new CrystalPouchItem(p, 2),
			new Item.Properties().stacksTo(1).fireResistant().component(ModComponents.POUCH_CONTENTS, PouchContents.EMPTY)
					.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));
	// Smithing templates for the pouch upgrades (used up like vanilla's).
	public static final Item CRYSTAL_POUCH_UPGRADE_PLUS_ONE = register("crystal_pouch_upgrade_plus_one", Item::new, new Item.Properties());
	public static final Item CRYSTAL_POUCH_UPGRADE_PLUS_TWO = register("crystal_pouch_upgrade_plus_two", Item::new,
			new Item.Properties().component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));
	// Ice Star artifact (FrostwalkerStarItem): freezes water and lava underfoot; crafted at the Spectral Altar.
	public static final Item FROSTWALKER_STAR = register("frostwalker_star", FrostwalkerStarItem::new, new Item.Properties());
	// Cruel Sun artifact (SunblessedTalismanItem): mined blocks drop smelted; immune to fire and lava.
	public static final Item SUNBLESSED_TALISMAN = register("sunblessed_talisman", SunblessedTalismanItem::new, new Item.Properties());
	// Storm Fang artifact (StormfathersHeirloomItem): +50 % movement speed (adds flat with Blitz Rush) and storm step.
	public static final Item STORMFATHERS_HEIRLOOM = register("stormfathers_heirloom", StormfathersHeirloomItem::new, new Item.Properties());
	// Cerulean Abyss artifact (AbyssalHeartItem): endless breath, full walking speed and Dolphin's Grace in water.
	public static final Item ABYSSAL_HEART = register("abyssal_heart", AbyssalHeartItem::new, new Item.Properties());

	private ModItems() {
	}

	private static <T extends Item> T register(String path, Function<Item.Properties, T> factory, Item.Properties properties) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, PrismaticRealms.id(path));
		return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(properties.setId(key)));
	}

	private static final float SCYTHE_DAMAGE_BASELINE = 4.0F;   // vanilla swords use 3.0
	private static final float SCYTHE_SPEED_BASELINE = -3.0F;   // 4.0 - 3.0 = 1.0 attacks/second (swords: -2.4)
	private static final float SCYTHE_REACH = 3.5F;             // survival reach; the player default is 3.0
	private static final float SCYTHE_CREATIVE_REACH = 5.5F;    // creative default is 5.0

	private static Item.Properties scytheProperties(ToolMaterial material) {
		return new Item.Properties()
				.sword(material, SCYTHE_DAMAGE_BASELINE, SCYTHE_SPEED_BASELINE)
				.component(DataComponents.ATTACK_RANGE, new AttackRange(0.0F, SCYTHE_REACH, 0.0F, SCYTHE_CREATIVE_REACH, 0.3F, 1.0F));
	}

	private static Item scythe(String path, ToolMaterial material) {
		return register(path, Item::new, scytheProperties(material));
	}

	public static void initialize() {
		// each scythe right after the sword of the same material
		CreativeModeTabEvents.modifyOutputEvent(COMBAT_TAB).register(output -> {
			output.insertAfter(Items.WOODEN_SWORD, WOODEN_SCYTHE);
			output.insertAfter(Items.STONE_SWORD, STONE_SCYTHE);
			output.insertAfter(Items.COPPER_SWORD, COPPER_SCYTHE);
			output.insertAfter(Items.GOLDEN_SWORD, GOLDEN_SCYTHE);
			output.insertAfter(Items.IRON_SWORD, IRON_SCYTHE);
			output.insertAfter(Items.DIAMOND_SWORD, DIAMOND_SCYTHE);
			output.insertAfter(Items.NETHERITE_SWORD, NETHERITE_SCYTHE, SCYTHE_OF_THE_FROZEN_EXPANSE, OVERGROWN_REAPER, CRIMSON_DIABELL, BLITZ_SLASHER, THE_SHALLOW_WATERS, MELTING_SUNRAY, SCORPION_TAIL);
		});
		CreativeModeTabEvents.modifyOutputEvent(INGREDIENTS_TAB).register(output -> output.insertAfter(Items.AMETHYST_SHARD, CRIMSON_SANGUIS_SHARD, CRIMSON_SANGUIS_CRYSTAL, CRUEL_SUN_SHARD, CRUEL_SUN_CRYSTAL, STORM_FANG_SHARD, STORM_FANG_CRYSTAL, OVERGROWN_PROSPERITY_SHARD, OVERGROWN_PROSPERITY_CRYSTAL, ICE_STAR_SHARD, ICE_STAR_CRYSTAL, CERULEAN_ABYSS_SHARD, CERULEAN_ABYSS_CRYSTAL, VENOM_CROWN_SHARD, VENOM_CROWN_CRYSTAL, SPECTRAL_BINDER, SPECTRAL_SETTING));
		CreativeModeTabEvents.modifyOutputEvent(TOOLS_TAB).register(output -> output.insertAfter(Items.RECOVERY_COMPASS, DOWSING_ROD));
		CreativeModeTabEvents.modifyOutputEvent(TOOLS_TAB).register(output -> output.insertAfter(Items.RECOVERY_COMPASS, FROSTWALKER_STAR, SUNBLESSED_TALISMAN, STORMFATHERS_HEIRLOOM, ABYSSAL_HEART));
		CreativeModeTabEvents.modifyOutputEvent(TOOLS_TAB).register(output -> output.insertAfter(Items.BUNDLE, CRYSTAL_POUCH, CRYSTAL_POUCH_PLUS_ONE, CRYSTAL_POUCH_PLUS_TWO));
		CreativeModeTabEvents.modifyOutputEvent(INGREDIENTS_TAB).register(output -> output.insertAfter(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, CRYSTAL_POUCH_UPGRADE_PLUS_ONE, CRYSTAL_POUCH_UPGRADE_PLUS_TWO));
	}
}
