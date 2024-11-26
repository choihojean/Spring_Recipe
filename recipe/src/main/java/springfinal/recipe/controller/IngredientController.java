package springfinal.recipe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfinal.recipe.dto.IngredientDTO;
import springfinal.recipe.service.IngredientService;

@Controller
@RequestMapping("/ingredient")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @GetMapping
    public String listIngredient(Model model) {
        model.addAttribute("ingredients", ingredientService.findAll());
        return "ingredient-list";
    }

    @GetMapping("/new")
    public String newIngredientForm(Model model) {
        model.addAttribute("ingredient", new IngredientDTO());
        return "ingredient-form";
    }

    @PostMapping("/new")
    public String addIngredient(@ModelAttribute("ingredient") IngredientDTO ingredientDTO, Model model) {
        if (!ingredientService.addIngredient(ingredientDTO)) {
            model.addAttribute("error", "이미 추가된 재료");
            return "ingredient-form";
        }
        return "redirect:/ingredient";
    }

    @GetMapping("/edit/{id}")
    public String editIngredientForm(@PathVariable("id") Long id, Model model) {
        IngredientDTO ingredient = ingredientService.findById(id);
        model.addAttribute("ingredient", ingredient);
        return "ingredient-form";
    }

    @PostMapping("/edit/{id}")
    public String updateIngredient(@PathVariable("id") Long id, @ModelAttribute("ingredient") IngredientDTO ingredientDTO) {
        ingredientService.updateIngredient(id, ingredientDTO);
        return "redirect:/ingredient";
    }

    @PostMapping("/delete/{id}")
    public String deleteIngredient(@PathVariable("id") Long id) {
        ingredientService.deleteIngredient(id);
        return "redirect:/ingredient";
    }
}
