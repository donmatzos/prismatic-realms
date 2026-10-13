package at.donmatzos.prismaticrealms.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;

/** The artifact toggle closes the recipe book (and the recipe book closes the artifact bar): both use the left side. */
@Mixin(AbstractRecipeBookScreen.class)
public interface AbstractRecipeBookScreenAccessor {
	@Accessor("recipeBookComponent")
	RecipeBookComponent<?> prismaticrealms$getRecipeBookComponent();
}
