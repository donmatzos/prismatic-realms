package at.donmatzos.prismaticrealms.artifact;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import at.donmatzos.prismaticrealms.effect.VenomStackEffect;
import at.donmatzos.prismaticrealms.item.ScorpionTailItem;
import at.donmatzos.prismaticrealms.particle.ModParticles;

/**
 * The Venom Crown artifact: hostile mobs (vanilla {@link Enemy}, never passive mobs, pets or players) within 6 blocks are
 * kept at Venom Stack II or more (Toxic Aura), and every fully charged melee hit adds a Venom Stack (Venomous Strike).
 * With the Scorpion Tail in hand the stacks go up to 10 instead of 5.
 */
public class VipersDiademItem extends ArtifactItem {
	private static final double AURA_RADIUS = 6.0;
	private static final int AURA_STACKS = 2;

	public VipersDiademItem(Item.Properties properties) {
		super(properties);
	}

	/** True when the causer holds the Scorpion Tail and wears the Diadem. */
	public static boolean boostsVenom(LivingEntity causer) {
		return causer instanceof Player player
				&& player.getMainHandItem().getItem() instanceof ScorpionTailItem
				&& ArtifactEffects.equipped(player).stream().anyMatch(artifact -> artifact instanceof VipersDiademItem);
	}

	@Override
	public void tick(ServerPlayer wearer) {
		ServerLevel level = wearer.level();
		double radiusSqr = AURA_RADIUS * AURA_RADIUS;
		for (LivingEntity mob : level.getEntitiesOfClass(LivingEntity.class, wearer.getBoundingBox().inflate(AURA_RADIUS),
				entity -> entity instanceof Enemy && entity.isAlive() && entity.distanceToSqr(wearer) <= radiusSqr)) {
			VenomStackEffect.ensureStacks(mob, wearer, AURA_STACKS);
		}
	}

	@Override
	public void onChargedHit(ServerPlayer wearer, LivingEntity target) {
		int stacks = VenomStackEffect.addStack(target, wearer);
		wearer.level().sendParticles(ModParticles.VENOM_DRIP, target.getX(), target.getY(0.6), target.getZ(), 6 + 3 * stacks,
				target.getBbWidth() * 0.4, target.getBbHeight() * 0.3, target.getBbWidth() * 0.4, 0.0);
	}
}
