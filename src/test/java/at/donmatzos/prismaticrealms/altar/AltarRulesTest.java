package at.donmatzos.prismaticrealms.altar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import at.donmatzos.prismaticrealms.altar.AltarRules.Target;

class AltarRulesTest {
	@Test
	void fuelNeedsThreeShards() {
		assertFalse(AltarRules.hasFuel(0));
		assertFalse(AltarRules.hasFuel(2));
		assertTrue(AltarRules.hasFuel(3));
		assertTrue(AltarRules.hasFuel(64));
	}

	@Test
	void shardsGoToFuelFirstThenTheSides() {
		assertEquals(List.of(Target.FUEL, Target.SHARDS), AltarRules.quickMoveTargets(true, false, false));
	}

	@Test
	void crystalsGoToTheTop() {
		assertEquals(List.of(Target.CRYSTAL), AltarRules.quickMoveTargets(false, true, false));
	}

	@Test
	void theSettingGoesToTheCentre() {
		assertEquals(List.of(Target.SETTING), AltarRules.quickMoveTargets(false, false, true));
	}

	@Test
	void everythingElseIsACatalyst() {
		assertEquals(List.of(Target.CATALYST), AltarRules.quickMoveTargets(false, false, false));
	}
}
