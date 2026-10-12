package at.donmatzos.prismaticrealms.dowsing;

/**
 * Wording for the Dowsing Rod's result, from the block offset between the player and the ore. Plain Java so it can be
 * unit-tested; the keys are suffixes of lang keys ({@code dowsing.prismaticrealms.direction.<key>},
 * {@code dowsing.prismaticrealms.height.<key>}). Minecraft's axes: north is -Z, east is +X.
 */
public final class DowsingText {
	private static final String[] POINTS = {"north", "north_east", "east", "south_east", "south", "south_west", "west", "north_west"};

	private DowsingText() {
	}

	/** The nearest of the 8 compass points from the player towards the ore, or "straight" when it is directly above or below. */
	public static String compassPoint(int dx, int dz) {
		if (dx == 0 && dz == 0) {
			return "straight";
		}
		double degrees = Math.toDegrees(Math.atan2(dx, -dz));   // 0 = north, 90 = east
		int sector = (int) Math.round(degrees / 45.0);
		return POINTS[Math.floorMod(sector, 8)];
	}

	/** "above", "below" or "level" for the ore's height relative to the player. */
	public static String heightKey(int dy) {
		return dy > 0 ? "above" : dy < 0 ? "below" : "level";
	}
}
