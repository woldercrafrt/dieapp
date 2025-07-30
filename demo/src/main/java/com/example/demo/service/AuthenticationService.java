package com.example.demo.service;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            return User.builder()
                    .username(usuario.getEmail())
                    .password("") // No password for OAuth2
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                    .accountExpired(false)
                    .accountLocked(!usuario.getActivo())
                    .credentialsExpired(false)
                    .disabled(!usuario.getActivo())
                    .build();
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }
    }
    
    public Usuario processOAuth2User(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String microsoftId = oAuth2User.getName(); // Microsoft ID
        String nombreCompleto = oAuth2User.getAttribute("name");
        
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(email);
        
        if (usuarioExistente.isPresent()) {
            // Usuario existe, actualizar último acceso
            Usuario usuario = usuarioExistente.get();
            usuario.setUltimoAcceso(LocalDateTime.now());
            usuario.setMicrosoftId(microsoftId);
            return usuarioRepository.save(usuario);
        } else {
            // Crear nuevo usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setNombreCompleto(nombreCompleto);
            nuevoUsuario.setMicrosoftId(microsoftId);
            nuevoUsuario.setFechaRegistro(LocalDateTime.now());
            nuevoUsuario.setUltimoAcceso(LocalDateTime.now());
            nuevoUsuario.setActivo(true);
            nuevoUsuario.setRolUsuario("USER");
            
            return usuarioRepository.save(nuevoUsuario);
        }
    }
    
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    public Optional<Usuario> findByMicrosoftId(String microsoftId) {
        return usuarioRepository.findByMicrosoftId(microsoftId);
    }
    
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
} 