package com.example.cms.config;

import org.springframework.security.core.userdetails.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.cms.entity.Member;
import com.example.cms.exception.MemberNotFoundException;
import com.example.cms.repository.MemberRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private MemberRepository memberRepository;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
            AuthFilter authFilter) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.sessionManagement(configure -> configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers("/api/vs1/member", "/api/vs1/login", "/api/vs1/logout").permitAll()

                .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Member member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new MemberNotFoundException("User not found: " + username));
            return User.builder()
                    .username(member.getUsername())
                    .password(member.getPassword())
                    .roles("USER") // Hoặc lấy danh sách các roles từ đối tượng member nếu có
                    .build();
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

}
