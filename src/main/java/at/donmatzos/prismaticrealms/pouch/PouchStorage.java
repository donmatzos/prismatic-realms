package at.donmatzos.prismaticrealms.pouch;

import java.util.Arrays;

/**
 * The Crystal Pouch's storage as plain Java (unit-tested): 7 families x {shard, crystal} counts. Slot index = family
 * (0-6) for shards, 7 + family for crystals. Capacity per slot: 512 shards / 128 crystals, doubled per tier (0, 1, 2).
 */
public final class PouchStorage {
	public static final int FAMILIES = 7;
	public static final int SLOTS = 2 * FAMILIES;
	public static final int STACK = 64;
	private static final int BASE_SHARDS = 512;
	private static final int BASE_CRYSTALS = 128;

	private final int[] counts;
	private final int tier;

	public PouchStorage(int[] counts, int tier) {
		this.tier = tier;
		this.counts = new int[SLOTS];
		for (int i = 0; i < SLOTS && i < counts.length; i++) {
			this.counts[i] = Math.max(0, Math.min(counts[i], capacity(i, tier)));
		}
	}

	public static int capacity(int slot, int tier) {
		return (slot < FAMILIES ? BASE_SHARDS : BASE_CRYSTALS) << tier;
	}

	public int count(int slot) {
		return this.counts[slot];
	}

	public int[] counts() {
		return Arrays.copyOf(this.counts, SLOTS);
	}

	public int tier() {
		return this.tier;
	}

	/** Inserts up to amount into the slot; returns how many went in. */
	public int insert(int slot, int amount) {
		int inserted = Math.max(0, Math.min(amount, capacity(slot, this.tier) - this.counts[slot]));
		this.counts[slot] += inserted;
		return inserted;
	}

	/** Removes one stack (64) or the rest if fewer are stored; returns how many were removed. */
	public int takeStack(int slot) {
		return this.take(slot, STACK);
	}

	/** Removes up to max; returns how many were removed. */
	public int take(int slot, int max) {
		int taken = Math.max(0, Math.min(max, this.counts[slot]));
		this.counts[slot] -= taken;
		return taken;
	}

	public int total(boolean crystals) {
		int sum = 0;
		for (int i = crystals ? FAMILIES : 0; i < (crystals ? SLOTS : FAMILIES); i++) {
			sum += this.counts[i];
		}
		return sum;
	}
}
