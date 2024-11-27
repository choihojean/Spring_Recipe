package springfinal.recipe.service;

import springfinal.recipe.dto.IngredientDTO;

import java.util.List;

public interface IngredientService {
    List<IngredientDTO> findAll();
    IngredientDTO findById(Long id);
    List<IngredientDTO> findByIngredientNameContaining(String name);
    boolean addIngredient(IngredientDTO ingredientDTO); //재료 추가시?
    void updateIngredient(Long id, IngredientDTO ingredientDTO); //재료 수정
//    void deleteIngredient(Long id);
}
