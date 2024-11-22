package springfinal.recipe;

import springfinal.recipe.dto.RecipeDTO;
import springfinal.recipe.dto.UserDTO;
import springfinal.recipe.model.Recipe;
import springfinal.recipe.model.User;

public class Utils {
    public static RecipeDTO toDTO(Recipe recipe) {
        return RecipeDTO.builder()
                .id(recipe.getId())
                .recipeName(recipe.getRecipeName())
                .cookery(recipe.getCookery())
                .cookingTime(recipe.getCookingTime())
                .difficultyLevel(recipe.getDifficultyLevel())
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
                .recommend(dto.getRecommend())
                .is_deleted(dto.getIs_deleted())
                .build();
    }

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .password(user.getPassword())
                .is_deleted(user.getIs_deleted())
                .build();
    }

    public static User toEntity(UserDTO dto) {
        return User.builder()
                .id(dto.getId())
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .is_deleted(dto.getIs_deleted())
                .build();
    }
}
