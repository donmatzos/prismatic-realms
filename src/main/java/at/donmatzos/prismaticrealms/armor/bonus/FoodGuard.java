package at.donmatzos.prismaticrealms.armor.bonus;

/**
 * Keeps hunger from dropping: FoodData has no exhaustion setter, so Photosynthesis remembers the highest food level
 * and saturation seen and hands them back whenever they fall. Eating raises them as usual.
 */
public final class FoodGuard {
	public record Level(int food, float saturation) {
	}

	private Level last;

	public Level update(int food, float saturation) {
		this.last = this.last == null ? new Level(food, saturation) : new Level(Math.max(this.last.food(), food), Math.max(this.last.saturation(), saturation));
		return this.last;
	}

	public void reset() {
		this.last = null;
	}
}
