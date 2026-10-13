package at.donmatzos.prismaticrealms.armor.bonus;

public final class Cooldown {
	/** Marks "never used". */
	public static final long NEVER = Long.MIN_VALUE / 2;

	private Cooldown() {
	}

	public static boolean ready(long lastUse, long now, int cooldownTicks) {
		return now - lastUse >= cooldownTicks;
	}
}
