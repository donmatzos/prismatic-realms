package at.donmatzos.prismaticrealms.artifact;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

/**
 * The Overgrown Prosperity artifact: plants near the wearer grow faster (each second every growing plant within 5 blocks
 * sideways and 2 up or down gets an extra vanilla random tick with an 8 % chance, roughly 5-6 times vanilla speed), and
 * broken logs drop twice their log items (leaves and everything else are untouched).
 */
public class VerdantAuraItem extends ArtifactItem {
	private static final int RADIUS = 5;
	private static final int HEIGHT = 2;
	private static final float GROWTH_CHANCE = 0.08F;

	public VerdantAuraItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public void tick(ServerPlayer wearer) {
		ServerLevel level = wearer.level();
		RandomSource random = level.getRandom();
		BlockPos center = wearer.blockPosition();
		for (BlockPos pos : BlockPos.betweenClosed(center.offset(-RADIUS, -HEIGHT, -RADIUS), center.offset(RADIUS, HEIGHT, RADIUS))) {
			BlockState state = level.getBlockState(pos);
			if (isGrowingPlant(state.getBlock()) && state.isRandomlyTicking() && random.nextFloat() < GROWTH_CHANCE) {
				state.randomTick(level, pos.immutable(), random);
			}
		}
	}

	private static boolean isGrowingPlant(Block block) {
		return block instanceof CropBlock || block instanceof SaplingBlock || block instanceof StemBlock
				|| block instanceof NetherWartBlock || block instanceof SweetBerryBushBlock || block instanceof CocoaBlock;
	}

	@Override
	public void modifyBlockDrops(ServerPlayer wearer, LootContext context, List<ItemStack> drops) {
		drops.replaceAll(drop -> drop.is(ItemTags.LOGS) ? drop.copyWithCount(drop.getCount() * 2) : drop);
	}
}
