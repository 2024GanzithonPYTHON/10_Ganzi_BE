package com.example.APT.utils.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;


    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(tokenProvider);
    }


    private final CustomUserDetailsService customUserDetailsService; // 사용자 정보 로드 서비스

    /**
     * PasswordEncoder 빈 등록
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



// 충돌로 인한 주석 처리
//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         // CSRF 설정 Disable
//         http
//                 .csrf((auth) -> auth.disable());

//         //From 로그인 방식 disable
//         http
//                 .formLogin((auth) -> auth.disable());

//         //http basic 인증 방식 disable
//         http
//                 .httpBasic((auth) -> auth.disable());

//         // 시큐리티는 기본적으로 세션을 사용
//         // JWT는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
//         http
//                 .sessionManagement((session) -> session
//                         .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//         // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
//         //경로별 인가 작업
// //        http
// //                .authorizeHttpRequests((auth) -> auth
// //                        .requestMatchers(GlobalConstant.noTokenNeadedAPIs).permitAll()
// //                        .anyRequest().authenticated());

//         http
//                 .authorizeHttpRequests((auth) -> auth
//                         .requestMatchers("/user/login", "/user/signup").permitAll()
//                         .anyRequest().authenticated());




//         // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
//         http
//                 .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);


    /**
     * AuthenticationManager 빈 등록
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        // CustomUserDetailsService와 PasswordEncoder를 설정
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }

    /**
     * SecurityFilterChain 설정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http.csrf(csrf -> csrf.disable());

        // Form 로그인 방식 비활성화
        http.formLogin(form -> form.disable());

        // HTTP Basic 인증 비활성화
        http.httpBasic(httpBasic -> httpBasic.disable());

        // 세션 정책: JWT 사용 시 세션 비활성화
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // URL별 권한 설정
        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers("/user/login", "/user/signup").permitAll() // 로그인과 회원가입은 인증 없이 접근 가능
                        .anyRequest().authenticated() // 다른 요청은 인증 필요
        );

        // JWT 필터 추가
        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

// 충돌로 인한 주석 처리
//     @Bean
//     public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//         AuthenticationManagerBuilder authenticationManagerBuilder =
//                 http.getSharedObject(AuthenticationManagerBuilder.class);
//         return authenticationManagerBuilder.build();
//     }
// }

}

