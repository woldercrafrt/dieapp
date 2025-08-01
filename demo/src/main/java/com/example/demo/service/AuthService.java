package com.example.demo.service;

import com.example.demo.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Date;
import javax.crypto.SecretKey;

@Service
public class AuthService {

    private final VerificationCodeService verificationCodeService;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    @Autowired
    public AuthService(VerificationCodeService verificationCodeService, UsuarioService usuarioService, JwtService jwtService) {
        this.verificationCodeService = verificationCodeService;
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    public void requestLoginCode(String email) {
        // Verificar si el usuario existe
        Usuario usuario = usuarioService.findByEmail(email);
        
        if (usuario == null) {
            // Si el usuario no existe, podríamos crearlo automáticamente o lanzar una excepción
            // En este caso, vamos a crear un usuario nuevo
            usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setNombreCompleto("Usuario de " + email);
            usuario.setRolUsuario("ROLE_USER");
            usuario.setActivo(true);
            usuarioService.save(usuario);
        }
        
        // Generar y enviar el código de verificación
        verificationCodeService.generateAndSendCode(email);
    }

    public String verifyLoginCode(String email, String code) {
        boolean isValid = verificationCodeService.verifyCode(email, code);
        
        if (isValid) {
            // Buscar el usuario
            Usuario usuario = usuarioService.findByEmail(email);
            
            if (usuario != null) {
                // Actualizar el último acceso
                usuario.setUltimoAcceso(LocalDateTime.now());
                usuarioService.save(usuario);
                
                // Generar token JWT
                String token = jwtService.generateToken(email, usuario.getRolUsuario());
                
                return token;
            }
        }
        
        return null;
    }

    private void authenticateUser(Usuario usuario) {
        // Crear una lista de autoridades basada en el rol del usuario
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(usuario.getRolUsuario());
        
        // Crear un token de autenticación (sin contraseña, ya que usamos código de verificación)
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                usuario.getEmail(),
                null, // No hay contraseña
                Collections.singletonList(authority)
        );
        
        // Establecer la autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}