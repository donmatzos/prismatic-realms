package at.donmatzos.prismaticrealms.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

import at.donmatzos.prismaticrealms.block.ModBlockEntities;
import at.donmatzos.prismaticrealms.client.altar.SpectralAltarRenderer;
import at.donmatzos.prismaticrealms.client.altar.SpectralAltarScreen;
import at.donmatzos.prismaticrealms.client.artifact.ArtifactBar;
import at.donmatzos.prismaticrealms.client.mixin.AbstractContainerScreenAccessor;
import at.donmatzos.prismaticrealms.client.particle.DripParticle;
import at.donmatzos.prismaticrealms.client.particle.LeechSporeParticle;
import at.donmatzos.prismaticrealms.client.particle.RisingMoteParticle;
import at.donmatzos.prismaticrealms.client.particle.StaticSparkParticle;
import at.donmatzos.prismaticrealms.client.pouch.ClientPouchTooltip;
import at.donmatzos.prismaticrealms.client.pouch.CrystalPouchScreen;
import at.donmatzos.prismaticrealms.client.pouch.PouchMouseActions;
import at.donmatzos.prismaticrealms.particle.ModParticles;
import at.donmatzos.prismaticrealms.pouch.ModMenus;
import at.donmatzos.prismaticrealms.pouch.PouchTooltip;

public class PrismaticRealmsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ArtifactBar.initialize();
		ModTooltips.initialize();
		MenuScreens.register(ModMenus.CRYSTAL_POUCH, CrystalPouchScreen::new);
		MenuScreens.register(ModMenus.SPECTRAL_ALTAR, SpectralAltarScreen::new);
		BlockEntityRenderers.register(ModBlockEntities.SPECTRAL_ALTAR, SpectralAltarRenderer::new);
		ClientTooltipComponentCallback.EVENT.register(data -> data instanceof PouchTooltip pouch ? new ClientPouchTooltip(pouch) : null);
		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			if (screen instanceof AbstractContainerScreen<?> containerScreen) {
				((AbstractContainerScreenAccessor) containerScreen).prismaticrealms$addItemSlotMouseAction(new PouchMouseActions());
			}
		});
		ParticleProviderRegistry registry = ParticleProviderRegistry.getInstance();
		registry.register(ModParticles.FROST_FLURRY, sprites -> new RisingMoteParticle.Provider(sprites, 1.0F, 1.0F, 1.0F));
		registry.register(ModParticles.VERDANT_MOTE, sprites -> new RisingMoteParticle.Provider(sprites, 0.48F, 0.88F, 0.34F));
		registry.register(ModParticles.LEECH_SPORE, LeechSporeParticle.Provider::new);
		registry.register(ModParticles.BLOOD_DRIP, sprites -> new DripParticle.Provider(sprites, 0.42F, 0.03F, 0.06F));
		registry.register(ModParticles.VENOM_DRIP, sprites -> new DripParticle.Provider(sprites, 0.62F, 0.28F, 0.84F, 0.56F, 0.82F, 0.16F, 0.3F));
		registry.register(ModParticles.STATIC_SPARK, StaticSparkParticle.Provider::new);
		registry.register(ModParticles.SUN_MOTE, sprites -> new RisingMoteParticle.Provider(sprites, 1.0F, 0.74F, 0.2F));
		registry.register(ModParticles.TIDE_MOTE, sprites -> new RisingMoteParticle.Provider(sprites, 0.44F, 0.96F, 0.86F));
	}
}
