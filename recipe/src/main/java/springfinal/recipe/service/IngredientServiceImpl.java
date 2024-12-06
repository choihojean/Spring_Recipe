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
                .orElseThrow(() -> new IllegalArgumentException("재료를 찾을 수 없음"));
    }

    public List<IngredientDTO> findByIngredientNameContaining(String name) {
        return ingredientRepository.findByIngredientNameContaining(name).stream()
                .map(IngredientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean addIngredient(IngredientDTO ingredientDTO) {
        if (ingredientRepository.existsByIngredientName(ingredientDTO.getIngredientName())) {
            throw new IllegalArgumentException("중복된 재료명");
        }
        Ingredient ingredient = IngredientMapper.toEntity(ingredientDTO);
        ingredientRepository.save(ingredient);
        return true;
    }

    @Override
    public void updateIngredient(Long id, IngredientDTO ingredientDTO) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("재료를 찾을 수 없음"));

        if (ingredientRepository.existsByIngredientName(ingredientDTO.getIngredientName()) &&
                !ingredient.getIngredientName().equals(ingredientDTO.getIngredientName())) {
            throw new IllegalArgumentException("중복된 재료명입니다.");
        }

        ingredient = Ingredient.builder()
                .id(ingredient.getId())
                .ingredientName(ingredientDTO.getIngredientName())
                .build();

        ingredientRepository.save(ingredient);
    }

//    @Override
//    public void deleteIngredient(Long id) {
//        ingredientRepository.deleteById(id);
//    }
}
