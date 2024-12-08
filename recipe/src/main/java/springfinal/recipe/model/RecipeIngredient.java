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

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe; // 레시피 조인

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient; // 재료 조인

    private String qty; // 양
    private String unit; //단위

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
