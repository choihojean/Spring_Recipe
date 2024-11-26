package springfinal.recipe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfinal.recipe.dto.CommentDTO;
import springfinal.recipe.service.CommentService;

import java.util.List;
import java.util.Optional;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/api/comment/{id}")
    public List<CommentDTO> readRecipeComment(@PathVariable("id") Long recipeId) {
        return commentService.findByRecipeId(recipeId);
    }
}
