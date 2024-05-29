package com.example.cms.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.cms.util.TokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFilter extends OncePerRequestFilter {
    @Autowired
    private final UserDetailsService userDetailsService;

    public AuthFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Phương thức này được triệu hồi khi một request được thực hiện
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException,
            IOException {
        // Lấy token từ header "Authorization"
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = TokenProvider.extractUsername(token);
        }

        // Nếu tên người dùng không null và không có xác thực trong
        // SecurityContextHolder
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Tải thông tin người dùng từ userDetailsService
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // Kiểm tra tính hợp lệ của token
            if (TokenProvider.validateToken(token, userDetails)) {
                // Xác thực người dùng và thiết lập Authentication trong SecurityContextHolder
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Chuyển tiếp request và response cho các filter khác trong filterChain
        filterChain.doFilter(request, response);
    }
}
