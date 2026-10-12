package at.donmatzos.prismaticrealms.dowsing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Which ore the Dowsing Rod searches for, from the item id in the player's off hand. Plain Java so it can be unit-tested.
 * Crystal families: the family's shard, crystal or either ore → that family's two ores. Vanilla materials: the ore block
 * or its mined form → the matching {@code c:ores/<material>} convention block tag, so modded ores count too.
 */
public final class DowsingTargets {
	public enum Kind { FAMILY, TAG }

	/** FAMILY: id = family name; TAG: id = block tag id. displayBlockId names the target in messages. */
	public record Target(Kind kind, String id, String displayBlockId) {
	}

	private static final String MOD = "prismaticrealms:";
	private static final Map<String, Target> VANILLA = new HashMap<>();

	static {
		vanilla("coal", "coal_ore", "coal", "coal_ore", "deepslate_coal_ore");
		vanilla("iron", "iron_ore", "raw_iron", "iron_ore", "deepslate_iron_ore");
		vanilla("copper", "copper_ore", "raw_copper", "copper_ore", "deepslate_copper_ore");
		vanilla("gold", "gold_ore", "raw_gold", "gold_ore", "deepslate_gold_ore", "nether_gold_ore");
		vanilla("redstone", "redstone_ore", "redstone", "redstone_ore", "deepslate_redstone_ore");
		vanilla("lapis", "lapis_ore", "lapis_lazuli", "lapis_ore", "deepslate_lapis_ore");
		vanilla("diamond", "diamond_ore", "diamond", "diamond_ore", "deepslate_diamond_ore");
		vanilla("emerald", "emerald_ore", "emerald", "emerald_ore", "deepslate_emerald_ore");
		vanilla("quartz", "nether_quartz_ore", "quartz", "nether_quartz_ore");
		vanilla("netherite_scrap", "ancient_debris", "ancient_debris", "netherite_scrap");
	}

	private DowsingTargets() {
	}

	private static void vanilla(String material, String displayBlock, String... items) {
		Target target = new Target(Kind.TAG, "c:ores/" + material, "minecraft:" + displayBlock);
		for (String item : items) {
			VANILLA.put("minecraft:" + item, target);
		}
	}

	public static Optional<Target> forItem(String itemId, List<String> families) {
		if (itemId.startsWith(MOD)) {
			String path = itemId.substring(MOD.length());
			for (String family : families) {
				if (path.equals(family + "_shard") || path.equals(family + "_crystal")
						|| path.equals(family + "_ore") || path.equals("deepslate_" + family + "_ore")) {
					return Optional.of(new Target(Kind.FAMILY, family, MOD + family + "_ore"));
				}
			}
			return Optional.empty();
		}
		return Optional.ofNullable(VANILLA.get(itemId));
	}
}
