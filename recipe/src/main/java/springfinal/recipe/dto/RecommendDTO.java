package springfinal.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor @AllArgsConstructor
public class RecommendDTO {
    private Long id; // 추천 ID
    private UserDTO user; // 추천한 사용자
    private Long recipeId; // 추천된 레시피 ID
}
