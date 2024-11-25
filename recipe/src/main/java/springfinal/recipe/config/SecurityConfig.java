package springfinal.recipe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 모든 요청 허용
                )
                .formLogin(form -> form
                        .loginPage("/user/login") //로그인 페이지 경로
                        .loginProcessingUrl("/user/login") //로그인 처리 URL
                        .defaultSuccessUrl("/main", true) //로그인 성공 시 이동 경로
                        .failureUrl("/user/login?error") //로그인 실패 시 이동 경로
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/user/logout") //로그아웃 경로
                        .logoutSuccessUrl("/main") //로그아웃 성공 후 이동 경로
                        .invalidateHttpSession(true) //세션 무효화
                        .clearAuthentication(true)   //인증 정보 제거
                        .permitAll()
                );

        return http.build();
    }
}
