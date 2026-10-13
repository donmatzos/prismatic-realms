package at.donmatzos.prismaticrealms.armor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import at.donmatzos.prismaticrealms.armor.bonus.SprintCharge;

class SprintChargeTest {
	private static void sprint(SprintCharge charge, int ticks) {
		for (int i = 0; i < ticks; i++) {
			charge.tick(true);
		}
	}

	private static void idle(SprintCharge charge, int ticks) {
		for (int i = 0; i < ticks; i++) {
			charge.tick(false);
		}
	}

	@Test
	void chargesAfterSixtyUnbrokenSprintTicks() {
		SprintCharge charge = new SprintCharge();
		sprint(charge, SprintCharge.BUILD_TICKS - 1);
		assertFalse(charge.isCharged());
		sprint(charge, 1);
		assertTrue(charge.isCharged());
	}

	@Test
	void aBrokenSprintRestartsTheBuildUp() {
		SprintCharge charge = new SprintCharge();
		sprint(charge, 40);
		idle(charge, 1);
		sprint(charge, 40);
		assertFalse(charge.isCharged());
	}

	@Test
	void theChargeSurvivesTheSwingThatEndsSprinting() {
		SprintCharge charge = new SprintCharge();
		sprint(charge, SprintCharge.BUILD_TICKS);
		idle(charge, SprintCharge.KEEP_TICKS);
		assertTrue(charge.isCharged());
	}

	@Test
	void theChargeIsLostAfterTooLongWithoutSprinting() {
		SprintCharge charge = new SprintCharge();
		sprint(charge, SprintCharge.BUILD_TICKS);
		idle(charge, SprintCharge.KEEP_TICKS + 1);
		assertFalse(charge.isCharged());
	}

	@Test
	void consumingSpendsTheChargeAndRestartsTheBuildUp() {
		SprintCharge charge = new SprintCharge();
		sprint(charge, SprintCharge.BUILD_TICKS);
		assertTrue(charge.consume());
		assertFalse(charge.consume());
		sprint(charge, SprintCharge.BUILD_TICKS - 1);
		assertFalse(charge.isCharged());
		sprint(charge, 1);
		assertTrue(charge.isCharged());
	}
}
