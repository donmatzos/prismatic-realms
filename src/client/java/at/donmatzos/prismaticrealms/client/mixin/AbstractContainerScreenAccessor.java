package at.donmatzos.prismaticrealms.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.gui.ItemSlotMouseAction;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

/** Lets the mod add its scroll handler (pouch entry selection) to every container screen, like vanilla's bundle one. */
@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor {
	@Invoker("addItemSlotMouseAction")
	void prismaticrealms$addItemSlotMouseAction(ItemSlotMouseAction action);
}
