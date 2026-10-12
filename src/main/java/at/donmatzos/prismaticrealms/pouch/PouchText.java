package at.donmatzos.prismaticrealms.pouch;

/** How pouch counts are written (plain Java, unit-tested): slot labels, their font scale, grouped numbers. */
public final class PouchText {
	private PouchText() {
	}

	/** The number drawn on a slot: nothing for 0 and 1 (like vanilla), otherwise the count. */
	public static String label(int count) {
		return count <= 1 ? "" : Integer.toString(count);
	}

	/** Font scale so the label fits a 16px slot: full size up to two digits, 0.75 for three, 0.5 for four or more. */
	public static float scale(int count) {
		int digits = Integer.toString(Math.max(0, count)).length();
		return digits <= 2 ? 1.0F : digits == 3 ? 0.75F : 0.5F;
	}

	/** "1,536" — thousands separated by commas. */
	public static String grouped(int n) {
		return String.format(java.util.Locale.ROOT, "%,d", n);
	}
}
