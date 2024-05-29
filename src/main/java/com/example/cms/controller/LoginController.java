package com.example.cms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cms.dto.LoginDTO;
import com.example.cms.dto.ResponseLoginDTO;
import com.example.cms.util.TokenProvider;

@RestController
@RequestMapping("/api/vs1/login")
public class LoginController {
    private final AuthenticationManager authenticationManager;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
            if (authentication.isAuthenticated()) {
                ResponseLoginDTO responseLoginDTO = new ResponseLoginDTO();
                responseLoginDTO.setUsername(loginDTO.getUsername());
                responseLoginDTO.setToken("Bearer " + TokenProvider.generateToken(loginDTO.getUsername()));
                return new ResponseEntity<>(responseLoginDTO, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>("Đăng nhập thất bại", HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
