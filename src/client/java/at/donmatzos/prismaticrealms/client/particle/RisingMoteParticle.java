package at.donmatzos.prismaticrealms.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SnowflakeParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

/** Vanilla's snowflake, tinted and drifting upwards instead of falling (Freeze: white frost flurry; Leech Seed: green motes). */
public class RisingMoteParticle extends SnowflakeParticle {
	protected RisingMoteParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, float r, float g, float b) {
		// ignore the spawn velocity: effect particles are spawned with (1, 1, 1), which is meant as a colour
		super(level, x, y, z, 0.0, 0.03, 0.0, sprites);
		this.gravity = -0.06F;
		this.lifetime += 10;
		this.setColor(r, g, b);
	}

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;
		private final float r, g, b;

		public Provider(SpriteSet sprites, float r, float g, float b) {
			this.sprites = sprites;
			this.r = r;
			this.g = g;
			this.b = b;
		}

		@Override
		public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
			return new RisingMoteParticle(level, x, y, z, this.sprites, this.r, this.g, this.b);
		}
	}
}
