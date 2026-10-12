package at.donmatzos.prismaticrealms.altar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SpectralAltarAnimationTest {
	private static final float EPS = 1.0E-4F;

	@Test
	void nearbyEasesTowardsTheTarget() {
		assertEquals(0.1F, SpectralAltarAnimation.nextNearby(0.0F, true), EPS);
		assertEquals(0.4F, SpectralAltarAnimation.nextNearby(0.5F, false), EPS);
	}

	@Test
	void nearbyClampsAtBothEnds() {
		float nearby = 0.0F;
		for (int i = 0; i < 100; i++) {
			nearby = SpectralAltarAnimation.nextNearby(nearby, true);
		}
		assertEquals(1.0F, nearby, EPS);
		for (int i = 0; i < 100; i++) {
			nearby = SpectralAltarAnimation.nextNearby(nearby, false);
		}
		assertEquals(0.0F, nearby, EPS);
	}

	@Test
	void orbitTakesTenSecondsAloneAndTwiceAsFastWithAPlayerNear() {
		assertEquals(SpectralAltarAnimation.TWO_PI / 200.0F, SpectralAltarAnimation.orbitStep(0.0F), EPS);
		assertEquals(2.0F * SpectralAltarAnimation.orbitStep(0.0F), SpectralAltarAnimation.orbitStep(1.0F), EPS);
	}

	@Test
	void advanceOrbitWrapsBothValuesTogether() {
		float[] next = SpectralAltarAnimation.advanceOrbit(SpectralAltarAnimation.TWO_PI - 0.01F, 0.0F, 1.0F);
		float newOrbit = next[0], newOOrbit = next[1];
		assertTrue(newOrbit >= 0.0F && newOrbit < SpectralAltarAnimation.TWO_PI);
		// the step between the interpolation ends stays one orbit step, so the ring never jumps
		assertEquals(SpectralAltarAnimation.orbitStep(1.0F), newOrbit - newOOrbit, EPS);
	}

	@Test
	void advanceOrbitWithoutWrapKeepsThePreviousAngle() {
		float[] next = SpectralAltarAnimation.advanceOrbit(1.0F, 0.9F, 0.0F);
		assertEquals(1.0F + SpectralAltarAnimation.orbitStep(0.0F), next[0], EPS);
		assertEquals(1.0F, next[1], EPS);
	}

	@Test
	void shardAnglesAreEvenlySpacedForAnyCount() {
		for (int count : new int[] {7, 9}) {
			float step = SpectralAltarAnimation.TWO_PI / count;
			for (int i = 0; i < count; i++) {
				assertEquals(0.5F + i * step, SpectralAltarAnimation.shardAngle(i, count, 0.5F), EPS);
			}
		}
	}

	@Test
	void bobsStayWithinTheirAmplitude() {
		for (int t = 0; t < SpectralAltarAnimation.ANIMATION_PERIOD; t++) {
			assertTrue(Math.abs(SpectralAltarAnimation.binderBob(t)) <= SpectralAltarAnimation.BINDER_BOB + EPS);
			for (int i = 0; i < 7; i++) {
				assertTrue(Math.abs(SpectralAltarAnimation.shardBob(i, 7, t)) <= SpectralAltarAnimation.SHARD_BOB + EPS);
			}
		}
	}

	@Test
	void neighbouringShardsBobOutOfPhase() {
		float a = SpectralAltarAnimation.shardBob(0, 7, 10.0F);
		float b = SpectralAltarAnimation.shardBob(1, 7, 10.0F);
		assertTrue(Math.abs(a - b) > EPS);
	}

	@Test
	void binderSpinsAgainstTheOrbit() {
		// the orbit angle grows; the binder's spin angle shrinks
		assertTrue(SpectralAltarAnimation.binderSpin(10.0F) < SpectralAltarAnimation.binderSpin(0.0F));
	}

	@Test
	void periodicFunctionsRepeatEveryAnimationPeriod() {
		int p = SpectralAltarAnimation.ANIMATION_PERIOD;
		for (float t : new float[] {0.0F, 13.5F, 77.25F}) {
			assertEquals(SpectralAltarAnimation.binderBob(t), SpectralAltarAnimation.binderBob(t + p), EPS);
			assertEquals(SpectralAltarAnimation.shardBob(3, 7, t), SpectralAltarAnimation.shardBob(3, 7, t + p), EPS);
			float spinDiff = SpectralAltarAnimation.binderSpin(t) - SpectralAltarAnimation.binderSpin(t + p);
			// a whole number of turns
			float turns = spinDiff / SpectralAltarAnimation.TWO_PI;
			assertEquals(Math.round(turns), turns, EPS);
		}
	}

	@Test
	void wrapTimeStaysSmallAfterDaysOfUptime() {
		int ticks = 20 * 60 * 60 * 24 * 5 + 123;   // five days
		float wrapped = SpectralAltarAnimation.wrapTime(ticks, 0.5F);
		assertEquals((ticks % SpectralAltarAnimation.ANIMATION_PERIOD) + 0.5F, wrapped, EPS);
		assertTrue(wrapped < SpectralAltarAnimation.ANIMATION_PERIOD + 1);
	}
}
