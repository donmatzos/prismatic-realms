package at.donmatzos.prismaticrealms.altar;

import java.util.List;

/** Spectral Altar crafting rules without Minecraft types: the fuel cost and where shift-clicked items go. */
public final class AltarRules {
	/** Shards of any family burned per craft. */
	public static final int FUEL_PER_CRAFT = 3;

	/** The altar's input slot groups, as shift-click targets. */
	public enum Target {
		CRYSTAL, SHARDS, SETTING, CATALYST, FUEL
	}

	private AltarRules() {
	}

	public static boolean hasFuel(int fuelCount) {
		return fuelCount >= FUEL_PER_CRAFT;
	}

	/** Slot groups to try, in order, when an item is shift-clicked from the inventory into the altar. */
	public static List<Target> quickMoveTargets(boolean isShard, boolean isCrystal, boolean isSetting) {
		if (isShard) {
			return List.of(Target.FUEL, Target.SHARDS);
		}
		if (isCrystal) {
			return List.of(Target.CRYSTAL);
		}
		if (isSetting) {
			return List.of(Target.SETTING);
		}
		return List.of(Target.CATALYST);
	}
}
