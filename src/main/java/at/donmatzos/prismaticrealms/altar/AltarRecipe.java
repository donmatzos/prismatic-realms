package at.donmatzos.prismaticrealms.altar;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import at.donmatzos.prismaticrealms.item.ModItems;
import at.donmatzos.prismaticrealms.recipe.ModRecipes;

/**
 * A Spectral Altar recipe: crystal on top, the same shard ingredient in both side slots, a catalyst at the bottom, and
 * always a Spectral Setting in the centre (implied, so every artifact recipe shares the shape). Fuel is checked by the
 * menu, not here. Special: never shown in the recipe book.
 */
public class AltarRecipe implements Recipe<AltarRecipeInput> {
	public static final MapCodec<AltarRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Ingredient.CODEC.fieldOf("crystal").forGetter(r -> r.crystal),
			Ingredient.CODEC.fieldOf("shards").forGetter(r -> r.shards),
			Ingredient.CODEC.fieldOf("catalyst").forGetter(r -> r.catalyst),
			ItemStackTemplate.CODEC.fieldOf("result").forGetter(r -> r.result))
			.apply(i, AltarRecipe::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, AltarRecipe> STREAM_CODEC = StreamCodec.composite(
			Ingredient.CONTENTS_STREAM_CODEC, r -> r.crystal,
			Ingredient.CONTENTS_STREAM_CODEC, r -> r.shards,
			Ingredient.CONTENTS_STREAM_CODEC, r -> r.catalyst,
			ItemStackTemplate.STREAM_CODEC, r -> r.result,
			AltarRecipe::new);
	public static final RecipeSerializer<AltarRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private final Ingredient crystal;
	private final Ingredient shards;
	private final Ingredient catalyst;
	private final ItemStackTemplate result;

	public AltarRecipe(Ingredient crystal, Ingredient shards, Ingredient catalyst, ItemStackTemplate result) {
		this.crystal = crystal;
		this.shards = shards;
		this.catalyst = catalyst;
		this.result = result;
	}

	@Override
	public boolean matches(AltarRecipeInput input, Level level) {
		return input.setting().is(ModItems.SPECTRAL_SETTING)
				&& this.crystal.test(input.crystal())
				&& this.shards.test(input.leftShard())
				&& this.shards.test(input.rightShard())
				&& this.catalyst.test(input.catalyst());
	}

	@Override
	public ItemStack assemble(AltarRecipeInput input) {
		return this.result.create();
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public boolean showNotification() {
		return false;
	}

	@Override
	public String group() {
		return "";
	}

	@Override
	public RecipeSerializer<AltarRecipe> getSerializer() {
		return ModRecipes.ALTAR_SERIALIZER;
	}

	@Override
	public RecipeType<AltarRecipe> getType() {
		return ModRecipes.ALTAR;
	}

	@Override
	public PlacementInfo placementInfo() {
		return PlacementInfo.NOT_PLACEABLE;
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return ModRecipes.ALTAR_BOOK_CATEGORY;
	}
}
