package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {
    
    @GetMapping("/public")
    public ResponseEntity<Map<String, String>> publicEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Este es un endpoint público");
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/protected")
    public ResponseEntity<Map<String, Object>> protectedEndpoint(@AuthenticationPrincipal OAuth2User oauth2User) {
        Map<String, Object> response = new HashMap<>();
        
        if (oauth2User != null) {
            response.put("message", "Este es un endpoint protegido");
            response.put("status", "success");
            response.put("user", oauth2User.getAttributes());
            response.put("authenticated", true);
        } else {
            response.put("message", "No autenticado");
            response.put("status", "error");
            response.put("authenticated", false);
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/oauth2-debug")
    public ResponseEntity<Map<String, Object>> oauth2Debug(@AuthenticationPrincipal OAuth2User oauth2User) {
        Map<String, Object> response = new HashMap<>();
        
        if (oauth2User != null) {
            response.put("authenticated", true);
            response.put("name", oauth2User.getName());
            response.put("authorities", oauth2User.getAuthorities());
            response.put("attributes", oauth2User.getAttributes());
        } else {
            response.put("authenticated", false);
            response.put("message", "No hay usuario OAuth2 autenticado");
        }
        
        return ResponseEntity.ok(response);
    }
} 