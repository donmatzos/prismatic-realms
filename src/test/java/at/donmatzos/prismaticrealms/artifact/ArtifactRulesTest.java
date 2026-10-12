package at.donmatzos.prismaticrealms.artifact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class ArtifactRulesTest {
	private static final String A = "prismaticrealms:a";
	private static final String B = "prismaticrealms:b";

	@Test
	void artifactsFitIntoFreeSlots() {
		assertTrue(ArtifactRules.mayPlace(List.of("", "", ""), 0, A, true));
		assertTrue(ArtifactRules.mayPlace(List.of(A, "", ""), 1, B, true));
	}

	@Test
	void nonArtifactsNeverFit() {
		assertFalse(ArtifactRules.mayPlace(List.of("", "", ""), 0, "minecraft:stick", false));
	}

	@Test
	void duplicatesAreRefused() {
		assertFalse(ArtifactRules.mayPlace(List.of(A, "", ""), 1, A, true));
		assertFalse(ArtifactRules.mayPlace(List.of("", "", A), 0, A, true));
	}

	@Test
	void replacingTheSameSlotIsAllowed() {
		// the slot that already holds the artifact doesn't count as "another" slot (swapping it out and back)
		assertTrue(ArtifactRules.mayPlace(List.of(A, "", ""), 0, A, true));
	}

	@Test
	void firstFreeSlot() {
		assertEquals(0, ArtifactRules.firstFreeSlot(List.of("", "", ""), A, true));
		assertEquals(1, ArtifactRules.firstFreeSlot(List.of(B, "", ""), A, true));
		assertEquals(-1, ArtifactRules.firstFreeSlot(List.of(A, "", ""), A, true));   // already equipped
		assertEquals(-1, ArtifactRules.firstFreeSlot(List.of(A, B, "prismaticrealms:c"), "prismaticrealms:d", true));   // full
		assertEquals(-1, ArtifactRules.firstFreeSlot(List.of("", "", ""), "minecraft:stick", false));
	}
}
