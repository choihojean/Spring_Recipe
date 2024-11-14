package springfinal.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor @AllArgsConstructor
public class RecipeDTO {
    private Long id;
    private String recipeName;
    private String ingredients;
    private String cookery;
    private Integer cookingTime;
    private String difficultyLevel;

}
