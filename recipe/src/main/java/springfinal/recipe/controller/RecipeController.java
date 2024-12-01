package springfinal.recipe.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfinal.recipe.dto.IngredientDTO;
import springfinal.recipe.dto.RecipeDTO;
import springfinal.recipe.service.IngredientService;
import springfinal.recipe.service.RecipeIngredientService;
import springfinal.recipe.service.RecipeService;
import springfinal.recipe.model.Recipe;

import java.util.List;

@Controller
@RequestMapping
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private RecipeIngredientService recipeIngredientService;

//main에서 사용하므로 필요하지 않을 듯
//    @GetMapping
//    public String listRecipes(Model model) {
//        model.addAttribute("recipe", recipeService.findAll());
//        return "recipe";
//    }

    @GetMapping("/search")
    public String searchRecipes(@RequestParam("type") String type, @RequestParam("name") String name, Model model) {
        if ("recipe".equals(type)) {
            List<RecipeDTO> recipes = recipeService.findByRecipeNameContaining(name);
            model.addAttribute("results", recipes);
        } else if ("ingredient".equals(type)) {
            List<IngredientDTO> ingredients = ingredientService.findByIngredientNameContaining(name);
            model.addAttribute("results", ingredients);
        }
        model.addAttribute("type", type);
        model.addAttribute("isSearch", true);
        return "recipe";
    }

    @GetMapping("/new")
    public String newRecipeForm(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/user/login";
        }

        model.addAttribute("recipe", new Recipe());
        return "recipe-form";
    }

    @GetMapping("/detail/{id}")
    public String recipeDetail(@PathVariable("id") Long id, Model model, Authentication authentication) {
        RecipeDTO recipe = recipeService.findById(id);
        model.addAttribute("recipe", recipe);

        if (authentication != null && authentication.isAuthenticated()) {
            String currentUsername = authentication.getName();
            boolean isUserNickname = recipe.getUserNickname().equals(currentUsername);
            model.addAttribute("isUserNickname", isUserNickname); //작성자인지 여부 전달
        } else {
            model.addAttribute("isUserNickname", false);
        }

        return "recipe-detail";
    }

    @PostMapping
    public String saveRecipe(@ModelAttribute RecipeDTO recipeDTO, @RequestParam("ingredients") String ingredientsStr, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/user/login"; // 로그인 필요
        }
        String username = authentication.getName();
        Long recipeId = recipeService.save(recipeDTO, username);
        Gson gson = new Gson();
        List<IngredientDTO> ingredients = List.of(gson.fromJson(ingredientsStr, IngredientDTO[].class));
        recipeIngredientService.saveRecipeIngredients(recipeId, ingredients);
        return "redirect:/main";
    }

    @GetMapping("/delete/{id}")
    public String deleteRecipe(@PathVariable("id") Long id) {
        recipeService.deleteById(id);
        return "redirect:/main";
    }

    // 수정 페이지로 이동
    @GetMapping("/edit/{id}")
    public String editRecipeForm(@PathVariable("id") Long id, Authentication authentication, Model model) {
        RecipeDTO recipe = recipeService.findById(id);

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/user/login"; //로그인되지 않은 경우 로그인 페이지로
        }

        String currentUsername = authentication.getName();
        if (!recipe.getUserNickname().equals(currentUsername)) {
            return "redirect:/recipe/detail/" + id; //작성자가 아닌 경우 상세 페이지로
        }

        model.addAttribute("recipe", recipe);
        return "recipe-edit";
    }

    // 수정된 내용을 저장
    @PostMapping("/update/{id}")
    public String updateRecipe(@PathVariable("id") Long id, @ModelAttribute RecipeDTO recipeDTO, Authentication authentication) {
        RecipeDTO existingRecipe = recipeService.findById(id);

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/user/login"; //로그인되지 않은 경우
        }

        String currentUsername = authentication.getName();
        if (!existingRecipe.getUserNickname().equals(currentUsername)) {
            return "redirect:/recipe/detail/" + id; //작성자가 아닌 경우
        }

        recipeService.updateById(id, recipeDTO);
        return "redirect:/recipe/detail/" + id; //수정 완료 후 상세 페이지로
    }
}
