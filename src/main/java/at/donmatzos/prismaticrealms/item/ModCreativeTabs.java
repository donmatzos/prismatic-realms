package at.donmatzos.prismaticrealms.item;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.block.ModBlocks;

public final class ModCreativeTabs {
	public static final ResourceKey<CreativeModeTab> PRISMATIC_REALMS_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, PrismaticRealms.id("prismatic_realms"));

	public static final CreativeModeTab PRISMATIC_REALMS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, PRISMATIC_REALMS_KEY, FabricCreativeModeTab.builder()
			.title(Component.translatable("itemGroup.prismaticrealms.prismatic_realms"))
			.icon(() -> new ItemStack(ModItems.ICE_STAR_CRYSTAL))
			.displayItems((parameters, output) -> {
				output.accept(ModItems.CRIMSON_SANGUIS_SHARD);
				output.accept(ModItems.CRIMSON_SANGUIS_CRYSTAL);
				output.accept(ModItems.CRUEL_SUN_SHARD);
				output.accept(ModItems.CRUEL_SUN_CRYSTAL);
				output.accept(ModItems.STORM_FANG_SHARD);
				output.accept(ModItems.STORM_FANG_CRYSTAL);
				output.accept(ModItems.OVERGROWN_PROSPERITY_SHARD);
				output.accept(ModItems.OVERGROWN_PROSPERITY_CRYSTAL);
				output.accept(ModItems.ICE_STAR_SHARD);
				output.accept(ModItems.ICE_STAR_CRYSTAL);
				output.accept(ModItems.CERULEAN_ABYSS_SHARD);
				output.accept(ModItems.CERULEAN_ABYSS_CRYSTAL);
				output.accept(ModItems.VENOM_CROWN_SHARD);
				output.accept(ModItems.VENOM_CROWN_CRYSTAL);
				output.accept(ModItems.SPECTRAL_BINDER);
				output.accept(ModItems.SPECTRAL_SETTING);
				output.accept(ModItems.WOODEN_SCYTHE);
				output.accept(ModItems.STONE_SCYTHE);
				output.accept(ModItems.COPPER_SCYTHE);
				output.accept(ModItems.GOLDEN_SCYTHE);
				output.accept(ModItems.IRON_SCYTHE);
				output.accept(ModItems.DIAMOND_SCYTHE);
				output.accept(ModItems.NETHERITE_SCYTHE);
				output.accept(ModItems.SCYTHE_OF_THE_FROZEN_EXPANSE);
				output.accept(ModItems.OVERGROWN_REAPER);
				output.accept(ModItems.CRIMSON_DIABELL);
				output.accept(ModItems.BLITZ_SLASHER);
				output.accept(ModItems.THE_SHALLOW_WATERS);
				output.accept(ModItems.MELTING_SUNRAY);
				output.accept(ModItems.SCORPION_TAIL);
				output.accept(ModItems.DOWSING_ROD);
				output.accept(ModItems.CRYSTAL_POUCH);
				output.accept(ModItems.CRYSTAL_POUCH_PLUS_ONE);
				output.accept(ModItems.CRYSTAL_POUCH_PLUS_TWO);
				output.accept(ModItems.CRYSTAL_POUCH_UPGRADE_PLUS_ONE);
				output.accept(ModItems.CRYSTAL_POUCH_UPGRADE_PLUS_TWO);
				output.accept(ModBlocks.SPECTRAL_ALTAR);
				output.accept(ModItems.FROSTWALKER_STAR);
				output.accept(ModItems.SUNBLESSED_TALISMAN);
				for (ModBlocks.CrystalOre ore : ModBlocks.CRYSTAL_ORES) {
					output.accept(ore.stone());
					output.accept(ore.deepslate());
				}
			})
			.build());

	private ModCreativeTabs() {
	}

	public static void initialize() {
	}
}
