package at.donmatzos.prismaticrealms.item;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** A unique scythe with an extra effect on fully charged hits (attack charge > 0.9, vanilla's "full strength" threshold). */
public abstract class ChargedHitScytheItem extends Item {
	// Vanilla resets the attack charge (Player#onAttack) before Item#hurtEnemy runs, so the charge is read in
	// AttackEntityCallback, which fires before the attack, and handed over per player.
	private static final Map<UUID, Boolean> FULLY_CHARGED = new ConcurrentHashMap<>();

	protected ChargedHitScytheItem(Item.Properties properties) {
		super(properties);
	}

	public static void initialize() {
		AttackEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> {
			if (!level.isClientSide() && player.getItemInHand(hand).getItem() instanceof ChargedHitScytheItem) {
				FULLY_CHARGED.put(player.getUUID(), player.getAttackStrengthScale(0.5F) > 0.9F);
			}
			return InteractionResult.PASS;
		});
	}

	/** The effect this scythe applies; its colour tints the tooltip description (client ModTooltips). */
	public abstract Holder<MobEffect> chargedHitEffect();

	/** Called on the server for the directly hit target of a fully charged hit (swept targets don't count). */
	protected abstract void onChargedHit(ServerLevel level, ItemStack stack, LivingEntity target, LivingEntity attacker);

	@Override
	public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		super.hurtEnemy(stack, target, attacker);
		// mobs wielding the scythe have no charge meter, so their hits always count as charged
		boolean fullyCharged = !(attacker instanceof Player player) || Boolean.TRUE.equals(FULLY_CHARGED.remove(player.getUUID()));
		if (fullyCharged && target.level() instanceof ServerLevel level) {
			this.onChargedHit(level, stack, target, attacker);
		}
	}
}
