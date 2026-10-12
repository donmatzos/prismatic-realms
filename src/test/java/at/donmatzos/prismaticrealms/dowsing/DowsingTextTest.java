package at.donmatzos.prismaticrealms.dowsing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DowsingTextTest {
	@Test
	void cardinalDirections() {
		assertEquals("north", DowsingText.compassPoint(0, -10));
		assertEquals("east", DowsingText.compassPoint(10, 0));
		assertEquals("south", DowsingText.compassPoint(0, 10));
		assertEquals("west", DowsingText.compassPoint(-10, 0));
	}

	@Test
	void diagonalDirections() {
		assertEquals("north_east", DowsingText.compassPoint(5, -5));
		assertEquals("south_east", DowsingText.compassPoint(7, 7));
		assertEquals("south_west", DowsingText.compassPoint(-3, 3));
		assertEquals("north_west", DowsingText.compassPoint(-8, -8));
	}

	@Test
	void roundsToTheNearestOfEightPoints() {
		assertEquals("east", DowsingText.compassPoint(10, -3));   // ~73 degrees from north
		assertEquals("north", DowsingText.compassPoint(2, -10));  // ~11 degrees
		assertEquals("north_east", DowsingText.compassPoint(8, -5));
	}

	@Test
	void directlyAboveOrBelowIsStraight() {
		assertEquals("straight", DowsingText.compassPoint(0, 0));
	}

	@Test
	void heightKeys() {
		assertEquals("above", DowsingText.heightKey(5));
		assertEquals("below", DowsingText.heightKey(-9));
		assertEquals("level", DowsingText.heightKey(0));
	}
}
