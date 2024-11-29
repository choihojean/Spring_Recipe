package springfinal.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor @AllArgsConstructor
public class IngredientDTO {
    private Long id; // 인덱스
    private String ingredientName; // 재료명
    private List<RecipeIngredientDTO> recipeIngredients = new ArrayList<>(); // 레시피 재료
}
