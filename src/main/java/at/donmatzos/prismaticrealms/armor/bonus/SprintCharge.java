package at.donmatzos.prismaticrealms.armor.bonus;

/**
 * Storm Fang's Static Charge counter. 60 unbroken sprint ticks charge it; it is kept for 40 ticks without sprinting
 * (vanilla ends sprinting with a hit, so it must survive the swing) and is spent by a hit, after which the build-up
 * starts again.
 */
public final class SprintCharge {
	public static final int BUILD_TICKS = 60;
	public static final int KEEP_TICKS = 40;

	private int sprinted;
	private int idle;
	private boolean charged;

	public void tick(boolean sprinting) {
		if (sprinting) {
			this.idle = 0;
			if (!this.charged && ++this.sprinted >= BUILD_TICKS) {
				this.charged = true;
			}
		} else {
			this.sprinted = 0;
			if (this.charged && ++this.idle > KEEP_TICKS) {
				this.charged = false;
				this.idle = 0;
			}
		}
	}

	public boolean isCharged() {
		return this.charged;
	}

	/** Spends the charge: true if there was one. */
	public boolean consume() {
		boolean was = this.charged;
		this.charged = false;
		this.sprinted = 0;
		this.idle = 0;
		return was;
	}
}
