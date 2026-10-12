package at.donmatzos.prismaticrealms.artifact;

import java.util.function.BiConsumer;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;

/** Creative-only artifact for testing the artifact system: +1 armour while equipped. Not craftable. */
public class TestArtifactItem extends ArtifactItem {
	public TestArtifactItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public void addModifiers(BiConsumer<Holder<Attribute>, AttributeModifier> out) {
		out.accept(Attributes.ARMOR, new AttributeModifier(this.modifierId("armor"), 1.0, AttributeModifier.Operation.ADD_VALUE));
	}
}
