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
    private UserDTO userNickname; //레시피 작성자
    private String recipeName; // 레시피명
    private String cookery; // 조리법
    private Integer cookingTime; // 요리 시간
    private Integer difficultyLevel; // 난이도
    private String img; // 이미지 URL
    private List<CommentDTO> comments; // 댓글들
    private Boolean is_deleted; // 삭제 여부
}
