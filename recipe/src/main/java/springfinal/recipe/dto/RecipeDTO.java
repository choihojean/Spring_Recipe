package springfinal.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor @AllArgsConstructor
public class RecipeDTO {
    private Long id; // 인덱스
    private String recipeName; // 레시피명
    private String cookery; // 조리법
    private Integer cookingTime; // 요리 시간
    private Integer difficultyLevel; // 난이도
    private List<Integer> recommend; // 추천
    private Boolean is_deleted; // 삭제 여부
}
