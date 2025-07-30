package com.example.demo.controller;

import com.example.demo.entity.Usuario;
import com.example.demo.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        // Este endpoint requeriría un servicio adicional para obtener todos los usuarios
        // Por ahora, solo retornamos una lista vacía
        return ResponseEntity.ok(List.of());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        // Este endpoint requeriría un servicio adicional
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> getUsuarioByEmail(@PathVariable String email) {
        Optional<Usuario> usuario = authenticationService.findByEmail(email);
        return usuario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/microsoft/{microsoftId}")
    public ResponseEntity<Usuario> getUsuarioByMicrosoftId(@PathVariable String microsoftId) {
        Optional<Usuario> usuario = authenticationService.findByMicrosoftId(microsoftId);
        return usuario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetails) {
        // Este endpoint requeriría un servicio adicional
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        // Este endpoint requeriría un servicio adicional
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/activar")
    public ResponseEntity<Usuario> activarUsuario(@PathVariable Long id) {
        // Este endpoint requeriría un servicio adicional
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/desactivar")
    public ResponseEntity<Usuario> desactivarUsuario(@PathVariable Long id) {
        // Este endpoint requeriría un servicio adicional
        return ResponseEntity.notFound().build();
    }
} 