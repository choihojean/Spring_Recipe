package springfinal.recipe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter @Builder
@Entity
@Table(name="comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 인덱스

    private String userNickname; // 유저 닉네임
    private String content; // 내용

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe; // 레시피 조인

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment; // 부모 댓글 조인

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>(); // 자식 댓글들

    private Boolean is_deleted; // 삭제 여부

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}
