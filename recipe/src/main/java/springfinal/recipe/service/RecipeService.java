package springfinal.recipe.service;

import springfinal.recipe.dto.RecipeDTO;

import java.util.List;

public interface RecipeService {
    List<RecipeDTO> findAll();
    RecipeDTO findById(long id);
    void updateById(Long id, RecipeDTO recipeDTO);

    List<RecipeDTO> findByUserNickname(String username);

    List<RecipeDTO> findByRecipeNameContaining(String name);

    Long save(RecipeDTO recipe, String username);
    void deleteById(Long id);

    void addRecommendation(Long recipeId, String username);
    void removeRecommendation(Long recipeId, String username);
    Long countRecommendations(Long recipeId);
    boolean isUserRecommended(Long recipeId, String username);
}
