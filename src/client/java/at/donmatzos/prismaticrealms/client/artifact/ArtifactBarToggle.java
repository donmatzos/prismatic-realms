package at.donmatzos.prismaticrealms.client.artifact;

import java.nio.file.Path;

import net.fabricmc.loader.api.FabricLoader;

import at.donmatzos.prismaticrealms.artifact.ArtifactBarSetting;
import at.donmatzos.prismaticrealms.artifact.ArtifactBarState;

/** Opens and closes the artifact bar and keeps the choice in `config/prismaticrealms-client.properties`. */
public final class ArtifactBarToggle {
	private ArtifactBarToggle() {
	}

	private static Path file() {
		return FabricLoader.getInstance().getConfigDir().resolve("prismaticrealms-client.properties");
	}

	public static void load() {
		ArtifactBarState.setOpen(ArtifactBarSetting.read(file()));
	}

	public static void setOpen(boolean open) {
		if (ArtifactBarState.isOpen() != open) {
			ArtifactBarState.setOpen(open);
			ArtifactBarSetting.write(file(), open);
		}
	}
}
