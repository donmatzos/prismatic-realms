package at.donmatzos.prismaticrealms.armor.bonus;

import java.util.function.BiConsumer;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import at.donmatzos.prismaticrealms.armor.ArmorSetEffects;
import at.donmatzos.prismaticrealms.artifact.ArtifactItem;
import at.donmatzos.prismaticrealms.artifact.BloodstoneChaliceItem;
import at.donmatzos.prismaticrealms.family.CrystalFamily;

/** Crimson Sanguis set: +6 hearts, and the Bloodstone Chalice has a 50 % chance to stay in its slot when it saves you. */
public class SanguineVessel implements SetBonus {
	private static final double BONUS_HEALTH = 12.0;

	@Override
	public void addModifiers(BiConsumer<Holder<Attribute>, AttributeModifier> out) {
		out.accept(Attributes.MAX_HEALTH, new AttributeModifier(ArmorSetEffects.modifierId(CrystalFamily.CRIMSON_SANGUIS, "max_health"), BONUS_HEALTH, AttributeModifier.Operation.ADD_VALUE));
	}

	@Override
	public boolean keepsArtifact(ServerPlayer wearer, ArtifactItem artifact) {
		return artifact instanceof BloodstoneChaliceItem && wearer.getRandom().nextBoolean();
	}
}
