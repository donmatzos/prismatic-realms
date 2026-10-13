package at.donmatzos.prismaticrealms.artifact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import at.donmatzos.prismaticrealms.family.CrystalFamily;

class FamilyGlintTest {
	@Test
	void everyFamilyHasMembersAndAGlintTextureInTheJar() {
		for (CrystalFamily family : CrystalFamily.values()) {
			assertTrue(FamilyGlint.hasMembers(family), family.id());
			assertNotNull(getClass().getResource("/assets/prismaticrealms/textures/misc/glint/" + family.id() + ".png"), family.id());
		}
	}

	@Test
	void artifactAndScytheShareTheirFamilysGlint() {
		assertEquals("overgrown_prosperity", FamilyGlint.forSprite("item/verdant_aura"));
		assertEquals("overgrown_prosperity", FamilyGlint.forSprite("item/overgrown_reaper"));
		assertEquals("overgrown_prosperity", FamilyGlint.forSprite("item/overgrown_reaper_in_hand"));
		assertEquals(CrystalFamily.VENOM_CROWN, FamilyGlint.familyOf("item/scorpion_tail_in_hand"));
	}

	@Test
	void everyMappedSpriteHasAnItemTexture() {
		for (String sprite : FamilyGlint.spritePaths()) {
			assertNotNull(getClass().getResource("/assets/prismaticrealms/textures/" + sprite + ".png"), sprite);
		}
	}

	@Test
	void otherSpritesKeepTheVanillaGlint() {
		assertNull(FamilyGlint.forSprite("item/netherite_scythe"));
		assertNull(FamilyGlint.forSprite("item/netherite_scythe_in_hand"));
		assertNull(FamilyGlint.forSprite("item/crimson_sanguis_crystal"));
	}
}
