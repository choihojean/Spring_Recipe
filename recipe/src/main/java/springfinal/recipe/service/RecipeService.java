package springfinal.recipe.service;

import springfinal.recipe.dto.RecipeDTO;

import java.util.List;

public interface RecipeService {
    List<RecipeDTO> findAll();
    RecipeDTO findById(long id);
    void updateById(Long id, RecipeDTO recipeDTO);

    List<RecipeDTO> findByRecipeNameContaining(String name);

    Long save(RecipeDTO recipe, String username);
    void deleteById(Long id);
}
