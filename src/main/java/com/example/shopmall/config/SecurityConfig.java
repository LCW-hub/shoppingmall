package com.example.shopmall.config; // ⚠️ 패키지명은 본인 프로젝트에 맞게 수정하세요 (예: com.myshop.config)

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/*
	 * @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws
	 * Exception { http .authorizeHttpRequests((auth) -> auth .requestMatchers(new
	 * AntPathRequestMatcher("/h2-console/**")).permitAll() // H2 콘솔 접속 허용
	 * .anyRequest().authenticated() // 그 외 요청은 인증 필요 (로그인 기능 구현 전이면 .permitAll()로
	 * 변경 가능) ) .csrf((csrf) -> csrf .ignoringRequestMatchers(new
	 * AntPathRequestMatcher("/h2-console/**")) // H2 콘솔만 CSRF 보안 끄기 )
	 * .headers((headers) -> headers .frameOptions((frame) -> frame.sameOrigin()) //
	 * ⭐ 중요: 화면 깨짐 방지 (Frame 허용) );
	 * 
	 * return http.build(); }
	 */
    
    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable()) // 테스트 환경에서는 CSRF 비활성화 권장
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/**").permitAll() // 모든 요청 허용 (테스트용)
	            // .requestMatchers("/admin/**").hasRole("ADMIN") // 특정 경로 제한 예시
	            .anyRequest().authenticated()
	        );
	    return http.build();
	}
}