package at.donmatzos.prismaticrealms.armor;

import at.donmatzos.prismaticrealms.family.CrystalFamily;

/**
 * Which family's armor set a wearer has. A set is helmet, leggings and boots of one family plus either that family's
 * chestplate or any elytra in the chest slot. Plain Java so it can be unit-tested; null means "no set".
 */
public final class ArmorSetRules {
	private ArmorSetRules() {
	}

	public static CrystalFamily activeSet(CrystalFamily helmet, CrystalFamily chest, boolean chestIsElytra, CrystalFamily legs, CrystalFamily boots) {
		if (helmet == null || helmet != legs || helmet != boots) {
			return null;
		}
		return chestIsElytra || chest == helmet ? helmet : null;
	}
}
