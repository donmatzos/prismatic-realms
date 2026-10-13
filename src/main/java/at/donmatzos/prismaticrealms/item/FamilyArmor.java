package at.donmatzos.prismaticrealms.item;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.family.CrystalFamily;

/**
 * One armor set (helmet, chestplate, leggings, boots) per colour family. All sets share netherite's stats; they differ
 * in look (recoloured netherite model, see tools/textures/family_armor_gen.py) and are repaired with the family crystal.
 */
public final class FamilyArmor {
	public static final ArmorType[] TYPES = {ArmorType.HELMET, ArmorType.CHESTPLATE, ArmorType.LEGGINGS, ArmorType.BOOTS};

	private static final int BASE_DURABILITY = 37;   // netherite

	private static final Map<CrystalFamily, Map<ArmorType, Item>> PIECES = new EnumMap<>(CrystalFamily.class);

	private static final Map<Item, CrystalFamily> FAMILY_OF = new HashMap<>();

	static {
		for (CrystalFamily family : CrystalFamily.values()) {
			ArmorMaterial material = new ArmorMaterial(
					BASE_DURABILITY,
					Map.of(ArmorType.HELMET, 3, ArmorType.CHESTPLATE, 8, ArmorType.LEGGINGS, 6, ArmorType.BOOTS, 3, ArmorType.BODY, 11),
					15,
					SoundEvents.ARMOR_EQUIP_NETHERITE,
					3.0F,
					0.1F,
					TagKey.create(Registries.ITEM, PrismaticRealms.id("repairs_" + family.id() + "_armor")),
					ResourceKey.create(EquipmentAssets.ROOT_ID, PrismaticRealms.id(family.id())));
			Map<ArmorType, Item> pieces = new EnumMap<>(ArmorType.class);
			for (ArmorType type : TYPES) {
				Item item = ModItems.register(family.id() + "_" + pieceName(type), Item::new,
						new Item.Properties().humanoidArmor(material, type).durability(type.getDurability(BASE_DURABILITY)));
				pieces.put(type, item);
				FAMILY_OF.put(item, family);
			}
			PIECES.put(family, pieces);
		}
	}

	private FamilyArmor() {
	}

	public static Item piece(CrystalFamily family, ArmorType type) {
		return PIECES.get(family).get(type);
	}

	/** The family a family armor piece belongs to, or null for any other item. */
	public static CrystalFamily familyOf(ItemStack stack) {
		return FAMILY_OF.get(stack.getItem());
	}

	public static String pieceName(ArmorType type) {
		return switch (type) {
			case HELMET -> "helmet";
			case CHESTPLATE -> "chestplate";
			case LEGGINGS -> "leggings";
			case BOOTS -> "boots";
			default -> throw new IllegalArgumentException(type.name());
		};
	}

	public static void initialize() {
	}
}
