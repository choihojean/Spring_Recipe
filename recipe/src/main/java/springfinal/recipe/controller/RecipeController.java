package springfinal.recipe.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfinal.recipe.dto.IngredientDTO;
import springfinal.recipe.dto.RecipeDTO;
import springfinal.recipe.dto.RecipeIngredientDTO;
import springfinal.recipe.dto.UserDTO;
import springfinal.recipe.service.*;
import springfinal.recipe.model.Recipe;

import java.io.IOException;
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

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

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

    @GetMapping("/user-recipes/{username}")
    public String getUserRecipes(@PathVariable("username") String username, Model model) {
        List<RecipeDTO> recipes = recipeService.findByUserNickname(username);
        model.addAttribute("recipes", recipes);
        model.addAttribute("username", username); //템플릿에서 작성자 이름 표시
        return "user-recipes";
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

        if (recipe == null || recipe.getIs_deleted()) { // 삭제된 레시피 처리
            return "redirect:/main"; // 메인 페이지로 리다이렉트
        }

        model.addAttribute("recipe", recipe);

        if (authentication != null && authentication.isAuthenticated()) {
            String currentUsername = authentication.getName();
            boolean isUserNickname = recipe.getUserNickname().getNickname().equals(currentUsername);
            model.addAttribute("isUserNickname", isUserNickname); //작성자인지 여부 전달

            boolean isRecommended = recipeService.isUserRecommended(id, currentUsername);
            model.addAttribute("isRecommended", isRecommended); //사용자가 추천했는지 여부 전달
        } else {
            model.addAttribute("isUserNickname", false);
            model.addAttribute("isRecommended", false);
        }

        Long recommendCount = recipeService.countRecommendations(id);
        model.addAttribute("recommendCount", recommendCount);

        return "recipe-detail";
    }

    @PostMapping(value = "/", consumes = "multipart/form-data")
    public String saveRecipe(@ModelAttribute RecipeDTO recipeDTO,
                             @RequestParam("image") MultipartFile image,
                             @RequestParam("ingredientsStr") String ingredientsStr,
                             Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/user/login"; // 로그인 필요
        }

        //현재 로그인된 사용자 이름 가져오기
        String username = authentication.getName();

        //username을 통해 UserDTO 생성
        UserDTO userDTO = userService.findByNickname(username); // UserService를 통해 UserDTO 조회
        if (userDTO == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username);
        }

        // recipeDTO에 UserDTO 설정
        recipeDTO.setUserNickname(userDTO);

        // 이미지 s3에 저장
        try {
            String imgUrl = imageService.saveImageToS3(image);
            recipeDTO.setImg(imgUrl);
        } catch (Exception e) {
            System.out.println(e);
        }

        // 레시피 저장
        Long recipeId = recipeService.save(recipeDTO, username);

        // 재료 저장
        Gson gson = new Gson();
        List<RecipeIngredientDTO> recipeIngredients = List.of(gson.fromJson(ingredientsStr, RecipeIngredientDTO[].class));

        recipeIngredients.forEach(ri -> ri.setRecipeId(recipeId)); //각 RecipeIngredientDTO에 recipeId 설정

        recipeIngredientService.saveRecipeIngredients(recipeId, recipeIngredients);

        return "redirect:/main";
    }

    @PostMapping("/delete/{id}")
    public String deleteRecipe(@PathVariable("id") Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/user/login"; // 로그인하지 않은 사용자는 로그인 페이지로 리다이렉트
        }

        String username = authentication.getName();
        RecipeDTO recipe = recipeService.findById(id);

        if (recipe == null || !recipe.getUserNickname().getNickname().equals(username)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        recipeService.deleteById(id); // 개념적 삭제 처리
        return "redirect:/main"; // 삭제 후 메인 페이지로 리다이렉트
    }

    // 수정 페이지로 이동
    @GetMapping("/edit/{id}")
    public String editRecipeForm(@PathVariable("id") Long id, Authentication authentication, Model model) {
        RecipeDTO recipe = recipeService.findById(id);

        if (recipe == null || recipe.getIs_deleted()) {
            return "redirect:/main";
        }

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/user/login"; //로그인되지 않은 경우 로그인 페이지로
        }

        String currentUsername = authentication.getName();
        if (!recipe.getUserNickname().getNickname().equals(currentUsername)) {
            return "redirect:/recipe/detail/" + id; //작성자가 아닌 경우 상세 페이지로
        }

        model.addAttribute("recipe", recipe);
        return "recipe-edit";
    }

    // 수정된 내용을 저장
    @PostMapping("/update/{id}")
    public String updateRecipe(@PathVariable("id") Long id,
                               @ModelAttribute RecipeDTO recipeDTO,
                               @RequestParam(value = "image", required = false) MultipartFile image,
                               @RequestParam("ingredientsStr") String ingredientsStr,
                               Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/user/login"; //로그인되지 않은 경우
        }

        // 작성자 확인
        RecipeDTO existingRecipe = recipeService.findById(id);
        String currentUsername = authentication.getName();
        if (!existingRecipe.getUserNickname().getNickname().equals(currentUsername)) {
            return "redirect:/detail/" + id; // 작성자가 아닌 경우
        }

        // 이미지 업데이트 (새 이미지가 제공된 경우)
        if (image != null && !image.isEmpty()) {
            try {
                String imgUrl = imageService.saveImageToS3(image);
                recipeDTO.setImg(imgUrl);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("이미지 업로드 실패");
            }
        }

        // 재료 업데이트
        Gson gson = new Gson();
        List<RecipeIngredientDTO> ingredients = List.of(gson.fromJson(ingredientsStr, RecipeIngredientDTO[].class));
        recipeDTO.setIngredients(ingredients);

        // 레시피 업데이트
        recipeService.updateById(id, recipeDTO);

        return "redirect:/detail/" + id; // 수정 완료 후 상세 페이지로 이동
    }

    @PostMapping("/{id}/recommend")
    public String recommendRecipe(@PathVariable("id") Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/user/login"; // 로그인 필요
        }

        String username = authentication.getName();
        recipeService.addRecommendation(id, username);
        return "redirect:/detail/" + id;
    }

    @PostMapping("/{id}/remove-recommend")
    public String removeRecommendRecipe(@PathVariable("id") Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/user/login"; // 로그인 필요
        }

        String username = authentication.getName();
        recipeService.removeRecommendation(id, username);
        return "redirect:/detail/" + id;
    }

    @GetMapping("/ingredient/{id}")
    public String getRecipeToIngredient(@PathVariable("id") Long id, Model model) {
        List<RecipeDTO> recipes = recipeService.findByIngredientsIngredientId(id);
        System.out.println(recipes);
        model.addAttribute("results", recipes);
        model.addAttribute("type", "recipe");
        model.addAttribute("isSearch", true);
        return "recipe";
    }

}
