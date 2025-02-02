package springfinal.recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfinal.recipe.dto.CommentDTO;
import springfinal.recipe.dto.RecipeIngredientDTO;
import springfinal.recipe.mapper.RecipeMapper;
import springfinal.recipe.dto.RecipeDTO;
import springfinal.recipe.model.*;
import springfinal.recipe.repository.RecipeRepository;
import springfinal.recipe.repository.RecommendRepository;
import springfinal.recipe.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
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
                .filter(recipe -> !recipe.getIs_deleted()) //삭제되지 않은 레시피만 필터링
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
                .filter(recipe -> !recipe.getIs_deleted())
                .map(RecipeMapper::toDTO)
                .orElse(null);
    }

    @Override
    public List<RecipeDTO> findByUserNickname(String username) {
        User user = userRepository.findByNickname(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return recipeRepository.findByUserNickname(user).stream()
                .filter(recipe -> !recipe.getIs_deleted())
                .map(RecipeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> findByRecipeNameContaining(String name) {
        return recipeRepository.findByRecipeNameContaining(name).stream()
                .filter(recipe -> !recipe.getIs_deleted())
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
                .filter(recipe -> !recipe.getIs_deleted())
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
                .is_deleted(false)
                .build();
        Recipe savedRecipe = recipeRepository.save(recipe);
        return savedRecipe.getId();
    }

    @Override
    public void updateById(Long id, RecipeDTO recipeDTO) {
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));

        // 레시피 기본 정보 업데이트
        existingRecipe.setRecipeName(recipeDTO.getRecipeName());
        existingRecipe.setCookery(recipeDTO.getCookery());
        existingRecipe.setCookingTime(recipeDTO.getCookingTime());
        existingRecipe.setDifficultyLevel(recipeDTO.getDifficultyLevel());

        // 이미지 업데이트 (새로운 이미지가 제공된 경우)
        if (recipeDTO.getImg() != null) {
            existingRecipe.setImg(recipeDTO.getImg());
        }

        // 기존 재료 초기화 및 업데이트
        List<RecipeIngredient> existingIngredients = existingRecipe.getIngredients();
        existingIngredients.clear();

        // 새로운 재료 추가
        for (RecipeIngredientDTO ingredientDTO : recipeDTO.getIngredients()) {
            RecipeIngredient ingredient = RecipeIngredient.builder()
                    .recipe(existingRecipe) // 역참조 설정
                    .ingredient(Ingredient.builder().id(ingredientDTO.getIngredientId()).build())
                    .qty(ingredientDTO.getQty())
                    .unit(ingredientDTO.getUnit())
                    .build();
            existingIngredients.add(ingredient);
        }

        // 업데이트된 레시피 저장
        recipeRepository.save(existingRecipe);
    }

    public void deleteById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));
        recipe = Recipe.builder()
                .id(recipe.getId())
                .userNickname(recipe.getUserNickname())
                .recipeName(recipe.getRecipeName())
                .cookery(recipe.getCookery())
                .cookingTime(recipe.getCookingTime())
                .difficultyLevel(recipe.getDifficultyLevel())
                .img(recipe.getImg())
                .ingredients(recipe.getIngredients())
                .recommendations(recipe.getRecommendations())
                .comments(recipe.getComments())
                .is_deleted(true) // 삭제 상태 설정
                .build();

        recipeRepository.save(recipe); // 업데이트된 상태로 저장
    }

    //recommend
    @Override
    public void addRecommendation(Long recipeId, String username) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));
        User user = userRepository.findByNickname(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (recipe.getUserNickname().getNickname().equals(username)) {
            throw new IllegalArgumentException("자신의 레시피는 추천할 수 없습니다.");
        }
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
