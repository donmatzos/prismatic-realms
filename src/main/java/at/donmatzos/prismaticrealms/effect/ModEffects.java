package at.donmatzos.prismaticrealms.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import at.donmatzos.prismaticrealms.PrismaticRealms;

public final class ModEffects {
	/** Movement speed x0.25 (-75 %), applied for 3 seconds by fully charged hits of the Scythe of the Frozen Expanse. */
	public static final Holder<MobEffect> FREEZE = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, PrismaticRealms.id("freeze"),
			new FreezeEffect(MobEffectCategory.HARMFUL, 0xBEEBFF)
					.addAttributeModifier(Attributes.MOVEMENT_SPEED, PrismaticRealms.id("effect.freeze"), -0.75, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

	/** Drains 1 health per second for 5 seconds and heals whoever planted it (Overgrown Reaper, fully charged hits). */
	public static final Holder<MobEffect> LEECH_SEED = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, PrismaticRealms.id("leech_seed"),
			new LeechSeedEffect(MobEffectCategory.HARMFUL, 0x6BBF3A));

	/** 1 damage per second for 5 seconds, 2 while the target keeps moving (Crimson Diabell, fully charged hits). */
	public static final Holder<MobEffect> BLEED = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, PrismaticRealms.id("bleed"),
			new BleedEffect(MobEffectCategory.HARMFUL, 0x8A0F1A));

	/** Beneficial: movement speed +50 % for 3 seconds, given to the wielder by fully charged Blitz Slasher hits. */
	public static final Holder<MobEffect> BLITZ_RUSH = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, PrismaticRealms.id("blitz_rush"),
			new BlitzRushEffect(MobEffectCategory.BENEFICIAL, 0xFFE04A)
					.addAttributeModifier(Attributes.MOVEMENT_SPEED, PrismaticRealms.id("effect.blitz_rush"), 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

	/** Beneficial marker: comes with Dolphin's Grace and Water Breathing; The Shallow Waters' hits heal in water while it lasts. */
	public static final Holder<MobEffect> TIDECALLER = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, PrismaticRealms.id("tidecaller"),
			new TidecallerEffect(MobEffectCategory.BENEFICIAL, 0x2FD8C8));

	/** Beneficial: whoever damages the bearer catches fire; comes with Fire Resistance (Melting Sunray, fully charged hits). */
	public static final Holder<MobEffect> RADIANCE = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, PrismaticRealms.id("radiance"),
			new RadianceEffect(MobEffectCategory.BENEFICIAL, 0xFFB52E));

	/** 0.5 damage per stack per second for 6 seconds; each fully charged Scorpion Tail hit adds a stack (max 5) and resets it. */
	public static final Holder<MobEffect> VENOM_STACK = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, PrismaticRealms.id("venom_stack"),
			new VenomStackEffect(MobEffectCategory.HARMFUL, 0x9E47D6));

	private ModEffects() {
	}

	public static void initialize() {
	}
}
