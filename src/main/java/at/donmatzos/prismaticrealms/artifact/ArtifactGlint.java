package at.donmatzos.prismaticrealms.artifact;

import java.util.Map;

/**
 * Which tinted enchantment glint each artifact uses (plain Java, unit-tested): its item texture path (the sprite name, e.g.
 * `item/verdant_aura`) mapped to the glint texture `textures/misc/glint/<name>.png`, in the colour of its crystal family.
 */
public final class ArtifactGlint {
	private static final Map<String, String> GLINT_BY_SPRITE = Map.of(
			"item/frostwalker_star", "ice_star",
			"item/sunblessed_talisman", "cruel_sun",
			"item/stormfathers_heirloom", "storm_fang",
			"item/abyssal_heart", "cerulean_abyss",
			"item/bloodstone_chalice", "crimson_sanguis",
			"item/verdant_aura", "overgrown_prosperity",
			"item/vipers_diadem", "venom_crown");

	private ArtifactGlint() {
	}

	/** The glint name for an item sprite path in the mod's namespace, or null when it keeps the vanilla glint. */
	public static String forSprite(String spritePath) {
		return GLINT_BY_SPRITE.get(spritePath);
	}

	public static Iterable<String> glintNames() {
		return GLINT_BY_SPRITE.values();
	}

	public static Iterable<String> spritePaths() {
		return GLINT_BY_SPRITE.keySet();
	}
}
