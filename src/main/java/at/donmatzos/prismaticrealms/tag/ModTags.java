package at.donmatzos.prismaticrealms.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import at.donmatzos.prismaticrealms.PrismaticRealms;

public final class ModTags {
	// Pickaxe tiers for crystal ore drops: low tier drops 1-3 shards, high tier 3-5 plus a chance at a Spectral Binder.
	public static final TagKey<Item> LOW_TIER_PICKAXES = TagKey.create(Registries.ITEM, PrismaticRealms.id("low_tier_pickaxes"));
	public static final TagKey<Item> HIGH_TIER_PICKAXES = TagKey.create(Registries.ITEM, PrismaticRealms.id("high_tier_pickaxes"));

	public static final TagKey<Item> SCYTHES = TagKey.create(Registries.ITEM, PrismaticRealms.id("scythes"));

	public static final TagKey<Block> CRYSTAL_ORES = TagKey.create(Registries.BLOCK, PrismaticRealms.id("crystal_ores"));

	private ModTags() {
	}
}
