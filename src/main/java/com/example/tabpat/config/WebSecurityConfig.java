package com.example.tabpat.config;

import com.example.tabpat.filter.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Security拦截配置
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private JwtTokenFilter jwtTokenFilter;
    @Autowired
    public void setJwtTokenFilter(JwtTokenFilter jwtTokenFilter){
        this.jwtTokenFilter = jwtTokenFilter;
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 用新的方式配置 CSRF，如果需要禁用，使用 csrf().disable()
                .csrf(AbstractHttpConfigurer::disable)
                // 用新的方式配置会话管理
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/public/**").permitAll() // 为了方便测试，放行post
                        .requestMatchers("/secure/**").authenticated()
                );
        return http.build();
    }
}
