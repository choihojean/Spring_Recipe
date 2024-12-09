package springfinal.recipe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springfinal.recipe.dto.RecipeDTO;
import springfinal.recipe.model.Recipe;
import springfinal.recipe.service.IngredientService;
import springfinal.recipe.service.RecipeService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class RecipeApplicationTests {

	@Test
	void contextLoads() {
	}
	@Autowired
	private RecipeService recipeService; // RecipeService 주입

	@Test
	public void testEditRecipeForm() {

		RecipeDTO recipe = recipeService.findById(16L); // 존재하는 레시피 ID 사용
		assertNotNull(recipe.getIngredients());
		assertFalse(recipe.getIngredients().isEmpty());
	}

}
