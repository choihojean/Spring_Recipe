package springfinal.recipe.mapper;

import springfinal.recipe.dto.RecipeIngredientDTO;
import springfinal.recipe.model.Ingredient;
import springfinal.recipe.model.Recipe;
import springfinal.recipe.model.RecipeIngredient;

public class RecipeIngredientMapper {
    public static RecipeIngredientDTO toDTO(RecipeIngredient entity) {
        return RecipeIngredientDTO.builder()
                .id(entity.getId())
                .recipeId(entity.getRecipe().getId())
                .ingredientId(entity.getIngredient().getId())
                .qty(entity.getQty())
                .build();
    }

    public static RecipeIngredient toEntity(RecipeIngredientDTO dto) {
        return RecipeIngredient.builder()
                .id(dto.getId())
                .recipe(Recipe.builder().id(dto.getRecipeId()).build())
                .ingredient(Ingredient.builder().id(dto.getIngredientId()).build())
                .qty(dto.getQty())
                .build();
    }
}
