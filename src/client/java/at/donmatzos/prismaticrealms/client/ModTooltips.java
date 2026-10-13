package at.donmatzos.prismaticrealms.client;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import at.donmatzos.prismaticrealms.artifact.ArtifactItem;
import at.donmatzos.prismaticrealms.family.CrystalFamily;
import at.donmatzos.prismaticrealms.item.ChargedHitScytheItem;
import at.donmatzos.prismaticrealms.item.CrystalPouchItem;
import at.donmatzos.prismaticrealms.item.FamilyArmor;
import at.donmatzos.prismaticrealms.item.ModItems;
import at.donmatzos.prismaticrealms.pouch.PouchStorage;
import at.donmatzos.prismaticrealms.pouch.PouchText;
import at.donmatzos.prismaticrealms.tag.ModTags;

/**
 * The mod's extra tooltip lines, right below the item name, word-wrapped at {@link #MAX_CHARS} characters so tooltips stay
 * narrow: the crop harvest line (every scythe), the charged-hit effect for unique scythes ("Charged hit:" in grey, then the
 * description in the effect's colour, as one paragraph), and the Dowsing Rod's usage line. Texts come from the generated
 * en_us.json.
 */
public final class ModTooltips {
	/** Line length in characters before a word wraps to the next line. */
	public static final int MAX_CHARS = 30;
	private static final Style GREY = Style.EMPTY.withColor(ChatFormatting.GRAY);

	private ModTooltips() {
	}

	public static void initialize() {
		ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> {
			if (lines.isEmpty()) {
				return;
			}
			List<Component> extra = new ArrayList<>();
			if (stack.is(ModTags.SCYTHES)) {
				extra.addAll(wrap(List.of(new Segment(Component.translatable("item.prismaticrealms.scythe.harvest").getString(), GREY))));
				if (stack.getItem() instanceof ChargedHitScytheItem scythe) {
					Style effect = Style.EMPTY.withColor(TextColor.fromRgb(scythe.chargedHitEffect().value().getColor()));
					extra.addAll(wrap(List.of(
							new Segment(Component.translatable("item.prismaticrealms.charged_hit").getString(), GREY),
							new Segment(Component.translatable(stack.getItem().getDescriptionId() + ".effect").getString(), effect))));
				}
			} else if (stack.is(ModItems.DOWSING_ROD)) {
				extra.addAll(wrap(List.of(new Segment(Component.translatable("item.prismaticrealms.dowsing_rod.tooltip").getString(), GREY))));
			} else if (stack.getItem() instanceof CrystalPouchItem) {
				PouchStorage storage = CrystalPouchItem.storage(stack);
				extra.addAll(wrap(List.of(new Segment(Component.translatable("item.prismaticrealms.crystal_pouch.capacity",
						PouchText.grouped(PouchStorage.capacity(0, storage.tier())), PouchText.grouped(PouchStorage.capacity(PouchStorage.FAMILIES, storage.tier()))).getString(), GREY))));
				extra.addAll(wrap(List.of(new Segment(Component.translatable("item.prismaticrealms.crystal_pouch.stored",
						PouchText.grouped(storage.total(false)), PouchText.grouped(storage.total(true))).getString(), GREY))));
			} else if (stack.is(ModItems.CRYSTAL_POUCH_UPGRADE_PLUS_ONE) || stack.is(ModItems.CRYSTAL_POUCH_UPGRADE_PLUS_TWO)) {
				extra.addAll(wrap(List.of(new Segment(Component.translatable("item.prismaticrealms.crystal_pouch_upgrade.tooltip").getString(), GREY))));
			} else if (FamilyArmor.familyOf(stack) instanceof CrystalFamily family) {
				extra.addAll(wrap(List.of(new Segment(Component.translatable("item.prismaticrealms.armor_set").getString(), GREY))));
				extra.addAll(wrap(List.of(new Segment(Component.translatable("armor_set.prismaticrealms." + family.id()).getString(), Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE)))));
			} else if (stack.getItem() instanceof ArtifactItem artifact) {
				extra.addAll(wrap(List.of(new Segment(Component.translatable("item.prismaticrealms.artifact").getString(), GREY))));
				extra.addAll(wrap(List.of(new Segment(Component.translatable(artifact.bonusKey()).getString(), Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE)))));
			}
			lines.addAll(1, extra);
		});
	}

	public record Segment(String text, Style style) {
	}

	/** Greedy word wrap of styled text: a line takes words until the next one would pass MAX_CHARS. */
	public static List<Component> wrap(List<Segment> segments) {
		List<Component> out = new ArrayList<>();
		MutableComponent line = Component.empty();
		int length = 0;
		for (Segment segment : segments) {
			for (String word : segment.text().split(" ")) {
				if (word.isEmpty()) {
					continue;
				}
				if (length > 0 && length + 1 + word.length() > MAX_CHARS) {
					out.add(line);
					line = Component.empty();
					length = 0;
				}
				line.append(Component.literal(length > 0 ? " " + word : word).withStyle(segment.style()));
				length += (length > 0 ? 1 : 0) + word.length();
			}
		}
		if (length > 0) {
			out.add(line);
		}
		return out;
	}
}
