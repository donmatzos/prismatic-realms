package at.donmatzos.prismaticrealms.pouch;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.altar.SpectralAltarMenu;

public final class ModMenus {
	/** Opened from a held pouch; the opening data says which hand holds it. */
	public static final ExtendedMenuType<CrystalPouchMenu, InteractionHand> CRYSTAL_POUCH = Registry.register(BuiltInRegistries.MENU,
			PrismaticRealms.id("crystal_pouch"), new ExtendedMenuType<>(CrystalPouchMenu::new, InteractionHand.STREAM_CODEC.cast()));

	/** Opened by right-clicking the Spectral Altar. */
	public static final MenuType<SpectralAltarMenu> SPECTRAL_ALTAR = Registry.register(BuiltInRegistries.MENU,
			PrismaticRealms.id("spectral_altar"), new MenuType<>(SpectralAltarMenu::new, FeatureFlags.VANILLA_SET));

	private ModMenus() {
	}

	public static void initialize() {
	}
}
