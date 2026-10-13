package at.donmatzos.prismaticrealms.artifact;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** The saved open/closed state of the artifact bar, as a properties file (plain Java, unit-tested). */
public final class ArtifactBarSetting {
	private static final String KEY = "artifactBarOpen";

	private ArtifactBarSetting() {
	}

	/** The saved state; open when the file is missing or unreadable. */
	public static boolean read(Path file) {
		if (!Files.isRegularFile(file)) {
			return true;
		}
		Properties properties = new Properties();
		try (InputStream in = Files.newInputStream(file)) {
			properties.load(in);
		} catch (IOException | IllegalArgumentException e) {
			return true;
		}
		return !"false".equals(properties.getProperty(KEY));
	}

	/** Saves the state; returns false when the file couldn't be written. */
	public static boolean write(Path file, boolean open) {
		Properties properties = new Properties();
		properties.setProperty(KEY, Boolean.toString(open));
		try (OutputStream out = Files.newOutputStream(file)) {
			properties.store(out, "Prismatic Realms (client)");
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
