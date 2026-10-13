package at.donmatzos.prismaticrealms.family;

import java.util.Locale;

/**
 * The colour crystal families, in rainbow order. `id()` is the family's snake_case name used in item and texture ids
 * (`<id>_shard`, `<id>_crystal`, glint textures). Light and Dark join when those endgame families are implemented.
 */
public enum CrystalFamily {
	CRIMSON_SANGUIS,
	CRUEL_SUN,
	STORM_FANG,
	OVERGROWN_PROSPERITY,
	ICE_STAR,
	CERULEAN_ABYSS,
	VENOM_CROWN;

	public String id() {
		return this.name().toLowerCase(Locale.ROOT);
	}
}
