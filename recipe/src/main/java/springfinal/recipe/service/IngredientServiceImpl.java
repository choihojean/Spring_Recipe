package springfinal.recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfinal.recipe.dto.IngredientDTO;
import springfinal.recipe.mapper.IngredientMapper;
import springfinal.recipe.model.Ingredient;
import springfinal.recipe.repository.IngredientRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public List<IngredientDTO> findAll() {
        return ingredientRepository.findAll().stream()
                .map(IngredientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public IngredientDTO findById(Long id) {
        return ingredientRepository.findById(id)
                .map(IngredientMapper::toDTO)
                .orElse(null);
    }

    @Override
    public boolean addIngredient(IngredientDTO ingredientDTO) {
        if (ingredientRepository.findByIngredientName(ingredientDTO.getIngredientName()).isPresent()) {
            return false; //중복된 재료 이름 안되게
        }
        Ingredient ingredient = IngredientMapper.toEntity(ingredientDTO);
        ingredientRepository.save(ingredient);
        return true;
    }

    @Override
    public void updateIngredient(Long id, IngredientDTO ingredientDTO) {
        Ingredient existingIngredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("재료를 찾을 수 없음"));

        existingIngredient = Ingredient.builder()
                .id(existingIngredient.getId())
                .ingredientName(ingredientDTO.getIngredientName())
                .build();

        ingredientRepository.save(existingIngredient);
    }

    @Override
    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }
}
