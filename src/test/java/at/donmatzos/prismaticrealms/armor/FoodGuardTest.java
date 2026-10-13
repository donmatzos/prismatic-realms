package at.donmatzos.prismaticrealms.armor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import at.donmatzos.prismaticrealms.armor.bonus.FoodGuard;

class FoodGuardTest {
	@Test
	void theFirstObservationIsKept() {
		FoodGuard guard = new FoodGuard();
		assertEquals(new FoodGuard.Level(18, 4.0F), guard.update(18, 4.0F));
	}

	@Test
	void drainedFoodAndSaturationAreRestored() {
		FoodGuard guard = new FoodGuard();
		guard.update(18, 4.0F);
		assertEquals(new FoodGuard.Level(18, 4.0F), guard.update(17, 3.0F));
	}

	@Test
	void eatingRaisesTheGuardedLevel() {
		FoodGuard guard = new FoodGuard();
		guard.update(10, 1.0F);
		assertEquals(new FoodGuard.Level(20, 6.0F), guard.update(20, 6.0F));
		assertEquals(new FoodGuard.Level(20, 6.0F), guard.update(19, 5.0F));
	}

	@Test
	void resettingForgetsTheLevel() {
		FoodGuard guard = new FoodGuard();
		guard.update(20, 5.0F);
		guard.reset();
		assertEquals(new FoodGuard.Level(12, 0.0F), guard.update(12, 0.0F));
	}
}
