package at.donmatzos.prismaticrealms.pouch;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.item.CrystalPouchItem;

/** Client -> server: the entry selected by scrolling over the pouch in menu slot `slot` (-1 = none). */
public record SelectPouchEntryPayload(int slot, int selected) implements CustomPacketPayload {
	public static final Type<SelectPouchEntryPayload> TYPE = new Type<>(PrismaticRealms.id("select_pouch_entry"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SelectPouchEntryPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, SelectPouchEntryPayload::slot, ByteBufCodecs.VAR_INT, SelectPouchEntryPayload::selected, SelectPouchEntryPayload::new);

	@Override
	public Type<SelectPouchEntryPayload> type() {
		return TYPE;
	}

	public static void initialize() {
		PayloadTypeRegistry.serverboundPlay().register(TYPE, STREAM_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(TYPE, (payload, context) -> {
			AbstractContainerMenu menu = context.player().containerMenu;
			if (payload.slot() < 0 || payload.slot() >= menu.slots.size()) {
				return;
			}
			ItemStack stack = menu.getSlot(payload.slot()).getItem();
			if (stack.getItem() instanceof CrystalPouchItem) {
				CrystalPouchItem.select(stack, payload.selected());   // out-of-range values clear the selection
			}
		});
	}
}
