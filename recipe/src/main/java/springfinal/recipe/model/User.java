package springfinal.recipe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Getter @Builder
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 인덱스

    private String nickname; // 닉네임
    private String password; // 비밀번호
    private Boolean is_deleted; // 삭제 여부
}
