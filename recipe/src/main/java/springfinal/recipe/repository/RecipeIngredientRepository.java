package springfinal.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springfinal.recipe.model.RecipeIngredient;

import java.util.List;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    List<RecipeIngredient> findByRecipeId(Long recipeId);
    List<RecipeIngredient> findByIngredientId(Long ingredientId);
}
