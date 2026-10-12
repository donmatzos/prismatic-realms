package at.donmatzos.prismaticrealms.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SnowflakeParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

/** A drop that falls from an afflicted entity (vanilla's snowflake sprite and motion, heavier), tinted: dark red blood; venom mostly violet with some acid-green drops. */
public class DripParticle extends SnowflakeParticle {
	protected DripParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, float r, float g, float b) {
		// ignore the spawn velocity: effect particles are spawned with (1, 1, 1), which is meant as a colour
		super(level, x, y, z, 0.0, -0.02, 0.0, sprites);
		this.gravity = 0.6F;
		this.setColor(r, g, b);
	}

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;
		private final float r;
		private final float g;
		private final float b;
		// optional second tint, used for a share of the drops (altChance 0 = always the main tint)
		private final float altR;
		private final float altG;
		private final float altB;
		private final float altChance;

		public Provider(SpriteSet sprites, float r, float g, float b) {
			this(sprites, r, g, b, 0.0F, 0.0F, 0.0F, 0.0F);
		}

		public Provider(SpriteSet sprites, float r, float g, float b, float altR, float altG, float altB, float altChance) {
			this.sprites = sprites;
			this.r = r;
			this.g = g;
			this.b = b;
			this.altR = altR;
			this.altG = altG;
			this.altB = altB;
			this.altChance = altChance;
		}

		@Override
		public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
			if (random.nextFloat() < this.altChance) {
				return new DripParticle(level, x, y, z, this.sprites, this.altR, this.altG, this.altB);
			}
			return new DripParticle(level, x, y, z, this.sprites, this.r, this.g, this.b);
		}
	}
}
