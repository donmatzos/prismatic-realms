package at.donmatzos.prismaticrealms.item;

import java.util.List;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import at.donmatzos.prismaticrealms.tag.ModTags;

/**
 * Right-clicking a fully grown crop with any scythe ({@code #prismaticrealms:scythes}) harvests it with double drops and
 * replants it (back to age 0, one seed taken out of the drops). Fully grown crops directly north, south, east and west of
 * it are harvested the same way. One click costs one durability, however many crops it takes. Unripe crops are left
 * alone, at no cost. Crops: every {@link CropBlock} (wheat, carrots, potatoes, beetroots, modded crops built on it) and
 * nether wart.
 */
public final class ScytheHarvest {
	private ScytheHarvest() {
	}

	public static void initialize() {
		UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
			ItemStack stack = player.getItemInHand(hand);
			BlockPos pos = hitResult.getBlockPos();
			if (player.isSpectator() || !stack.is(ModTags.SCYTHES) || !isRipeCrop(level.getBlockState(pos))
					|| !level.mayInteract(player, pos) || !player.mayBuild()) {
				return InteractionResult.PASS;
			}
			if (level instanceof ServerLevel serverLevel) {
				harvest(serverLevel, pos, player, stack);
				for (Direction side : Direction.Plane.HORIZONTAL) {
					BlockPos next = pos.relative(side);
					if (isRipeCrop(level.getBlockState(next)) && level.mayInteract(player, next)) {
						harvest(serverLevel, next, player, stack);
					}
				}
				stack.hurtAndBreak(1, player, hand);
			}
			return InteractionResult.SUCCESS;
		});
	}

	private static boolean isRipeCrop(BlockState state) {
		if (state.getBlock() instanceof CropBlock crop) {
			return crop.isMaxAge(state);
		}
		return state.getBlock() instanceof NetherWartBlock && state.getValue(NetherWartBlock.AGE) >= NetherWartBlock.MAX_AGE;
	}

	private static BlockState replanted(BlockState state) {
		return state.getBlock() instanceof CropBlock crop ? crop.getStateForAge(0) : state.setValue(NetherWartBlock.AGE, 0);
	}

	private static void harvest(ServerLevel level, BlockPos pos, Player player, ItemStack tool) {
		BlockState state = level.getBlockState(pos);
		List<ItemStack> drops = Block.getDrops(state, level, pos, null, player, tool);
		// the crop's own item is what it is planted with (wheat seeds, carrot, potato, beetroot seeds, nether wart)
		Item seed = state.getBlock().asItem();
		boolean seedTaken = false;
		for (ItemStack drop : drops) {
			drop.setCount(drop.getCount() * 2);
			if (!seedTaken && drop.is(seed)) {
				drop.shrink(1);
				seedTaken = true;
			}
			if (!drop.isEmpty()) {
				Block.popResource(level, pos, drop);
			}
		}
		BlockState young = replanted(state);
		level.setBlock(pos, young, Block.UPDATE_ALL);
		level.levelEvent(null, LevelEvent.PARTICLES_AND_SOUND_DESTROY_BLOCK, pos, Block.getId(state));
		level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, young));
	}
}
