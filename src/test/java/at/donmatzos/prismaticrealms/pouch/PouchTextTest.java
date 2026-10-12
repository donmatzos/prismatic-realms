package at.donmatzos.prismaticrealms.pouch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PouchTextTest {
	@Test
	void labels() {
		assertEquals("", PouchText.label(0));
		assertEquals("", PouchText.label(1));
		assertEquals("64", PouchText.label(64));
		assertEquals("512", PouchText.label(512));
		assertEquals("2048", PouchText.label(2048));
	}

	@Test
	void smallerFontFromThreeDigits() {
		assertEquals(1.0F, PouchText.scale(64));
		assertEquals(0.75F, PouchText.scale(512));
		assertEquals(0.5F, PouchText.scale(2048));
	}

	@Test
	void grouping() {
		assertEquals("0", PouchText.grouped(0));
		assertEquals("512", PouchText.grouped(512));
		assertEquals("1,536", PouchText.grouped(1536));
		assertEquals("2,048", PouchText.grouped(2048));
	}
}
