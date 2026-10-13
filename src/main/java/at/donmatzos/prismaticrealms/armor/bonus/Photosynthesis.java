package at.donmatzos.prismaticrealms.armor.bonus;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;

/**
 * Overgrown Prosperity set: in daylight under open sky hunger never drains (the food level is held; saturation is not
 * restored, so vanilla regeneration is not sped up) and the wearer heals 1 health every 4 s on top of vanilla's own regeneration.
 */
public class Photosynthesis implements SetBonus {
	private static final int HEAL_INTERVAL_TICKS = 80;

	private final Map<UUID, FoodGuard> guards = new ConcurrentHashMap<>();

	private static boolean inSunlight(ServerPlayer player) {
		ServerLevel level = player.level();
		return level.isBrightOutside() && level.canSeeSky(player.blockPosition().above());
	}

	@Override
	public void tick(ServerPlayer wearer) {
		FoodGuard guard = this.guards.computeIfAbsent(wearer.getUUID(), id -> new FoodGuard());
		if (!inSunlight(wearer)) {
			guard.reset();
			return;
		}
		FoodData food = wearer.getFoodData();
		FoodGuard.Level level = guard.update(food.getFoodLevel(), food.getSaturationLevel());
		if (food.getFoodLevel() < level.food()) {
			food.setFoodLevel(level.food());
		}
		if (wearer.tickCount % HEAL_INTERVAL_TICKS == 0 && wearer.getHealth() < wearer.getMaxHealth()) {
			wearer.heal(1.0F);
		}
	}

	@Override
	public void onDeactivate(ServerPlayer wearer) {
		this.guards.remove(wearer.getUUID());
	}
}
