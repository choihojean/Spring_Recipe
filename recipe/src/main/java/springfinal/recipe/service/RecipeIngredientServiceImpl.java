package springfinal.recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfinal.recipe.dto.RecipeIngredientDTO;
import springfinal.recipe.mapper.RecipeIngredientMapper;
import springfinal.recipe.model.RecipeIngredient;
import springfinal.recipe.repository.RecipeIngredientRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeIngredientServiceImpl implements RecipeIngredientService{
    @Autowired
    private RecipeIngredientRepository repository;

    @Override
    public void saveRecipeIngredient(RecipeIngredientDTO dto) {
        RecipeIngredient entity = RecipeIngredientMapper.toEntity(dto);
        repository.save(entity);
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
