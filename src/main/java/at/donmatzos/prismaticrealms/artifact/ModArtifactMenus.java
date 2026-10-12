package at.donmatzos.prismaticrealms.artifact;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

import at.donmatzos.prismaticrealms.PrismaticRealms;

public final class ModArtifactMenus {
	public static final MenuType<ArtifactMenu> ARTIFACTS = Registry.register(BuiltInRegistries.MENU, PrismaticRealms.id("artifacts"),
			new MenuType<>(ArtifactMenu::new, FeatureFlags.VANILLA_SET));

	private ModArtifactMenus() {
	}

	public static void initialize() {
	}
}
