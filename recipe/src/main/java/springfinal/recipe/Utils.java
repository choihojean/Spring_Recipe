package springfinal.recipe;

public class Utils {
    public static  RecipeDTO toDTO(Recipe recipe) {
        return RecipeDTO.builder()
                .id(recipe.getId())
                .recipeName(recipe.getRecipeName())
                .ingredients(recipe.getIngredients())
                .cookery(recipe.getCookery())
                .cookingTime(recipe.getCookingTime())
                .difficultyLevel(recipe.getDifficultyLevel())
                .build();
    }
    public static Recipe toEntity(RecipeDTO dto) {
        return Recipe.builder()
                .id(dto.getId())
                .recipeName(dto.getRecipeName())
                .ingredients(dto.getIngredients())
                .cookery(dto.getCookery())
                .cookingTime(dto.getCookingTime())
                .difficultyLevel(dto.getDifficultyLevel())
                .build();
    }
}
