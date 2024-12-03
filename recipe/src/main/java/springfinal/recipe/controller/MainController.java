package springfinal.recipe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfinal.recipe.service.RecipeService;


@Controller
@RequestMapping
public class MainController {
    @Autowired
    private RecipeService recipeService;

    @GetMapping("/main") // 메인 페이지
    public String mainPage(Model model) {
        model.addAttribute("results", recipeService.findAll());
        model.addAttribute("type", "recipe");
        return "recipe";
    }
}
