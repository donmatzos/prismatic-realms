package at.donmatzos.prismaticrealms.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

import at.donmatzos.prismaticrealms.PrismaticRealms;

public final class ModParticles {
	/** White snow that drifts upwards (the Freeze effect); rendered by the client's RisingMoteParticle. */
	public static final SimpleParticleType FROST_FLURRY = Registry.register(BuiltInRegistries.PARTICLE_TYPE, PrismaticRealms.id("frost_flurry"), FabricParticleTypes.simple());
	/** Green motes drifting upwards (the Leech Seed effect). */
	public static final SimpleParticleType VERDANT_MOTE = Registry.register(BuiltInRegistries.PARTICLE_TYPE, PrismaticRealms.id("verdant_mote"), FabricParticleTypes.simple());
	/** Green spore flying along its spawn velocity (Leech Seed's drain, from the target to the planter). */
	public static final SimpleParticleType LEECH_SPORE = Registry.register(BuiltInRegistries.PARTICLE_TYPE, PrismaticRealms.id("leech_spore"), FabricParticleTypes.simple());

	/** Dark red drop falling from a bleeding entity (the Bleed effect). */
	public static final SimpleParticleType BLOOD_DRIP = Registry.register(BuiltInRegistries.PARTICLE_TYPE, PrismaticRealms.id("blood_drip"), FabricParticleTypes.simple());

	/** Yellow spark radiating outwards (the Blitz Rush effect). */
	public static final SimpleParticleType STATIC_SPARK = Registry.register(BuiltInRegistries.PARTICLE_TYPE, PrismaticRealms.id("static_spark"), FabricParticleTypes.simple());

	/** Glowing teal mote drifting upwards (the Tidecaller effect); rendered by RisingMoteParticle in teal. */
	public static final SimpleParticleType TIDE_MOTE = Registry.register(BuiltInRegistries.PARTICLE_TYPE, PrismaticRealms.id("tide_mote"), FabricParticleTypes.simple());

	/** Golden sun mote drifting upwards (the Radiance effect); rendered by RisingMoteParticle in gold. */
	public static final SimpleParticleType SUN_MOTE = Registry.register(BuiltInRegistries.PARTICLE_TYPE, PrismaticRealms.id("sun_mote"), FabricParticleTypes.simple());

	/** Venom drop falling from a target (the Venom Stack effect); rendered by DripParticle, mostly violet, 30 % acid green. */
	public static final SimpleParticleType VENOM_DRIP = Registry.register(BuiltInRegistries.PARTICLE_TYPE, PrismaticRealms.id("venom_drip"), FabricParticleTypes.simple());

	private ModParticles() {
	}

	public static void initialize() {
	}
}
