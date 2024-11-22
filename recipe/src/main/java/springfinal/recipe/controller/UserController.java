package springfinal.recipe.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfinal.recipe.dto.UserDTO;
import springfinal.recipe.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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
            return "redirect:/user/login"; // 성공 시 login.html로 리다이렉트
        } else {
            model.addAttribute("error", "이미 사용 중인 닉네임입니다.");
            return "register"; // 회원가입 실패 시 다시 register.html
        }
    }

    //로그인 페이지
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    //로그인 처리
    @PostMapping("/login")
    public String loginUser(@RequestParam("nickname") String nickname, @RequestParam("password") String password, HttpSession session, Model model) {
        UserDTO userDTO = userService.authenticate(nickname, password);
        if (userDTO != null) {
            session.setAttribute("loggedInUser", userDTO); //세션에 사용자 정보 저장
            return "redirect:/main";
        } else {
            model.addAttribute("error", "닉네임 또는 비밀번호가 올바르지 않습니다.");
            return "login";
        }
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        UserDTO user = (UserDTO) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("user", user);
        return "profile";
    }

    //정보 수정 페이지
    @GetMapping("/edit")
    public String showEditForm(HttpSession session, Model model) {
        UserDTO user = (UserDTO) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("user", user);
        return "user-edit";
    }

    //정보 수정 처리
    @PostMapping("/edit")
    public String updateUser(@ModelAttribute("user") UserDTO userDTO, HttpSession session) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";
        }

        userService.updateUser(loggedInUser.getId(), userDTO);
        session.setAttribute("loggedInUser", userDTO); //세션 정보 업데이트
        return "redirect:/user/profile";
    }

    //로그아웃 처리
    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate(); //세션 종료
        return "redirect:/main";
    }

    // 회원 탈퇴 처리
    @PostMapping("/delete")
    public String deleteUser(HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/user/login";
        }

        userService.deleteUser(user.getId());
        session.invalidate(); //세션 종료
        return "redirect:/main";
    }
}
