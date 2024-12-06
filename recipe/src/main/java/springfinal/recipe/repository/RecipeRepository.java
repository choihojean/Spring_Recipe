package springfinal.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springfinal.recipe.model.Recipe;
import springfinal.recipe.model.User;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByRecipeNameContaining(String name);
    List<Recipe> findByUserNickname(User user);
}
