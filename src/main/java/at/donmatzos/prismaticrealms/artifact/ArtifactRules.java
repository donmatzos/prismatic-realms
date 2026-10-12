package at.donmatzos.prismaticrealms.artifact;

import java.util.List;

/**
 * The artifact slot rules as plain Java (unit-tested): only artifacts fit, and each artifact (item id) at most once
 * across the three slots. `equipped` lists the item id per slot, "" for an empty slot.
 */
public final class ArtifactRules {
	public static final int SLOTS = 3;

	private ArtifactRules() {
	}

	/** May `candidate` go into `slot`? Only if it's an artifact and no OTHER slot holds the same artifact. */
	public static boolean mayPlace(List<String> equipped, int slot, String candidate, boolean isArtifact) {
		if (!isArtifact) {
			return false;
		}
		for (int i = 0; i < equipped.size(); i++) {
			if (i != slot && equipped.get(i).equals(candidate)) {
				return false;
			}
		}
		return true;
	}

	/** The first empty slot that accepts `candidate`, or -1. */
	public static int firstFreeSlot(List<String> equipped, String candidate, boolean isArtifact) {
		for (int i = 0; i < equipped.size(); i++) {
			if (equipped.get(i).isEmpty() && mayPlace(equipped, i, candidate, isArtifact)) {
				return i;
			}
		}
		return -1;
	}
}
