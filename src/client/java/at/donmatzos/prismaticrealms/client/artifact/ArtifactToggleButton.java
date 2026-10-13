package at.donmatzos.prismaticrealms.client.artifact;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.artifact.ArtifactBarState;

/** The button that opens and closes the artifact bar; it shows as pressed while the bar is open. */
public class ArtifactToggleButton extends ImageButton {
	private static final WidgetSprites OPEN = new WidgetSprites(PrismaticRealms.id("container/artifacts/toggle_open"),
			PrismaticRealms.id("container/artifacts/toggle_open_highlighted"));
	private static final WidgetSprites CLOSED = new WidgetSprites(PrismaticRealms.id("container/artifacts/toggle_closed"),
			PrismaticRealms.id("container/artifacts/toggle_closed_highlighted"));

	public ArtifactToggleButton(int x, int y, OnPress onPress) {
		super(x, y, 20, 18, CLOSED, onPress, Component.translatable("container.prismaticrealms.artifacts.toggle"));
		this.setTooltip(Tooltip.create(Component.translatable("container.prismaticrealms.artifacts.toggle")));
	}

	@Override
	public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, (ArtifactBarState.isOpen() ? OPEN : CLOSED).get(true, this.isHoveredOrFocused()),
				this.getX(), this.getY(), this.width, this.height);
	}
}
