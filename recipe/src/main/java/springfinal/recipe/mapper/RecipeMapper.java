package springfinal.recipe.mapper;

import springfinal.recipe.dto.RecipeDTO;
import springfinal.recipe.dto.RecommendDTO;
import springfinal.recipe.dto.UserDTO;
import springfinal.recipe.model.Recipe;
import springfinal.recipe.model.Recommend;
import springfinal.recipe.model.User;

import java.util.stream.Collectors;

public class RecipeMapper {
    public static RecipeDTO toDTO(Recipe recipe) {
        return RecipeDTO.builder()
                .id(recipe.getId())
                .userNickname(toDTO(recipe.getUserNickname()))
                .recipeName(recipe.getRecipeName())
                .cookery(recipe.getCookery())
                .cookingTime(recipe.getCookingTime())
                .difficultyLevel(recipe.getDifficultyLevel())
                .img(recipe.getImg())
                .is_deleted(recipe.getIs_deleted())
                .build();
    }
    public static Recipe toEntity(RecipeDTO dto) {
        return Recipe.builder()
                .id(dto.getId())
                .userNickname(User.builder()
                        .id(dto.getUserNickname().getId())
                        .nickname(dto.getUserNickname().getNickname())
                        .build())
                .recipeName(dto.getRecipeName())
                .cookery(dto.getCookery())
                .cookingTime(dto.getCookingTime())
                .difficultyLevel(dto.getDifficultyLevel())
                .img(dto.getImg())
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

    public static RecommendDTO toDTO(Recommend recommend) {
        return RecommendDTO.builder()
                .id(recommend.getId())
                .user(toDTO(recommend.getUser()))
                .recipeId(recommend.getRecipe().getId())
                .build();
    }

    public static Recommend toEntity(RecommendDTO dto) {
        return Recommend.builder()
                .id(dto.getId())
                .user(toEntity(dto.getUser()))
                .build();
    }
}
