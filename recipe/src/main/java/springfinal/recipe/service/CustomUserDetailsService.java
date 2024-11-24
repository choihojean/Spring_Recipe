package springfinal.recipe.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springfinal.recipe.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    //인증 관련 로직에 초점을 두고 있는 클래스(회원가입, 업데이트, 탈퇴 등의 기능은 적합하지 않음)
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        springfinal.recipe.model.User appUser = userRepository.findByNickname(username)
                .filter(user -> !user.getIs_deleted())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        return User.builder()
                .username(appUser.getNickname())
                .password(appUser.getPassword())
                .roles("USER") //역할
                .build();
    }
}