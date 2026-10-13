package at.donmatzos.prismaticrealms.artifact;

import java.util.function.BiConsumer;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;

/**
 * The Cerulean Abyss artifact: endless breath with the eyes underwater, full walking speed in water (Depth Strider III)
 * and Dolphin's Grace while in water. Dolphin's Grace is topped up in short pieces, so it ends soon after leaving the
 * water, and a longer one from another source (potion, dolphins) is never cut short.
 */
public class AbyssalHeartItem extends ArtifactItem {
	private static final int GRACE_TICKS = 40;
	private static final int GRACE_REFRESH = 20;

	public AbyssalHeartItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public void addModifiers(BiConsumer<Holder<Attribute>, AttributeModifier> out) {
		out.accept(Attributes.WATER_MOVEMENT_EFFICIENCY, new AttributeModifier(this.modifierId("water_movement"), 1.0, AttributeModifier.Operation.ADD_VALUE));
	}

	@Override
	public void tickMovement(ServerPlayer wearer) {
		if (wearer.isEyeInFluid(FluidTags.WATER)) {
			wearer.setAirSupply(wearer.getMaxAirSupply());
		}
		if (wearer.isInWater()) {
			MobEffectInstance grace = wearer.getEffect(MobEffects.DOLPHINS_GRACE);
			if (grace == null || grace.endsWithin(GRACE_REFRESH)) {
				wearer.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, GRACE_TICKS, 0, true, false, true));
			}
		}
	}
}
