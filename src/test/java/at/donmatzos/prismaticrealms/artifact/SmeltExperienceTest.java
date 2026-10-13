package at.donmatzos.prismaticrealms.artifact;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SmeltExperienceTest {
	@Test
	void wholeAmountsNeedNoRoll() {
		assertEquals(4, SmeltExperience.reward(2, 2.0F, 0.99F));   // 2 netherite scrap
	}

	@Test
	void theFractionBecomesAChanceOfOneMorePoint() {
		// 3 raw iron x 0.7 = 2.1: floor 2, plus 1 when the roll is under 0.1
		assertEquals(3, SmeltExperience.reward(3, 0.7F, 0.05F));
		assertEquals(2, SmeltExperience.reward(3, 0.7F, 0.5F));
	}

	@Test
	void smallAmountsCanGiveNothing() {
		// 1 glass x 0.1: no whole point, 10 % chance of one
		assertEquals(0, SmeltExperience.reward(1, 0.1F, 0.5F));
		assertEquals(1, SmeltExperience.reward(1, 0.1F, 0.01F));
	}

	@Test
	void noExperienceRecipesGiveNothing() {
		assertEquals(0, SmeltExperience.reward(5, 0.0F, 0.0F));
	}
}
