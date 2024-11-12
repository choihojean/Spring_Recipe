package springfinal.recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    List<RecipeDTO> findAll();
    RecipeDTO findById(long id);
    void updateById(Long id, RecipeDTO recipeDTO);
    void save(RecipeDTO recipe);
    void deleteById(Long id);
}
