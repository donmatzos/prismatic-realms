package at.donmatzos.prismaticrealms.artifact;

/** Furnace experience for auto-smelted drops, rounded like vanilla's furnace result slot. No Minecraft types. */
public final class SmeltExperience {
	private SmeltExperience() {
	}

	/**
	 * Whole points of count x perItem, plus one more point when roll (uniform in [0, 1)) falls under the fractional
	 * part, as FurnaceResultSlot#createExperience does.
	 */
	public static int reward(int count, float perItem, float roll) {
		float total = count * perItem;
		int points = (int) Math.floor(total);
		float fraction = total - points;
		return fraction != 0.0F && roll < fraction ? points + 1 : points;
	}
}
