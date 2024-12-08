package springfinal.recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfinal.recipe.mapper.RecipeMapper;
import springfinal.recipe.dto.RecipeDTO;
import springfinal.recipe.model.Recipe;
import springfinal.recipe.model.Recommend;
import springfinal.recipe.model.User;
import springfinal.recipe.repository.RecipeRepository;
import springfinal.recipe.repository.RecommendRepository;
import springfinal.recipe.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecommendRepository recommendRepository;

    @Override
    public List<RecipeDTO> findAll() {
        return recipeRepository.findAll().stream()
                .map(recipe -> {
                    RecipeDTO dto = RecipeMapper.toDTO(recipe);
                    dto.setRecommendCount((long) recipe.getRecommendations().size());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDTO findById(long id) {
        return recipeRepository.findById(id)
                .map(RecipeMapper::toDTO)
                .orElse(null);
    }

    @Override
    public List<RecipeDTO> findByUserNickname(String username) {
        User user = userRepository.findByNickname(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return recipeRepository.findByUserNickname(user).stream()
                .map(RecipeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> findByRecipeNameContaining(String name) {
        return recipeRepository.findByRecipeNameContaining(name).stream()
                .map(recipe -> {
                    RecipeDTO dto = RecipeMapper.toDTO(recipe);
                    dto.setRecommendCount((long) recipe.getRecommendations().size());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> findByIngredientsIngredientId(Long id) {
        return recipeRepository.findAll().stream()
                .filter(recipe -> recipe.getIngredients().stream()
                        .anyMatch(recipeIngredient -> recipeIngredient.getIngredient().getId().equals(id)))
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
                .img(recipe.getImg())
                .build();
        Recipe savedRecipe = recipeRepository.save(recipe);
        return savedRecipe.getId();
    }

    @Override
    public void updateById(Long id, RecipeDTO recipeDTO) {
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));

        existingRecipe = Recipe.builder()
                .id(existingRecipe.getId()) //기존 ID 유지
                .recipeName(recipeDTO.getRecipeName())
                .cookery(recipeDTO.getCookery())
                .cookingTime(recipeDTO.getCookingTime())
                .difficultyLevel(recipeDTO.getDifficultyLevel())
                .userNickname(existingRecipe.getUserNickname())
                .build();

        recipeRepository.save(existingRecipe);
    }

    @Override
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }

    //recommend
    @Override
    public void addRecommendation(Long recipeId, String username) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));
        User user = userRepository.findByNickname(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (recommendRepository.findByRecipeAndUser(recipe, user).isPresent()) {
            return; //이미 추천한 경우
        }

        Recommend recommend = Recommend.builder()
                .recipe(recipe)
                .user(user)
                .build();
        recommendRepository.save(recommend);
    }

    @Override
    public void removeRecommendation(Long recipeId, String username) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));
        User user = userRepository.findByNickname(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Recommend recommend = recommendRepository.findByRecipeAndUser(recipe, user)
                .orElseThrow(() -> new IllegalArgumentException("추천 내역이 없습니다."));
        recommendRepository.delete(recommend);
    }

    @Override
    public Long countRecommendations(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));
        return recommendRepository.countByRecipe(recipe);
    }

    @Override
    public boolean isUserRecommended(Long recipeId, String username) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));
        User user = userRepository.findByNickname(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return recommendRepository.findByRecipeAndUser(recipe, user).isPresent();
    }
}
