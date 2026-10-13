package at.donmatzos.prismaticrealms.artifact;

import java.util.List;
import java.util.ListIterator;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

/**
 * The Cruel Sun artifact: blocks the wearer breaks drop smelted (whatever a furnace makes of each drop, Fortune counts
 * kept, the furnace's experience as orbs; off with Silk Touch), and full fire immunity (every #is_fire damage type,
 * burning put out every tick).
 */
public class SunblessedTalismanItem extends ArtifactItem {
	public SunblessedTalismanItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public void modifyBlockDrops(ServerPlayer wearer, LootContext context, List<ItemStack> drops) {
		ServerLevel level = context.getLevel();
		ItemInstance tool = context.getOptional(LootContextParams.TOOL);
		if (tool != null && EnchantmentHelper.getItemEnchantmentLevel(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), tool) > 0) {
			return;
		}
		Vec3 origin = context.getOptional(LootContextParams.ORIGIN);
		int experience = 0;
		for (ListIterator<ItemStack> it = drops.listIterator(); it.hasNext(); ) {
			ItemStack drop = it.next();
			SingleRecipeInput input = new SingleRecipeInput(drop);
			var recipe = level.recipeAccess().getRecipeFor(RecipeType.SMELTING, input, level);
			if (recipe.isEmpty()) {
				continue;
			}
			ItemStack smelted = recipe.get().value().assemble(input);
			if (smelted.isEmpty()) {
				continue;
			}
			it.set(smelted.copyWithCount(smelted.getCount() * drop.getCount()));
			experience += SmeltExperience.reward(drop.getCount(), recipe.get().value().experience(), level.getRandom().nextFloat());
		}
		if (experience > 0 && origin != null) {
			ExperienceOrb.award(level, origin, experience);
		}
	}

	@Override
	public void tickMovement(ServerPlayer wearer) {
		if (wearer.isOnFire()) {
			wearer.clearFire();
		}
	}

	@Override
	public boolean preventsDamage(ServerPlayer wearer, DamageSource source) {
		return source.is(DamageTypeTags.IS_FIRE);
	}
}
