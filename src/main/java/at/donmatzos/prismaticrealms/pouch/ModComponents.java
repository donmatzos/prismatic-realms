package at.donmatzos.prismaticrealms.pouch;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import at.donmatzos.prismaticrealms.PrismaticRealms;

public final class ModComponents {
	/** The pouch's stored counts; saved with the item (without the selection) and synced to clients (with it). */
	public static final DataComponentType<PouchContents> POUCH_CONTENTS = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
			PrismaticRealms.id("pouch_contents"), DataComponentType.<PouchContents>builder()
					.persistent(PouchContents.CODEC).networkSynchronized(PouchContents.STREAM_CODEC).cacheEncoding().build());

	private ModComponents() {
	}

	public static void initialize() {
	}
}
