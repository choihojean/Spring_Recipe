package springfinal.recipe.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfinal.recipe.dto.UserDTO;
import springfinal.recipe.service.CommentService;
import springfinal.recipe.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    //회원가입 페이지
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    //회원가입 처리
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDTO userDTO, Model model) {
        boolean isRegistered = userService.registerUser(userDTO);
        if (isRegistered) {
            return "redirect:/main";
        } else {
            model.addAttribute("error", "이미 사용 중인 닉네임입니다.");
            return "register";
        }
    }

    //로그인 페이지
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

//    //로그인 처리
//    @PostMapping("/login")
//    public String loginUser(@RequestParam("nickname") String nickname, @RequestParam("password") String password, HttpSession session, Model model) {
//        UserDTO userDTO = userService.authenticate(nickname, password);
//        if (userDTO != null) {
//            session.setAttribute("loggedInUser", userDTO); //세션에 사용자 정보 저장
//            return "redirect:/main";
//        } else {
//            model.addAttribute("error", "닉네임 또는 비밀번호가 올바르지 않습니다.");
//            return "login";
//        }
//    }

    @GetMapping("/profile")
    public String showProfile(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/user/login";
        }

        // 현재 인증된 사용자의 닉네임 가져오기
        String nickname = authentication.getName();
        UserDTO user = userService.findByNickname(nickname);

        if (user == null) {
            return "redirect:/user/login";
        }

        model.addAttribute("user", user);
        return "profile";
    }

    //정보 수정 페이지
    @GetMapping("/edit")
    public String showEditForm(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/user/login"; // 인증되지 않은 사용자라면 로그인 페이지로 리다이렉트
        }

        // 현재 로그인한 사용자의 닉네임 가져오기
        String nickname = authentication.getName();
        UserDTO user = userService.findByNickname(nickname);

        if (user == null) {
            return "redirect:/user/login";
        }

        model.addAttribute("user", user);
        return "user-edit"; // 정보 수정 페이지로 이동
    }

    //정보 수정 처리
    @PostMapping("/edit")
    public String updateUser(@ModelAttribute("user") UserDTO userDTO, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/user/login";
        }

        String nickname = authentication.getName();
        UserDTO loggedInUser = userService.findByNickname(nickname);

        if (loggedInUser == null) {
            return "redirect:/user/login";
        }

        boolean isSuccess = userService.updateUser(loggedInUser.getId(), userDTO);

        if(isSuccess) {
            commentService.updateCommentByUser(nickname, userDTO);
            return "redirect:/user/profile";
        }
        else {
            return "redirect:/user/edit";
        }
    }

//    //로그아웃 처리
//    @GetMapping("/logout")
//    public String logoutUser(HttpSession session) {
//        session.invalidate(); //세션 종료
//        return "redirect:/main";
//    }

    // 회원 탈퇴 처리
    @PostMapping("/delete")
    public String deleteUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/user/login"; // 인증되지 않은 사용자라면 로그인 페이지로 리다이렉트
        }

        // 현재 인증된 사용자의 닉네임 가져오기
        String nickname = authentication.getName();
        UserDTO user = userService.findByNickname(nickname);

        if (user == null) {
            return "redirect:/user/login"; // 사용자 정보가 없는 경우 로그인 페이지로 리다이렉트
        }

        userService.deleteUser(user.getId()); // 사용자 삭제 처리
        SecurityContextHolder.clearContext(); //Spring Security 컨텍스트 초기화

        return "redirect:/main"; // 탈퇴 후 메인 페이지로 이동
    }
}
