package springfinal.recipe.service;

import springfinal.recipe.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> findByRecipeId(long Id);
}