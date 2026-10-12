package at.donmatzos.prismaticrealms.pouch;

import java.util.List;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

/** Tooltip data for a pouch: the stored entries (stacks may exceed 64) and the selected one (-1 = none). */
public record PouchTooltip(List<ItemStack> entries, int selected) implements TooltipComponent {
}
