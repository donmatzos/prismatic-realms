package at.donmatzos.prismaticrealms.armor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import at.donmatzos.prismaticrealms.family.CrystalFamily;

class ArmorSetRulesTest {
	private static final CrystalFamily ICE = CrystalFamily.ICE_STAR;
	private static final CrystalFamily SUN = CrystalFamily.CRUEL_SUN;

	@Test
	void fullSetOfOneFamilyIsActive() {
		assertEquals(ICE, ArmorSetRules.activeSet(ICE, ICE, false, ICE, ICE));
	}

	@Test
	void elytraInTheChestSlotCompletesTheSet() {
		assertEquals(ICE, ArmorSetRules.activeSet(ICE, null, true, ICE, ICE));
	}

	@Test
	void elytraWithoutAMatchingHelmetLeggingsAndBootsIsNothing() {
		assertNull(ArmorSetRules.activeSet(null, null, true, ICE, ICE));
		assertNull(ArmorSetRules.activeSet(ICE, null, true, SUN, ICE));
		assertNull(ArmorSetRules.activeSet(null, null, true, null, null));
	}

	@Test
	void aChestplateOfAnotherFamilyBreaksTheSet() {
		assertNull(ArmorSetRules.activeSet(ICE, SUN, false, ICE, ICE));
	}

	@Test
	void aMissingPieceBreaksTheSet() {
		assertNull(ArmorSetRules.activeSet(ICE, ICE, false, ICE, null));
		assertNull(ArmorSetRules.activeSet(ICE, null, false, ICE, ICE));
		assertNull(ArmorSetRules.activeSet(null, ICE, false, ICE, ICE));
	}

	@Test
	void mixedFamiliesNeverCombine() {
		assertNull(ArmorSetRules.activeSet(ICE, ICE, false, SUN, SUN));
	}
}
