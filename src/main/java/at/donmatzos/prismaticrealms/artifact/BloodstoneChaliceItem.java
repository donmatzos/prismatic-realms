package at.donmatzos.prismaticrealms.artifact;

import java.util.function.BiConsumer;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;

/**
 * The Crimson Sanguis artifact (Blood Pact): +4 max hearts while worn. Lethal damage breaks the chalice instead, which
 * heals the wearer to full health (without the bonus hearts, they leave with it), like a Totem of Undying.
 */
public class BloodstoneChaliceItem extends ArtifactItem {
	private static final double BONUS_HEALTH = 8.0;

	public BloodstoneChaliceItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public void addModifiers(BiConsumer<Holder<Attribute>, AttributeModifier> out) {
		out.accept(Attributes.MAX_HEALTH, new AttributeModifier(this.modifierId("max_health"), BONUS_HEALTH, AttributeModifier.Operation.ADD_VALUE));
	}

	@Override
	public boolean savesFromDeath(ServerPlayer wearer, DamageSource source) {
		return true;
	}
}
