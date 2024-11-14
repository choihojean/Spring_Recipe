package springfinal.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor @AllArgsConstructor
public class UserDTO {
    private Long id; // 인덱스
    private String nickname; // 닉네임
    private String password; // 비밀번호
    private Boolean is_deleted; // 삭제 여부
}
