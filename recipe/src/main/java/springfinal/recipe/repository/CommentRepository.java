package springfinal.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springfinal.recipe.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
