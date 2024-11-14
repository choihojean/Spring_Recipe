package springfinal.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springfinal.recipe.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
