package springfinal.recipe.mapper;

import springfinal.recipe.dto.CommentDTO;
import springfinal.recipe.model.Comment;
import springfinal.recipe.model.Recipe;

import java.util.stream.Collectors;

public class CommentMapper {
    public static CommentDTO toDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .userNickname(comment.getUserNickname())
                .content(comment.getContent())
                .recipeId(comment.getRecipe().getId())
                .parentCommentId(comment.getParentComment().getId())
                .childComments(comment.getChildComments().stream()
                        .map(CommentMapper::toDTO)
                        .collect(Collectors.toList())
                )
                .is_deleted(comment.getIs_deleted())
                .build();
    }
    public static Comment toEntity(CommentDTO dto, Recipe recipe, Comment parentComment) {
        return Comment.builder()
                .id(dto.getId())
                .userNickname(dto.getUserNickname())
                .content(dto.getContent())
                .recipe(recipe)
                .parentComment(parentComment)
                .childComments(dto.getChildComments().stream()
                        .map(cDto -> toEntity(cDto, null, null))
                        .collect(Collectors.toList())
                )
                .is_deleted(dto.getIs_deleted())
                .build();
    }
}
