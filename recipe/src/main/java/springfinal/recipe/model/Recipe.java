package springfinal.recipe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter @Builder
@Entity
@Table(name="recipe")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 인덱스

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userNickname; //레시피 작성자

    private String recipeName; // 레시피명
    private String cookery; // 조리법
    private Integer cookingTime; // 요리 시간
    private Integer difficultyLevel; // 난이도
    private String img; // 이미지 URL

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommend> recommendations = new ArrayList<>(); // 추천

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>(); // 댓글들

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> ingredients = new ArrayList<>(); //레시피-재료 관계

    private Boolean is_deleted; // 삭제 여부

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setCookery(String cookery) {
        this.cookery = cookery;
    }

    public void setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
    }

    public void setDifficultyLevel(Integer difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
