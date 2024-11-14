package springfinal.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springfinal.recipe.model.Recipe;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByRecipeNameContaining(String name);
}
