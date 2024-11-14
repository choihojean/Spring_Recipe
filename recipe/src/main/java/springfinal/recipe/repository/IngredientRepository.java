package springfinal.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springfinal.recipe.model.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
