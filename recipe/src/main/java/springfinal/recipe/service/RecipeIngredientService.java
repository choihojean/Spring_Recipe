package springfinal.recipe.service;
import springfinal.recipe.dto.IngredientDTO;
import springfinal.recipe.dto.RecipeIngredientDTO;

import java.util.List;

public interface RecipeIngredientService {
    void saveRecipeIngredients(Long recipeId, List<IngredientDTO> ingredients);
    List<RecipeIngredientDTO> getIngredientsByRecipeId(Long recipeId);
    List<RecipeIngredientDTO> getRecipesByIngredientId(Long ingredientId);
    void deleteRecipeIngredient(Long id);
}
