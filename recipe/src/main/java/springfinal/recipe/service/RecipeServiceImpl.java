package springfinal.recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfinal.recipe.mapper.RecipeMapper;
import springfinal.recipe.dto.RecipeDTO;
import springfinal.recipe.model.Recipe;
import springfinal.recipe.model.User;
import springfinal.recipe.repository.RecipeIngredientRepository;
import springfinal.recipe.repository.RecipeRepository;
import springfinal.recipe.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<RecipeDTO> findAll() {
        return recipeRepository.findAll().stream()
                .map(RecipeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDTO findById(long id) {
        return recipeRepository.findById(id)
                .map(RecipeMapper::toDTO)
                .orElse(null);
    }

    @Override
    public List<RecipeDTO> findByRecipeNameContaining(String name) {
        return recipeRepository.findByRecipeNameContaining(name).stream()
                .map(RecipeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Long save(RecipeDTO recipeDTO, String username) {
        User user = userRepository.findByNickname(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Recipe recipe = RecipeMapper.toEntity(recipeDTO);
        recipe = Recipe.builder()
                .id(recipe.getId())
                .recipeName(recipe.getRecipeName())
                .cookery(recipe.getCookery())
                .cookingTime(recipe.getCookingTime())
                .difficultyLevel(recipe.getDifficultyLevel())
                .userNickname(user) // 작성자 설정
                .build();
        Recipe savedRecipe = recipeRepository.save(recipe);
        return savedRecipe.getId();
    }

    @Override
    public void updateById(Long id, RecipeDTO recipeDTO) {
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));

        // 기존 엔티티의 필드 업데이트
        existingRecipe = Recipe.builder()
                .id(existingRecipe.getId()) // 기존 ID 유지
                .recipeName(recipeDTO.getRecipeName())
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
