package springfinal.recipe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfinal.recipe.dto.RecipeDTO;
import springfinal.recipe.service.RecipeService;
import springfinal.recipe.model.Recipe;

@Controller
@RequestMapping
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

//main에서 사용하므로 필요하지 않을 듯
//    @GetMapping
//    public String listRecipes(Model model) {
//        model.addAttribute("recipe", recipeService.findAll());
//        return "recipe";
//    }

    @GetMapping("/search")
    public String searchRecipes(@RequestParam("name") String name, Model model) {
        model.addAttribute("recipe", recipeService.findByRecipeNameContaining(name));
        model.addAttribute("isSearch", true);
        return "recipe";
    }

    @GetMapping("/new")
    public String newRecipeForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "recipe-form";
    }

    @GetMapping("/detail/{id}")
    public String recipeDetail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("recipe", recipeService.findById(id));
        return "recipe-detail";
    }

    @PostMapping
    public String saveRecipe(@ModelAttribute RecipeDTO recipe) {
        recipeService.save(recipe);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteRecipe(@PathVariable("id") Long id) {
        recipeService.deleteById(id);
        return "redirect:/";
    }

    // 수정 페이지로 이동
    @GetMapping("/edit/{id}")
    public String editRecipeForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("recipe", recipeService.findById(id));
        return "recipe-edit";
    }

    // 수정된 내용을 저장
    @PostMapping("/update/{id}")
    public String updateRecipe(@PathVariable("id") Long id, @ModelAttribute RecipeDTO recipeDTO) {
        recipeService.updateById(id, recipeDTO);
        return "redirect:/detail/" + id;
    }
}
