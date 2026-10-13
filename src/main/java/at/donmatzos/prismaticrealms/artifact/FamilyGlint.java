package at.donmatzos.prismaticrealms.artifact;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import at.donmatzos.prismaticrealms.family.CrystalFamily;

/**
 * Which tinted enchantment glint each family item uses (plain Java, unit-tested): the artifacts and the unique scythes,
 * by item sprite path (e.g. `item/verdant_aura`; the scythes' in-hand model has its own `_in_hand` sprite), mapped to the
 * glint texture `textures/misc/glint/<family id>.png` in the colour of their crystal family.
 */
public final class FamilyGlint {
	/** The family's artifact and unique scythe, as item ids (without namespace). */
	private record Members(String artifact, String scythe) {
	}

	private static final Map<CrystalFamily, Members> MEMBERS = new EnumMap<>(CrystalFamily.class);
	private static final Map<String, CrystalFamily> FAMILY_BY_SPRITE = new HashMap<>();

	static {
		MEMBERS.put(CrystalFamily.ICE_STAR, new Members("frostwalker_star", "scythe_of_the_frozen_expanse"));
		MEMBERS.put(CrystalFamily.CRUEL_SUN, new Members("sunblessed_talisman", "melting_sunray"));
		MEMBERS.put(CrystalFamily.STORM_FANG, new Members("stormfathers_heirloom", "blitz_slasher"));
		MEMBERS.put(CrystalFamily.CERULEAN_ABYSS, new Members("abyssal_heart", "the_shallow_waters"));
		MEMBERS.put(CrystalFamily.CRIMSON_SANGUIS, new Members("bloodstone_chalice", "crimson_diabell"));
		MEMBERS.put(CrystalFamily.OVERGROWN_PROSPERITY, new Members("verdant_aura", "overgrown_reaper"));
		MEMBERS.put(CrystalFamily.VENOM_CROWN, new Members("vipers_diadem", "scorpion_tail"));
		MEMBERS.forEach((family, members) -> {
			FAMILY_BY_SPRITE.put("item/" + members.artifact(), family);
			FAMILY_BY_SPRITE.put("item/" + members.scythe(), family);
			FAMILY_BY_SPRITE.put("item/" + members.scythe() + "_in_hand", family);
		});
	}

	private FamilyGlint() {
	}

	/** The family of an item sprite path in the mod's namespace, or null when it keeps the vanilla glint. */
	public static CrystalFamily familyOf(String spritePath) {
		return FAMILY_BY_SPRITE.get(spritePath);
	}

	/** The glint texture name (`textures/misc/glint/<name>.png`) for a sprite path, or null for the vanilla glint. */
	public static String forSprite(String spritePath) {
		CrystalFamily family = familyOf(spritePath);
		return family == null ? null : family.id();
	}

	public static Iterable<String> spritePaths() {
		return FAMILY_BY_SPRITE.keySet();
	}

	public static boolean hasMembers(CrystalFamily family) {
		return MEMBERS.containsKey(family);
	}
}
