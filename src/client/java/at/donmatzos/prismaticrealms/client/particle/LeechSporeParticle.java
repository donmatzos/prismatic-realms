package at.donmatzos.prismaticrealms.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

/**
 * Green spore that flies in a straight line with its spawn velocity and fades out after 16 ticks; Leech Seed sends it
 * from the target to the planter with velocity = distance / 16, so it arrives as it disappears.
 */
public class LeechSporeParticle extends SingleQuadParticle {
	private final SpriteSet sprites;

	protected LeechSporeParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
		super(level, x, y, z, sprites.first());
		this.sprites = sprites;
		this.xd = xd;
		this.yd = yd;
		this.zd = zd;
		this.friction = 1.0F;
		this.gravity = 0.0F;
		this.hasPhysics = false;
		this.lifetime = 16;
		this.quadSize = 0.09F;
		this.setColor(0.42F, 0.86F, 0.28F);
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
	}

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
			return new LeechSporeParticle(level, x, y, z, xAux, yAux, zAux, this.sprites);
		}
	}
}
