package at.donmatzos.prismaticrealms.dowsing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import at.donmatzos.prismaticrealms.dowsing.DowsingTargets.Kind;
import at.donmatzos.prismaticrealms.dowsing.DowsingTargets.Target;

class DowsingTargetsTest {
	private static final List<String> FAMILIES = List.of("crimson_sanguis", "ice_star", "venom_crown");

	private static Target target(String itemId) {
		Optional<Target> target = DowsingTargets.forItem(itemId, FAMILIES);
		assertTrue(target.isPresent(), itemId + " should be a valid target");
		return target.get();
	}

	@Test
	void familyItemsTargetTheirFamily() {
		Target expected = new Target(Kind.FAMILY, "ice_star", "prismaticrealms:ice_star_ore");
		assertEquals(expected, target("prismaticrealms:ice_star_shard"));
		assertEquals(expected, target("prismaticrealms:ice_star_crystal"));
		assertEquals(expected, target("prismaticrealms:ice_star_ore"));
		assertEquals(expected, target("prismaticrealms:deepslate_ice_star_ore"));
	}

	@Test
	void vanillaMaterialsTargetTheirOreTag() {
		assertEquals(new Target(Kind.TAG, "c:ores/diamond", "minecraft:diamond_ore"), target("minecraft:diamond"));
		assertEquals(new Target(Kind.TAG, "c:ores/iron", "minecraft:iron_ore"), target("minecraft:raw_iron"));
		assertEquals(new Target(Kind.TAG, "c:ores/iron", "minecraft:iron_ore"), target("minecraft:deepslate_iron_ore"));
		assertEquals(new Target(Kind.TAG, "c:ores/gold", "minecraft:gold_ore"), target("minecraft:nether_gold_ore"));
		assertEquals(new Target(Kind.TAG, "c:ores/redstone", "minecraft:redstone_ore"), target("minecraft:redstone"));
		assertEquals(new Target(Kind.TAG, "c:ores/lapis", "minecraft:lapis_ore"), target("minecraft:lapis_lazuli"));
		assertEquals(new Target(Kind.TAG, "c:ores/quartz", "minecraft:nether_quartz_ore"), target("minecraft:quartz"));
		assertEquals(new Target(Kind.TAG, "c:ores/netherite_scrap", "minecraft:ancient_debris"), target("minecraft:netherite_scrap"));
		assertEquals(new Target(Kind.TAG, "c:ores/coal", "minecraft:coal_ore"), target("minecraft:coal"));
		assertEquals(new Target(Kind.TAG, "c:ores/copper", "minecraft:copper_ore"), target("minecraft:raw_copper"));
		assertEquals(new Target(Kind.TAG, "c:ores/emerald", "minecraft:emerald_ore"), target("minecraft:emerald"));
	}

	@Test
	void otherItemsAreInvalid() {
		assertTrue(DowsingTargets.forItem("minecraft:stick", FAMILIES).isEmpty());
		assertTrue(DowsingTargets.forItem("minecraft:air", FAMILIES).isEmpty());
		assertTrue(DowsingTargets.forItem("minecraft:iron_ingot", FAMILIES).isEmpty());
		assertTrue(DowsingTargets.forItem("prismaticrealms:spectral_binder", FAMILIES).isEmpty());
		assertTrue(DowsingTargets.forItem("prismaticrealms:storm_fang_shard", FAMILIES).isEmpty());   // not in this test's family list
		assertTrue(DowsingTargets.forItem("othermod:ice_star_shard", FAMILIES).isEmpty());
	}
}
