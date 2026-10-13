package at.donmatzos.prismaticrealms.armor.bonus;

import java.util.function.BiConsumer;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import at.donmatzos.prismaticrealms.armor.ArmorSetEffects;
import at.donmatzos.prismaticrealms.family.CrystalFamily;

/**
 * Cerulean Abyss set: full mining speed with the head underwater (the attribute defaults to 0.2, so +0.8), Night
 * Vision underwater, and no mining penalty off the ground (see PlayerDestroySpeedMixin).
 */
public class DeepDiver implements SetBonus {
	private static final int NIGHT_VISION_TICKS = 220;   // above vanilla's 200-tick flicker threshold

	@Override
	public void addModifiers(BiConsumer<Holder<Attribute>, AttributeModifier> out) {
		out.accept(Attributes.SUBMERGED_MINING_SPEED, new AttributeModifier(ArmorSetEffects.modifierId(CrystalFamily.CERULEAN_ABYSS, "submerged_mining_speed"), 0.8, AttributeModifier.Operation.ADD_VALUE));
	}

	@Override
	public void tick(ServerPlayer wearer) {
		MobEffectInstance current = wearer.getEffect(MobEffects.NIGHT_VISION);
		if (wearer.isEyeInFluid(FluidTags.WATER)) {
			if (current == null || (isOurs(current) && current.getDuration() < NIGHT_VISION_TICKS - 20)) {
				wearer.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, NIGHT_VISION_TICKS, 0, true, false, true));
			}
		} else if (current != null && isOurs(current)) {
			wearer.removeEffect(MobEffects.NIGHT_VISION);
		}
	}

	/** Our Night Vision is ambient, particle-less and never longer than NIGHT_VISION_TICKS; a potion's isn't. */
	private static boolean isOurs(MobEffectInstance effect) {
		return effect.isAmbient() && !effect.isVisible() && effect.getDuration() <= NIGHT_VISION_TICKS;
	}

	@Override
	public void onDeactivate(ServerPlayer wearer) {
		MobEffectInstance current = wearer.getEffect(MobEffects.NIGHT_VISION);
		if (current != null && isOurs(current)) {
			wearer.removeEffect(MobEffects.NIGHT_VISION);
		}
	}
}
