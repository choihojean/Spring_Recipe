package springfinal.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor @AllArgsConstructor
public class RecipeIngredientDTO {
    private Long id; // 인덱스
    private Long recipeId; // 레시피 인덱스
    private Long ingredientId; // 재료 인덱스
    private String qty; // 양
}
