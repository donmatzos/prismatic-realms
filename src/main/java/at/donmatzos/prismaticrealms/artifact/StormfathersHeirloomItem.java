package at.donmatzos.prismaticrealms.artifact;

import java.util.function.BiConsumer;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;

/**
 * The Storm Fang artifact: +50 % movement speed and storm step (+0.5 step height, so single blocks are walked up).
 * The speed is ADD_MULTIPLIED_BASE, like Blitz Rush, so the two add up (base x 2) instead of multiplying; sprinting
 * (vanilla, ADD_MULTIPLIED_TOTAL) still multiplies on top.
 */
public class StormfathersHeirloomItem extends ArtifactItem {
	public StormfathersHeirloomItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public void addModifiers(BiConsumer<Holder<Attribute>, AttributeModifier> out) {
		out.accept(Attributes.MOVEMENT_SPEED, new AttributeModifier(this.modifierId("speed"), 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
		out.accept(Attributes.STEP_HEIGHT, new AttributeModifier(this.modifierId("step_height"), 0.5, AttributeModifier.Operation.ADD_VALUE));
	}
}
