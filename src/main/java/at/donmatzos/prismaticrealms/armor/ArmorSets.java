package at.donmatzos.prismaticrealms.armor;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import at.donmatzos.prismaticrealms.family.CrystalFamily;
import at.donmatzos.prismaticrealms.item.FamilyArmor;

public final class ArmorSets {
	private ArmorSets() {
	}

	/** The family whose armor set the entity wears (full set, or helmet, leggings and boots with an elytra), or null. */
	public static CrystalFamily of(LivingEntity entity) {
		ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
		return ArmorSetRules.activeSet(
				FamilyArmor.familyOf(entity.getItemBySlot(EquipmentSlot.HEAD)),
				FamilyArmor.familyOf(chest),
				chest.has(DataComponents.GLIDER),
				FamilyArmor.familyOf(entity.getItemBySlot(EquipmentSlot.LEGS)),
				FamilyArmor.familyOf(entity.getItemBySlot(EquipmentSlot.FEET)));
	}

	public static boolean has(LivingEntity entity, CrystalFamily family) {
		return of(entity) == family;
	}
}
