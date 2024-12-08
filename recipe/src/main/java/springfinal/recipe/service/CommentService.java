package springfinal.recipe.service;

import org.springframework.stereotype.Service;
import springfinal.recipe.dto.CommentDTO;
import springfinal.recipe.dto.UserDTO;

import java.util.List;

@Service
public interface CommentService {
    List<CommentDTO> findByRecipeId(Long id);

    void saveComment(Long recipeId, String content, Long parentCommentId, String userNickname);

    void updateCommentByUser(String nickname, UserDTO userDTO);
}
