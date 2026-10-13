package at.donmatzos.prismaticrealms.client.artifact;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;

import at.donmatzos.prismaticrealms.artifact.ArtifactBreakPayload;

/** Client side of {@link ArtifactBreakPayload}: vanilla's totem effect (entity event 35) with the artifact and red particles. */
public final class ArtifactBreakEffect {
	private static final DustParticleOptions BLOOD = new DustParticleOptions(0xB0101C, 1.3F);

	private ArtifactBreakEffect() {
	}

	public static void initialize() {
		ClientPlayNetworking.registerGlobalReceiver(ArtifactBreakPayload.TYPE, (payload, context) -> {
			Minecraft minecraft = context.client();
			if (minecraft.level == null) {
				return;
			}
			Entity entity = minecraft.level.getEntity(payload.entityId());
			if (entity == null) {
				return;
			}
			minecraft.particleEngine.createTrackingEmitter(entity, BLOOD, 30);
			minecraft.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);
			if (entity == minecraft.player) {
				minecraft.player.displayItemActivation(payload.stack());
			}
		});
	}
}
