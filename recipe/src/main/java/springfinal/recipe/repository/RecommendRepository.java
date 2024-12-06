package springfinal.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springfinal.recipe.model.Recommend;
import springfinal.recipe.model.Recipe;
import springfinal.recipe.model.User;

import java.util.Optional;

@Repository
public interface RecommendRepository extends JpaRepository<Recommend, Long> {
    Optional<Recommend> findByRecipeAndUser(Recipe recipe, User user);
    long countByRecipe(Recipe recipe); //레시피별 추천 수
}
