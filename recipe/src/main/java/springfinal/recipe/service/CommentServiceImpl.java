package springfinal.recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfinal.recipe.dto.CommentDTO;
import springfinal.recipe.mapper.CommentMapper;
import springfinal.recipe.model.Comment;
import springfinal.recipe.model.Recipe;
import springfinal.recipe.repository.CommentRepository;
import springfinal.recipe.repository.RecipeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Override
    public List<CommentDTO> findByRecipeId(Long id) {
        return commentRepository.findAll().stream()
                .map(CommentMapper::toDTO)
                .filter(c -> c.getRecipeId().equals(id) && c.getParentCommentId() == 0)
                .collect(Collectors.toList());
    }

    @Override
    public void saveComment(Long recipeId, String content, Long parentCommentId, String userNickname) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("exception" + recipeId));

        Comment parentComment = parentCommentId != null ?
                commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new IllegalArgumentException("exception" + parentCommentId))
                :
                null;

        Comment comment = Comment.builder()
                .recipe(recipe)
                .content(content)
                .parentComment(parentComment)
                .userNickname(userNickname)
                .build();

        commentRepository.save(comment);
    }
}
