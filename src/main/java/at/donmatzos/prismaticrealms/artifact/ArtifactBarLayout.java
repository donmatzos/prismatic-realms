package at.donmatzos.prismaticrealms.artifact;

/**
 * Geometry of the artifact bar, relative to the inventory window's top-left corner (plain Java, unit-tested). The panel
 * overlaps the window's right border so it looks attached; its three slots are stacked 18 px apart.
 */
public final class ArtifactBarLayout {
	public static final int PANEL_WIDTH = 29;
	public static final int PANEL_HEIGHT = 61;
	private static final int SLOT_SIZE = 16;

	/** panelX/panelY: the panel's top-left; slotX/firstSlotY: the first slot's item position (like Slot#x/y). */
	public record Layout(int panelX, int panelY, int slotX, int firstSlotY) {
	}

	/** Survival inventory (176 wide): slots at x 179, y 8/26/44, level with the top three armour slots. */
	public static final Layout SURVIVAL = new Layout(172, 4, 179, 8);
	/** Creative inventory tab (195 wide). */
	public static final Layout CREATIVE = new Layout(191, 0, 198, 4);

	private ArtifactBarLayout() {
	}

	public static int slotY(Layout layout, int index) {
		return layout.firstSlotY() + 18 * index;
	}

	public static boolean contains(Layout layout, double mouseX, double mouseY, int leftPos, int topPos) {
		double x = mouseX - leftPos - layout.panelX(), y = mouseY - topPos - layout.panelY();
		return x >= 0 && x < PANEL_WIDTH && y >= 0 && y < PANEL_HEIGHT;
	}

	public static boolean overSlot(Layout layout, double mouseX, double mouseY, int leftPos, int topPos) {
		double x = mouseX - leftPos - layout.slotX();
		if (x < 0 || x >= SLOT_SIZE) {
			return false;
		}
		for (int i = 0; i < ArtifactRules.SLOTS; i++) {
			double y = mouseY - topPos - slotY(layout, i);
			if (y >= 0 && y < SLOT_SIZE) {
				return true;
			}
		}
		return false;
	}
}
