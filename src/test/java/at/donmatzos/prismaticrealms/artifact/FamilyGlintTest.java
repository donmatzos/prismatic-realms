package at.donmatzos.prismaticrealms.artifact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class ArtifactGlintTest {
	@Test
	void everyFamilyItemHasAGlintTextureInTheJar() {
		Set<String> seen = new HashSet<>();
		for (String sprite : FamilyGlint.spritePaths()) {
			String name = FamilyGlint.forSprite(sprite);
			assertNotNull(name);
			assertNotNull(getClass().getResource("/assets/prismaticrealms/textures/misc/glint/" + name + ".png"), name);
			seen.add(name);
		}
		assertEquals(7, seen.size());   // one glint per crystal family, shared by its artifact and unique scythe
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
