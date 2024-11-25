package springfinal.recipe.mapper;

import springfinal.recipe.dto.IngredientDTO;
import springfinal.recipe.model.Ingredient;

public class IngredientMapper {
    public static IngredientDTO toDTO(Ingredient ingredient) {
        return IngredientDTO.builder()
                .id(ingredient.getId())
                .ingredientName(ingredient.getIngredientName())
                .build();
    }

    public static Ingredient toEntity(IngredientDTO dto) {
        return Ingredient.builder()
                .id(dto.getId())
                .ingredientName(dto.getIngredientName())
                .build();
    }
}
