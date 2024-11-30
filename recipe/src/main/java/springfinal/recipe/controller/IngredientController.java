package springfinal.recipe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfinal.recipe.dto.IngredientDTO;
import springfinal.recipe.dto.RecipeDTO;
import springfinal.recipe.service.IngredientService;

import java.util.List;

@RestController
@RequestMapping("/api/ingredient")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @GetMapping
    public ResponseEntity<List<IngredientDTO>> getAllIngredients() {
        return ResponseEntity.ok(ingredientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDTO> getIngredientById(@PathVariable("id") Long id) {
        IngredientDTO ingredient = ingredientService.findById(id);
        if (ingredient == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ingredient);
    }

    @PostMapping
    public ResponseEntity<String> addIngredient(@RequestBody IngredientDTO ingredientDTO) {
        boolean added = ingredientService.addIngredient(ingredientDTO);
        if (!added) {
            return ResponseEntity.badRequest().body("중복된 재료명입니다.");
        }
        return ResponseEntity.ok("재료가 추가되었습니다.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateIngredient(@PathVariable("id") Long id, @RequestBody IngredientDTO ingredientDTO) {
        try {
            ingredientService.updateIngredient(id, ingredientDTO);
            return ResponseEntity.ok("재료가 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public List<IngredientDTO> searchRecipes(@RequestParam("name") String name) {
        return ingredientService.findByIngredientNameContaining(name);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteIngredient(@PathVariable Long id) {
//        ingredientService.deleteIngredient(id);
//        return ResponseEntity.ok("재료가 삭제되었습니다.");
//    }
}
