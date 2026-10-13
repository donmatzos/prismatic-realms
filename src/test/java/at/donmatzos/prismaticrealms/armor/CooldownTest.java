package at.donmatzos.prismaticrealms.armor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import at.donmatzos.prismaticrealms.armor.bonus.Cooldown;

class CooldownTest {
	@Test
	void readyOnceTheCooldownHasPassed() {
		assertFalse(Cooldown.ready(100, 159, 60));
		assertTrue(Cooldown.ready(100, 160, 60));
	}

	@Test
	void neverUsedIsAlwaysReady() {
		assertTrue(Cooldown.ready(Cooldown.NEVER, 0, 60));
	}
}
