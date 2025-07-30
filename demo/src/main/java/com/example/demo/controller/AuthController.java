package com.example.demo.controller;

import com.example.demo.entity.Usuario;
import com.example.demo.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal OAuth2User oauth2User) {
        Map<String, Object> response = new HashMap<>();
        
        if (oauth2User != null) {
            String email = oauth2User.getAttribute("email");
            String microsoftId = oauth2User.getName();
            String nombreCompleto = oauth2User.getAttribute("name");
            
            // Procesar o crear usuario
            Usuario usuario = authenticationService.processOAuth2User(oauth2User);
            
            response.put("authenticated", true);
            response.put("email", email);
            response.put("microsoftId", microsoftId);
            response.put("nombreCompleto", nombreCompleto);
            response.put("usuario", usuario);
            response.put("message", "Usuario autenticado exitosamente");
            
            return ResponseEntity.ok(response);
        } else {
            response.put("authenticated", false);
            response.put("message", "Usuario no autenticado");
            response.put("loginUrl", "/oauth2/authorization/microsoft");
            return ResponseEntity.status(401).body(response);
        }
    }
    
    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> login() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Redirigiendo a Microsoft para autenticación");
        response.put("loginUrl", "/oauth2/authorization/microsoft");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Sesión cerrada exitosamente");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkAuth(@AuthenticationPrincipal OAuth2User oauth2User) {
        Map<String, Object> response = new HashMap<>();
        
        if (oauth2User != null) {
            response.put("authenticated", true);
            response.put("email", oauth2User.getAttribute("email"));
            response.put("name", oauth2User.getAttribute("name"));
            response.put("microsoftId", oauth2User.getName());
        } else {
            response.put("authenticated", false);
            response.put("message", "Usuario no autenticado");
            response.put("loginUrl", "/oauth2/authorization/microsoft");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/error")
    public ResponseEntity<Map<String, Object>> handleAuthError(@RequestParam(required = false) String error,
                                                             @RequestParam(required = false) String error_description) {
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", false);
        response.put("error", error != null ? error : "unknown_error");
        response.put("error_description", error_description != null ? error_description : "Error de autenticación");
        response.put("message", "Error en la autenticación OAuth2");
        response.put("loginUrl", "/oauth2/authorization/microsoft");
        
        return ResponseEntity.status(400).body(response);
    }
} 