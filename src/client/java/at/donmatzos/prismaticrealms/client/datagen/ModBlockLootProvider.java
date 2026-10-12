package at.donmatzos.prismaticrealms.client.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;

import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import at.donmatzos.prismaticrealms.block.ModBlocks;
import at.donmatzos.prismaticrealms.item.ModItems;
import at.donmatzos.prismaticrealms.tag.ModTags;

public class ModBlockLootProvider extends FabricBlockLootSubProvider {
	// {shard count, weight}
	private static final int[][] LOW_TIER_SHARDS = {{1, 60}, {2, 30}, {3, 10}};
	private static final int[][] HIGH_TIER_SHARDS = {{3, 30}, {4, 50}, {5, 20}};
	private static final float BINDER_CHANCE = 0.05F;
	private static final float NETHERITE_BINDER_CHANCE = 0.25F;

	public ModBlockLootProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	public void generate() {
		for (ModBlocks.CrystalOre ore : ModBlocks.CRYSTAL_ORES) {
			this.add(ore.stone(), this.crystalOreDrops(ore.stone(), ore.shard()));
			this.add(ore.deepslate(), this.crystalOreDrops(ore.deepslate(), ore.shard()));
		}
	}

	/**
	 * Silk Touch drops the ore itself. Otherwise the pickaxe tier picks the weighted shard count (Fortune applies),
	 * and high-tier pickaxes roll for a Spectral Binder: 5 %, or 25 % with netherite.
	 */
	private LootTable.Builder crystalOreDrops(Block ore, Item shard) {
		LootItemCondition.Builder netherite = MatchTool.toolMatches(ItemPredicate.Builder.item().of(this.items, Items.NETHERITE_PICKAXE));
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ContextIntProviders.exactly(1))
						.when(this.hasSilkTouch())
						.add(LootItem.lootTableItem(ore)))
				.withPool(this.shardPool(ore, shard, ModTags.LOW_TIER_PICKAXES, LOW_TIER_SHARDS))
				.withPool(this.shardPool(ore, shard, ModTags.HIGH_TIER_PICKAXES, HIGH_TIER_SHARDS))
				.withPool(this.binderPool(ore, BINDER_CHANCE)
						.when(this.toolIn(ModTags.HIGH_TIER_PICKAXES))
						.when(netherite.invert()))
				.withPool(this.binderPool(ore, NETHERITE_BINDER_CHANCE)
						.when(netherite));
	}

	private LootPool.Builder shardPool(Block ore, Item shard, net.minecraft.tags.TagKey<Item> pickaxes, int[][] counts) {
		LootPool.Builder pool = LootPool.lootPool()
				.setRolls(ContextIntProviders.exactly(1))
				.when(this.doesNotHaveSilkTouch())
				.when(this.toolIn(pickaxes));
		for (int[] count : counts) {
			pool.add(this.applyExplosionDecay(ore, LootItem.lootTableItem(shard)
					.setWeight(count[1])
					.apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(count[0])))
					.apply(ApplyBonusCount.addOreBonusCount(this.enchantments.getOrThrow(Enchantments.FORTUNE)))));
		}
		return pool;
	}

	private LootPool.Builder binderPool(Block ore, float chance) {
		return LootPool.lootPool()
				.setRolls(ContextIntProviders.exactly(1))
				.when(this.doesNotHaveSilkTouch())
				.when(LootItemRandomChanceCondition.randomChance(chance))
				.add(this.applyExplosionDecay(ore, LootItem.lootTableItem(ModItems.SPECTRAL_BINDER)));
	}

	private LootItemCondition.Builder toolIn(net.minecraft.tags.TagKey<Item> tag) {
		return MatchTool.toolMatches(ItemPredicate.Builder.item().of(this.items, tag));
	}
}
