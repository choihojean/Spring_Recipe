package springfinal.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor @AllArgsConstructor
public class CommentDTO {
    private Long id; // 인덱스
    private String recipeId; // 레시피 인덱스
    private String userNickname; // 유저 닉네임
    private String content; // 내용
    private String parentComment; // 부모 댓글
    private Boolean is_deleted; // 삭제 여부

}
