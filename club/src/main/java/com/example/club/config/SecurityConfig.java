package com.example.club.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.extern.log4j.Log4j2;

@EnableWebSecurity // 모든 웹 요청에 대해 Security Filter Chain을 적용
@Log4j2
@Configuration // 스프링 설정 클래스
public class SecurityConfig {
    // 시큐리티 설정 클래스

    @Bean // == 객체 생성
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 어떤 요청이든 기본 폼 형태로 로그인 폼 띄움
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/assets/**").permitAll()
                .requestMatchers("/sample/member").hasRole("MEMBER")
                .requestMatchers("sample/admin").hasRole("ADMIN"))
                // .httpBasic(Customizer.withDefaults()); // 위에 작은 창으로 뜸
                // .formLogin(Customizer.withDefaults()); // 기폰 형태
                .formLogin(login -> login.loginPage("/member/login").permitAll())
                .logout(logout -> logout
                        .logoutUrl("/member/logout") // 로그아웃 POST로 처리
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));

        return http.build();
    }

    // 암호화
    @Bean
    PasswordEncoder passwordEncoder() {
        // 운영, 실무, 여러 암호화 알고리즘 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // 연습, 단일 알고리즘 사용
        // return new BCryptPasswordEncoder();
    }

}
