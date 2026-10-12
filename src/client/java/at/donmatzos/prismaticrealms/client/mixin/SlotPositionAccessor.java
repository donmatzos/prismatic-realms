package at.donmatzos.prismaticrealms.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.inventory.Slot;

/** Slot x/y are final; the creative inventory's slot wrappers for the artifact bar get repositioned through this. */
@Mixin(Slot.class)
public interface SlotPositionAccessor {
	@Mutable
	@Accessor("x")
	void prismaticrealms$setX(int x);

	@Mutable
	@Accessor("y")
	void prismaticrealms$setY(int y);
}
