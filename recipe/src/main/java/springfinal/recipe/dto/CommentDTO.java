package springfinal.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfinal.recipe.model.Comment;

import java.util.List;

@Builder
@Data
@NoArgsConstructor @AllArgsConstructor
public class CommentDTO {
    private Long id; // 인덱스
    private String userNickname; // 유저 닉네임
    private String content; // 내용
    private Long recipeId; // 레시피 인덱스
    private Long parentCommentId; // 부모 댓글 인덱스
    private List<CommentDTO> childComments; // 자식 댓글들
    private Boolean is_deleted; // 삭제 여부

}
