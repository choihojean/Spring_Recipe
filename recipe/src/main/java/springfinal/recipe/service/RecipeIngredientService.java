package springfinal.recipe.service;
import springfinal.recipe.dto.RecipeIngredientDTO;

import java.util.List;

public interface RecipeIngredientService {
    void saveRecipeIngredient(RecipeIngredientDTO dto);
    List<RecipeIngredientDTO> getIngredientsByRecipeId(Long recipeId);
    List<RecipeIngredientDTO> getRecipesByIngredientId(Long ingredientId);
    void deleteRecipeIngredient(Long id);
}
