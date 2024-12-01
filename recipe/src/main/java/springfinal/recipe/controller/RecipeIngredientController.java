package springfinal.recipe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfinal.recipe.dto.RecipeIngredientDTO;
import springfinal.recipe.model.RecipeIngredient;
import springfinal.recipe.service.RecipeIngredientService;

import java.util.List;

@RestController
@RequestMapping("/api/recipe-ingredient")
public class RecipeIngredientController {
    @Autowired
    private RecipeIngredientService service;

    @GetMapping("/recipe/{recipeId}")
    public List<RecipeIngredientDTO> getIngredientsByRecipeId(@PathVariable("recipeId") Long recipeId) {
        return service.getIngredientsByRecipeId(recipeId);
    }

    @GetMapping("/ingredient/{ingredientId}")
    public List<RecipeIngredientDTO> getRecipesByIngredientId(@PathVariable("ingredientId") Long ingredientId) {
        return service.getRecipesByIngredientId(ingredientId);
    }

    @DeleteMapping("/{id}")
    public void deleteRecipeIngredient(@PathVariable("id") Long id) {
        service.deleteRecipeIngredient(id);
    }
}
