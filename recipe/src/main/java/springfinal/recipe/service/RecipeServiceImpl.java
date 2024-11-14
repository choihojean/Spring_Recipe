package springfinal.recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfinal.recipe.Utils;
import springfinal.recipe.dto.RecipeDTO;
import springfinal.recipe.model.Recipe;
import springfinal.recipe.repository.RecipeRepository;
import springfinal.recipe.service.RecipeService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    @Override
    public List<RecipeDTO> findAll() {
        return recipeRepository.findAll().stream()
                .map(Utils::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDTO findById(long id) {
        return recipeRepository.findById(id)
                .map(Utils::toDTO)
                .orElse(null);
    }

    @Override
    public List<RecipeDTO> findByRecipeNameContaining(String name) {
        return recipeRepository.findByRecipeNameContaining(name).stream()
                .map(Utils::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void save(RecipeDTO recipeDTO) {
        Recipe recipe = Utils.toEntity(recipeDTO);
        recipeRepository.save(recipe);
    }

    @Override
    public void updateById(Long id, RecipeDTO recipeDTO) {
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));

        // 기존 엔티티의 필드 업데이트
        existingRecipe = Recipe.builder()
                .id(existingRecipe.getId()) // 기존 ID 유지
                .recipeName(recipeDTO.getRecipeName())
                .ingredients(recipeDTO.getIngredients())
                .cookery(recipeDTO.getCookery())
                .cookingTime(recipeDTO.getCookingTime())
                .difficultyLevel(recipeDTO.getDifficultyLevel())
                .build();

        recipeRepository.save(existingRecipe);
    }

    @Override
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }
}
