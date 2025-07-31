package com.example.demo.controller;

import com.example.demo.dto.AuthRequestDTO;
import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/request-code")
    public ResponseEntity<AuthResponseDTO> requestLoginCode(@RequestBody AuthRequestDTO request) {
        try {
            authService.requestLoginCode(request.getEmail());
            
            AuthResponseDTO response = new AuthResponseDTO(
                true,
                "Código de verificación enviado a " + request.getEmail(),
                request.getEmail()
            );
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            AuthResponseDTO response = new AuthResponseDTO(
                false,
                e.getMessage(),
                request.getEmail()
            );
            
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<AuthResponseDTO> verifyLoginCode(@RequestBody AuthRequestDTO request) {
        boolean isValid = authService.verifyLoginCode(request.getEmail(), request.getCode());
        
        AuthResponseDTO response;
        
        if (isValid) {
            response = new AuthResponseDTO(
                true,
                "Autenticación exitosa",
                request.getEmail()
            );
            return ResponseEntity.ok(response);
        } else {
            response = new AuthResponseDTO(
                false,
                "Código inválido o expirado",
                request.getEmail()
            );
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getPrincipal().equals("anonymousUser")) {
            
            HashMap<String, Object> user = new HashMap<>();
            user.put("email", authentication.getName());
            user.put("authenticated", true);
            
            HashMap<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", user);
            
            return ResponseEntity.ok(response);
        }
        
        HashMap<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "No autenticado");
        
        return ResponseEntity.status(401).body(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        
        return ResponseEntity.ok(new AuthResponseDTO(
            true,
            "Sesión cerrada correctamente",
            null
        ));
    }
}