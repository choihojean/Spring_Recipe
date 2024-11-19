package springfinal.recipe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter @Builder
@Entity
@Table(name="comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 인덱스

    @ManyToOne
    @JoinColumn(name="recipe_id")
    private Recipe recipe; // 레시피 인덱스

    private String userNickname; // 유저 닉네임
    private String content; // 내용
    private Integer parentCommentId; // 부모 댓글
    private List<Integer> childCommentsId; //자식 댓글
    private Boolean is_deleted; // 삭제 여부
}
