package at.donmatzos.prismaticrealms.recipe;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import at.donmatzos.prismaticrealms.PrismaticRealms;
import at.donmatzos.prismaticrealms.altar.AltarRecipe;

public final class ModRecipes {
	public static final RecipeType<AltarRecipe> ALTAR = Registry.register(BuiltInRegistries.RECIPE_TYPE, PrismaticRealms.id("altar"), new RecipeType<AltarRecipe>() {
		@Override
		public String toString() {
			return PrismaticRealms.id("altar").toString();
		}
	});
	public static final RecipeSerializer<AltarRecipe> ALTAR_SERIALIZER = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, PrismaticRealms.id("altar"), AltarRecipe.SERIALIZER);
	// Recipe requires a book category; altar recipes are special, so this one never shows in a recipe book.
	public static final RecipeBookCategory ALTAR_BOOK_CATEGORY = Registry.register(BuiltInRegistries.RECIPE_BOOK_CATEGORY, PrismaticRealms.id("altar"), new RecipeBookCategory());

	private ModRecipes() {
	}

	public static void initialize() {
	}
}
