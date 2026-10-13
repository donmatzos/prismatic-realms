package at.donmatzos.prismaticrealms.artifact;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ArtifactBarSettingTest {
	@TempDir
	Path dir;

	@Test
	void missingFileMeansOpen() {
		assertTrue(ArtifactBarSetting.read(this.dir.resolve("none.properties")));
	}

	@Test
	void savedStateComesBack() {
		Path file = this.dir.resolve("client.properties");
		assertTrue(ArtifactBarSetting.write(file, false));
		assertFalse(ArtifactBarSetting.read(file));
		assertTrue(ArtifactBarSetting.write(file, true));
		assertTrue(ArtifactBarSetting.read(file));
	}

	@Test
	void garbageMeansOpen() throws Exception {
		Path file = this.dir.resolve("broken.properties");
		Files.writeString(file, "artifactBarOpen=maybe\n\\u12");
		assertTrue(ArtifactBarSetting.read(file));
	}

	@Test
	void unwritablePathReportsFailure() {
		assertFalse(ArtifactBarSetting.write(this.dir.resolve("missing-dir/client.properties"), false));
	}
}
