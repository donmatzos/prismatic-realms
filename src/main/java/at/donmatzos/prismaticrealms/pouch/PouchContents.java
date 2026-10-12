package at.donmatzos.prismaticrealms.pouch;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * The Crystal Pouch's stored counts (14, see PouchStorage) plus the bundle-style selected entry (-1 = none), as an immutable
 * data component value. Like vanilla's BundleContents, the persistent CODEC leaves the selection out (it is never saved or
 * hashed for container clicks); only the STREAM_CODEC carries it.
 */
public record PouchContents(List<Integer> counts, int selected) {
	public static final PouchContents EMPTY = of(new int[PouchStorage.SLOTS]);
	public static final Codec<PouchContents> CODEC = Codec.INT.listOf().xmap(counts -> new PouchContents(counts, -1), PouchContents::counts);
	public static final StreamCodec<RegistryFriendlyByteBuf, PouchContents> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list(PouchStorage.SLOTS)).cast(), PouchContents::counts, ByteBufCodecs.VAR_INT, PouchContents::selected, PouchContents::new);

	public PouchContents {
		List<Integer> fixed = new ArrayList<>(counts);
		while (fixed.size() < PouchStorage.SLOTS) {
			fixed.add(0);
		}
		counts = List.copyOf(fixed.subList(0, PouchStorage.SLOTS));
	}

	public static PouchContents of(int[] counts) {
		List<Integer> list = new ArrayList<>(PouchStorage.SLOTS);
		for (int count : counts) {
			list.add(count);
		}
		return new PouchContents(list, -1);
	}

	public PouchContents withSelected(int selected) {
		return new PouchContents(this.counts, selected);
	}

	public int[] toArray() {
		return this.counts.stream().mapToInt(Integer::intValue).toArray();
	}
}
