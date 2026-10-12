package at.donmatzos.prismaticrealms.client.altar;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class SpectralAltarRenderState extends BlockEntityRenderState {
	/** Animation time within one period (SpectralAltarAnimation.wrapTime). */
	public float time;
	public float orbit;
	/** One per family shard, in CRYSTAL_ORES order; reused across frames. */
	public final List<ItemStackRenderState> shards = new ArrayList<>();
}
