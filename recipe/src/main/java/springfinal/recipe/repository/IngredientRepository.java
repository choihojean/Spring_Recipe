package springfinal.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springfinal.recipe.model.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByIngredientNameContaining(String ingredientName);
    boolean existsByIngredientName(String ingredientName);
}
