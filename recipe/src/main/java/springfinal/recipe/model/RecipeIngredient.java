package springfinal.recipe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Getter @Builder
@Entity
@Table(name="recipe_ingredient")
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 인덱스

    private String recipeId; // 레시피 인덱스
    private String ingredientId; // 재료 인덱스
    private String qty; // 양
}
