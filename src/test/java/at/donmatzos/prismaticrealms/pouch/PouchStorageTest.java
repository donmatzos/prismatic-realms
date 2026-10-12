package at.donmatzos.prismaticrealms.pouch;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PouchStorageTest {
	private static PouchStorage empty(int tier) {
		return new PouchStorage(new int[PouchStorage.SLOTS], tier);
	}

	@Test
	void capacitiesDoublePerTier() {
		assertEquals(512, PouchStorage.capacity(0, 0));
		assertEquals(128, PouchStorage.capacity(7, 0));
		assertEquals(1024, PouchStorage.capacity(6, 1));
		assertEquals(256, PouchStorage.capacity(13, 1));
		assertEquals(2048, PouchStorage.capacity(3, 2));
		assertEquals(512, PouchStorage.capacity(10, 2));
	}

	@Test
	void insertStopsAtCapacity() {
		PouchStorage storage = empty(0);
		assertEquals(500, storage.insert(2, 500));
		assertEquals(12, storage.insert(2, 64));
		assertEquals(512, storage.count(2));
		assertEquals(0, storage.insert(2, 1));
		assertEquals(0, storage.count(3));
	}

	@Test
	void takeStackTakesAtMostOneStack() {
		PouchStorage storage = empty(0);
		storage.insert(9, 100);
		assertEquals(64, storage.takeStack(9));
		assertEquals(36, storage.count(9));
		assertEquals(36, storage.takeStack(9));
		assertEquals(0, storage.count(9));
		assertEquals(0, storage.takeStack(9));
	}

	@Test
	void takeTakesAtMostTheRequestedAmount() {
		PouchStorage storage = empty(2);
		storage.insert(0, 1000);
		assertEquals(700, storage.take(0, 700));
		assertEquals(300, storage.take(0, 700));
		assertEquals(0, storage.count(0));
	}

	@Test
	void totalsAndCopies() {
		int[] counts = new int[PouchStorage.SLOTS];
		counts[0] = 5;
		counts[6] = 7;
		counts[7] = 2;
		PouchStorage storage = new PouchStorage(counts, 0);
		counts[0] = 99;   // the constructor copies
		assertEquals(12, storage.total(false));
		assertEquals(2, storage.total(true));
		int[] out = storage.counts();
		out[7] = 50;      // the getter copies
		assertEquals(2, storage.count(7));
		assertArrayEquals(new int[] {5, 0, 0, 0, 0, 0, 7, 2, 0, 0, 0, 0, 0, 0}, storage.counts());
	}

	@Test
	void countsAboveCapacityAreClampedOnLoad() {
		int[] counts = new int[PouchStorage.SLOTS];
		counts[1] = 5000;
		assertEquals(512, new PouchStorage(counts, 0).count(1));
	}
}
