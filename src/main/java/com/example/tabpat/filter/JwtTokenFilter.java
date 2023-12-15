package com.example.tabpat.filter;

import com.example.tabpat.config.JwtConfig;
import com.example.tabpat.util.JwtTokenUtil;
import com.example.tabpat.util.RedisUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtTokenUtil jwtTokenUtil;
    private RedisUtils redisUtils;
    private JwtConfig jwtConfig;

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Autowired
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Autowired
    public void setJwtConfig(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);
            // 验证 token
            if (jwtTokenUtil.isTokenValid(jwtToken)) {
                String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                //获取用户信息
                String RedisToken = redisUtils.get(JwtConfig.REDIS_TOKEN_KEY_PREFIX + username);
                if (RedisToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    //刷新token
                    redisUtils.setEx(JwtConfig.REDIS_TOKEN_KEY_PREFIX + username, jwtTokenUtil.refreshToken(jwtToken), jwtConfig.getTime(), TimeUnit.SECONDS);
                    UsernamePasswordAuthenticationToken authenticationToken = jwtTokenUtil.getAuthentication(jwtToken);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } else {
                filterChain.doFilter(request, response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
