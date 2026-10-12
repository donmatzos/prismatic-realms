package at.donmatzos.prismaticrealms.artifact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ArtifactBarLayoutTest {
	private static final ArtifactBarLayout.Layout S = ArtifactBarLayout.SURVIVAL;

	@Test
	void slotsAlignWithTheTopThreeArmourRows() {
		assertEquals(8, ArtifactBarLayout.slotY(S, 0));
		assertEquals(26, ArtifactBarLayout.slotY(S, 1));
		assertEquals(44, ArtifactBarLayout.slotY(S, 2));
	}

	@Test
	void panelIsAttachedToTheRightEdge() {
		// the panel overlaps the window's right border (imageWidth 176) so it looks attached
		assertTrue(S.panelX() < 176 && S.panelX() + ArtifactBarLayout.PANEL_WIDTH > 176);
		assertTrue(S.slotX() >= 176);
	}

	@Test
	void containsUsesWindowCoordinates() {
		int left = 100, top = 50;
		assertTrue(ArtifactBarLayout.contains(S, left + 180, top + 30, left, top));
		assertFalse(ArtifactBarLayout.contains(S, left + 180, top + 70, left, top));   // below the panel
		assertFalse(ArtifactBarLayout.contains(S, left + 150, top + 30, left, top));   // inside the inventory, left of the panel
		assertFalse(ArtifactBarLayout.contains(S, left + 210, top + 30, left, top));   // right of the panel
	}

	@Test
	void overSlotOnlyOnTheSlotAreas() {
		int left = 0, top = 0;
		assertTrue(ArtifactBarLayout.overSlot(S, 180, 30, left, top));    // second slot
		assertFalse(ArtifactBarLayout.overSlot(S, 180, 43, left, top));   // gap between slots 2 and 3
		assertFalse(ArtifactBarLayout.overSlot(S, 175, 10, left, top));   // frame, left of the slots
	}

	@Test
	void creativeLayoutIsRightOfTheCreativeWindow() {
		assertTrue(ArtifactBarLayout.CREATIVE.slotX() >= 195);
		assertTrue(ArtifactBarLayout.CREATIVE.panelX() < 195);
	}
}
