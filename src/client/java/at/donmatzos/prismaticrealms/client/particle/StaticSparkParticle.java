package at.donmatzos.prismaticrealms.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

/**
 * Yellow static spark (Blitz Rush): shoots outwards in a random direction, slows down and flickers out within a few ticks,
 * so a stream of them looks like electricity radiating off the entity. The spawn velocity is ignored (effect particles are
 * spawned with (1, 1, 1), meant as a colour); every spark picks its own outward direction.
 */
public class StaticSparkParticle extends SingleQuadParticle {
	private final SpriteSet sprites;

	protected StaticSparkParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
		super(level, x, y, z, sprites.first());
		this.sprites = sprites;
		double angle = this.random.nextDouble() * Math.PI * 2.0;
		double speed = 0.08 + this.random.nextDouble() * 0.08;
		this.xd = Math.cos(angle) * speed;
		this.zd = Math.sin(angle) * speed;
		this.yd = (this.random.nextDouble() - 0.3) * 0.08;
		this.friction = 0.82F;
		this.gravity = 0.0F;
		this.hasPhysics = false;
		this.lifetime = 5 + this.random.nextInt(5);
		this.quadSize = 0.05F + this.random.nextFloat() * 0.04F;
		this.setColor(1.0F, 0.9F, 0.25F);
		this.setSpriteFromAge(sprites);
	}

	@Override
	public SingleQuadParticle.Layer getLayer() {
		return SingleQuadParticle.Layer.OPAQUE;
	}

	@Override
	public void tick() {
		super.tick();
		this.setSpriteFromAge(this.sprites);
		// flicker: alternate between bright yellow and pale white-yellow like an electric crackle
		boolean bright = this.random.nextBoolean();
		this.setColor(1.0F, bright ? 0.92F : 1.0F, bright ? 0.22F : 0.7F);
	}

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
			return new StaticSparkParticle(level, x, y, z, this.sprites);
		}
	}
}
