package springfinal.recipe.mapper;

import springfinal.recipe.dto.RecipeDTO;
import springfinal.recipe.model.Recipe;

public class RecipeMapper {
    public static RecipeDTO toDTO(Recipe recipe) {
        return RecipeDTO.builder()
                .id(recipe.getId())
                .recipeName(recipe.getRecipeName())
                .cookery(recipe.getCookery())
                .cookingTime(recipe.getCookingTime())
                .difficultyLevel(recipe.getDifficultyLevel())
                .img(recipe.getImg())
                .recommend(recipe.getRecommend())
                .is_deleted(recipe.getIs_deleted())
                .build();
    }
    public static Recipe toEntity(RecipeDTO dto) {
        return Recipe.builder()
                .id(dto.getId())
                .recipeName(dto.getRecipeName())
                .cookery(dto.getCookery())
                .cookingTime(dto.getCookingTime())
                .difficultyLevel(dto.getDifficultyLevel())
                .img(dto.getImg())
                .recommend(dto.getRecommend())
                .is_deleted(dto.getIs_deleted())
                .build();
    }
}
