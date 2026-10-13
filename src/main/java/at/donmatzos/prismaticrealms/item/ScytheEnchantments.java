package at.donmatzos.prismaticrealms.item;

import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.util.TriState;

import net.minecraft.world.item.enchantment.Enchantments;

import at.donmatzos.prismaticrealms.tag.ModTags;

/**
 * Scythes can't take Fire Aspect (it would blur the unique scythes' charged-hit effects); every other enchantment follows
 * vanilla. Covers the enchanting table, anvils, enchanted loot and /enchant. Scythes that already have it keep it.
 */
public final class ScytheEnchantments {
	private ScytheEnchantments() {
	}

	public static void initialize() {
		EnchantmentEvents.ALLOW_ENCHANTING.register((enchantment, target, context) ->
				target.is(ModTags.SCYTHES) && enchantment.is(Enchantments.FIRE_ASPECT) ? TriState.FALSE : TriState.DEFAULT);
	}
}
