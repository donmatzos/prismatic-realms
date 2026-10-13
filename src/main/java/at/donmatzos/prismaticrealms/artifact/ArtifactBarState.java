package at.donmatzos.prismaticrealms.artifact;

/**
 * Whether the artifact bar is open (shown). Set by the client only (toggle button, saved in a config file); on a
 * dedicated server it stays at its default and is never read. Closing the bar only hides it: equipped artifacts keep working.
 */
public final class ArtifactBarState {
	private static volatile boolean open = true;

	private ArtifactBarState() {
	}

	public static boolean isOpen() {
		return open;
	}

	public static void setOpen(boolean value) {
		open = value;
	}
}
