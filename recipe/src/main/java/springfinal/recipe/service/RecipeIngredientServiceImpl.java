package springfinal.recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfinal.recipe.dto.IngredientDTO;
import springfinal.recipe.dto.RecipeIngredientDTO;
import springfinal.recipe.mapper.IngredientMapper;
import springfinal.recipe.mapper.RecipeIngredientMapper;
import springfinal.recipe.model.Ingredient;
import springfinal.recipe.model.Recipe;
import springfinal.recipe.model.RecipeIngredient;
import springfinal.recipe.repository.RecipeIngredientRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecipeIngredientServiceImpl implements RecipeIngredientService{
    @Autowired
    private RecipeIngredientRepository repository;

    @Override
    public void saveRecipeIngredients(Long recipeId, List<RecipeIngredientDTO> recipeIngredientDTO) {
        //이미 저장된 RecipeIngredient를 가져옴
        List<RecipeIngredient> existingIngredients = repository.findByRecipeId(recipeId);

        //새로운 RecipeIngredient 리스트 생성 (중복 제거)
        List<RecipeIngredient> newIngredients = recipeIngredientDTO.stream()
                .filter(dto -> existingIngredients.stream().noneMatch(existing ->
                        existing.getIngredient().getId().equals(dto.getIngredientId()) &&
                                existing.getQty().equals(dto.getQty()) &&
                                existing.getUnit().equals(dto.getUnit())))
                .map(dto -> RecipeIngredient.builder()
                        .recipe(Recipe.builder().id(recipeId).build())
                        .ingredient(Ingredient.builder().id(dto.getIngredientId()).build())
                        .qty(dto.getQty()) // 양 저장
                        .unit(dto.getUnit()) // 단위 저장
                        .build())
                .toList();

        //중복되지 않은 RecipeIngredient만 저장
        repository.saveAll(newIngredients);
    }

    @Override
    public List<RecipeIngredientDTO> getIngredientsByRecipeId(Long recipeId) {
        return repository.findByRecipeId(recipeId).stream()
                .map(RecipeIngredientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeIngredientDTO> getRecipesByIngredientId(Long ingredientId) {
        return repository.findByIngredientId(ingredientId).stream()
                .map(RecipeIngredientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRecipeIngredient(Long id) {
        repository.deleteById(id);
    }
}
