package at.donmatzos.prismaticrealms.item;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.Vec3;

import at.donmatzos.prismaticrealms.block.ModBlocks;
import at.donmatzos.prismaticrealms.dowsing.DowsingTargets;
import at.donmatzos.prismaticrealms.dowsing.DowsingText;

/**
 * Dowsing Rod: right-click with the rod in the main hand to find the nearest ore matching the off-hand item
 * ({@link DowsingTargets}) within {@link #RANGE} blocks. The result goes to the action bar (distance, compass point,
 * height; exact coordinates with Efficiency) and a short particle trail shows the way, visible to the user only. Each
 * scan costs {@link #COST} durability (Unbreaking applies) and starts a {@link #COOLDOWN_TICKS} cooldown; an invalid
 * off hand only shows a hint. Only loaded chunks are scanned.
 */
public class DowsingRodItem extends Item {
	public static final int RANGE = 32;
	public static final int COST = 2;
	public static final int COOLDOWN_TICKS = 3 * 20;
	private static final int TRAIL_BLOCKS = 6;

	public DowsingRodItem(Item.Properties properties) {
		super(properties);
	}

	private static List<String> families() {
		return ModBlocks.CRYSTAL_ORES.stream().map(ModBlocks.CrystalOre::family).toList();
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (hand != InteractionHand.MAIN_HAND) {
			return InteractionResult.PASS;
		}
		ItemStack rod = player.getItemInHand(hand);
		ItemStack held = player.getOffhandItem();
		Optional<DowsingTargets.Target> target = held.isEmpty() ? Optional.empty()
				: DowsingTargets.forItem(BuiltInRegistries.ITEM.getKey(held.getItem()).toString(), families());
		if (target.isEmpty()) {
			if (player instanceof ServerPlayer serverPlayer) {
				serverPlayer.sendOverlayMessage(Component.translatable("dowsing.prismaticrealms.hint"));
			}
			return InteractionResult.FAIL;
		}
		if (level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
			BlockPos origin = player.blockPosition();
			BlockPos found = scan(serverLevel, origin, matcher(target.get()));
			if (found == null) {
				Component name = BuiltInRegistries.BLOCK.getValue(Identifier.parse(target.get().displayBlockId())).getName();
				serverPlayer.sendOverlayMessage(Component.translatable("dowsing.prismaticrealms.none", name, RANGE));
			} else {
				serverPlayer.sendOverlayMessage(foundMessage(serverLevel, rod, origin, found));
				trail(serverLevel, serverPlayer, found);
			}
			// cooldown first: if the scan breaks the rod, hurtAndBreak empties the stack and the cooldown would land on air
			player.getCooldowns().addCooldown(rod, COOLDOWN_TICKS);
			rod.hurtAndBreak(COST, player, hand);
		}
		return InteractionResult.SUCCESS;
	}

	private static Predicate<BlockState> matcher(DowsingTargets.Target target) {
		if (target.kind() == DowsingTargets.Kind.FAMILY) {
			ModBlocks.CrystalOre ore = ModBlocks.CRYSTAL_ORES.stream()
					.filter(o -> o.family().equals(target.id())).findFirst().orElseThrow();
			return state -> state.is(ore.stone()) || state.is(ore.deepslate());
		}
		TagKey<Block> tag = TagKey.create(Registries.BLOCK, Identifier.parse(target.id()));
		return state -> state.is(tag);
	}

	/** The nearest matching block within RANGE of origin, or null. Skips unloaded chunks and sections without a match. */
	static BlockPos scan(ServerLevel level, BlockPos origin, Predicate<BlockState> match) {
		int minY = Math.max(level.getMinY(), origin.getY() - RANGE);
		int maxY = Math.min(level.getMaxY(), origin.getY() + RANGE);
		int minX = origin.getX() - RANGE, maxX = origin.getX() + RANGE;
		int minZ = origin.getZ() - RANGE, maxZ = origin.getZ() + RANGE;
		BlockPos best = null;
		long bestDistance = Long.MAX_VALUE;
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		for (int cx = minX >> 4; cx <= maxX >> 4; cx++) {
			for (int cz = minZ >> 4; cz <= maxZ >> 4; cz++) {
				LevelChunk chunk = level.getChunkSource().getChunkNow(cx, cz);
				if (chunk == null) {
					continue;
				}
				for (int sy = minY >> 4; sy <= maxY >> 4; sy++) {
					LevelChunkSection section = chunk.getSection(level.getSectionIndexFromSectionY(sy));
					if (section.hasOnlyAir() || !section.maybeHas(match)) {
						continue;
					}
					for (int y = Math.max(minY, sy << 4); y <= Math.min(maxY, (sy << 4) + 15); y++) {
						for (int x = Math.max(minX, cx << 4); x <= Math.min(maxX, (cx << 4) + 15); x++) {
							for (int z = Math.max(minZ, cz << 4); z <= Math.min(maxZ, (cz << 4) + 15); z++) {
								if (match.test(section.getBlockState(x & 15, y & 15, z & 15))) {
									cursor.set(x, y, z);
									long distance = (long) cursor.distSqr(origin);
									if (distance < bestDistance) {
										bestDistance = distance;
										best = cursor.immutable();
									}
								}
							}
						}
					}
				}
			}
		}
		return best;
	}

	private static Component foundMessage(ServerLevel level, ItemStack rod, BlockPos origin, BlockPos found) {
		int dx = found.getX() - origin.getX(), dy = found.getY() - origin.getY(), dz = found.getZ() - origin.getZ();
		long distance = Math.round(Math.sqrt(found.distSqr(origin)));
		Component name = level.getBlockState(found).getBlock().getName();
		Component direction = Component.translatable("dowsing.prismaticrealms.direction." + DowsingText.compassPoint(dx, dz));
		String heightKey = DowsingText.heightKey(dy);
		Component height = Component.translatable("dowsing.prismaticrealms.height." + heightKey, Math.abs(dy));
		MutableComponent message = Component.translatable("dowsing.prismaticrealms.found", name, distance, direction, height);
		int efficiency = EnchantmentHelper.getItemEnchantmentLevel(
				level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.EFFICIENCY), rod);
		if (efficiency > 0) {
			message.append(Component.translatable("dowsing.prismaticrealms.coords", found.getX(), found.getY(), found.getZ()));
		}
		return message;
	}

	/** A short line of particles from the player's eyes towards the ore, sent to this player only. */
	private static void trail(ServerLevel level, ServerPlayer player, BlockPos found) {
		Vec3 from = player.getEyePosition();
		Vec3 towards = Vec3.atCenterOf(found).subtract(from);
		double length = Math.min(TRAIL_BLOCKS, towards.length());
		Vec3 step = towards.normalize().scale(0.5);
		for (int i = 1; i * 0.5 <= length; i++) {
			Vec3 point = from.add(step.scale(i));
			level.sendParticles(player, ParticleTypes.END_ROD, true, false, point.x, point.y, point.z, 1, 0.0, 0.0, 0.0, 0.0);
		}
	}
}
