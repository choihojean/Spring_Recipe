package springfinal.recipe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfinal.recipe.dto.CommentDTO;
import springfinal.recipe.service.CommentService;

import java.util.List;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/api/comments") // api/comments?recipeId={recipeId}
    public List<CommentDTO> readRecipeComments(@RequestParam("recipeId") Long recipeId) {
        return commentService.findByRecipeId(recipeId);
    }

    @PostMapping("/api/comments") // api/comments
    public String CreateRecipeComment(@RequestParam("recipeId") Long recipeId,
                                      @RequestParam("content") String content,
                                      @RequestParam(name = "parentCommentId", required = false) Long parentCommentId,
                                      Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "<script>window.location.href='/user/login';</script>"; // 로그인 필요
        }

        String userNickname = authentication.getName();

        commentService.saveComment(recipeId, content, parentCommentId, userNickname);

        return String.format("<script>window.location.href='/detail/%d';</script>", recipeId);
    }

    @DeleteMapping("/api/comments/{id}")
    public boolean deleteComment(@PathVariable("id") Long commentId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false; // 로그인 필요
        }
        return commentService.deleteComment(commentId, authentication.getName());
    }
}
