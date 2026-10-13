package at.donmatzos.prismaticrealms.artifact;

import java.util.HashMap;
import java.util.Map;

/**
 * Which tinted enchantment glint each family item uses (plain Java, unit-tested): the artifacts and the unique scythes,
 * by item sprite path (e.g. `item/verdant_aura`; the scythes' in-hand model has its own `_in_hand` sprite), mapped to the
 * glint texture `textures/misc/glint/<name>.png` in the colour of their crystal family.
 */
public final class FamilyGlint {
	private static final String[][] FAMILIES = {   // glint name, artifact, unique scythe
			{"ice_star", "frostwalker_star", "scythe_of_the_frozen_expanse"},
			{"cruel_sun", "sunblessed_talisman", "melting_sunray"},
			{"storm_fang", "stormfathers_heirloom", "blitz_slasher"},
			{"cerulean_abyss", "abyssal_heart", "the_shallow_waters"},
			{"crimson_sanguis", "bloodstone_chalice", "crimson_diabell"},
			{"overgrown_prosperity", "verdant_aura", "overgrown_reaper"},
			{"venom_crown", "vipers_diadem", "scorpion_tail"}};
	private static final Map<String, String> GLINT_BY_SPRITE = new HashMap<>();

	static {
		for (String[] family : FAMILIES) {
			GLINT_BY_SPRITE.put("item/" + family[1], family[0]);
			GLINT_BY_SPRITE.put("item/" + family[2], family[0]);
			GLINT_BY_SPRITE.put("item/" + family[2] + "_in_hand", family[0]);
		}
	}

	private FamilyGlint() {
	}

	/** The glint name for an item sprite path in the mod's namespace, or null when it keeps the vanilla glint. */
	public static String forSprite(String spritePath) {
		return GLINT_BY_SPRITE.get(spritePath);
	}

	public static Iterable<String> spritePaths() {
		return GLINT_BY_SPRITE.keySet();
	}
}
