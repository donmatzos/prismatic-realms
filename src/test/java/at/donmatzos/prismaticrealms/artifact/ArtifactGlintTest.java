package at.donmatzos.prismaticrealms.artifact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class ArtifactGlintTest {
	@Test
	void everyArtifactHasItsOwnGlintTextureInTheJar() {
		Set<String> seen = new HashSet<>();
		for (String sprite : ArtifactGlint.spritePaths()) {
			String name = ArtifactGlint.forSprite(sprite);
			assertNotNull(name);
			assertNotNull(getClass().getResource("/assets/prismaticrealms/textures/misc/glint/" + name + ".png"), name);
			seen.add(name);
		}
		assertEquals(7, seen.size());
	}

	@Test
	void everyArtifactSpriteHasAnItemTexture() {
		for (String sprite : ArtifactGlint.spritePaths()) {
			assertNotNull(getClass().getResource("/assets/prismaticrealms/textures/" + sprite + ".png"), sprite);
		}
	}

	@Test
	void otherSpritesKeepTheVanillaGlint() {
		assertNull(ArtifactGlint.forSprite("item/the_shallow_waters"));
		assertNull(ArtifactGlint.forSprite("item/crimson_sanguis_crystal"));
	}
}
