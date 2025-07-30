package com.example.demo.repository;

import com.example.demo.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar usuario por email
    Optional<Usuario> findByEmail(String email);
    
    // Buscar usuario por Microsoft ID
    Optional<Usuario> findByMicrosoftId(String microsoftId);
    
    // Buscar usuarios por rol
    List<Usuario> findByRolUsuario(String rolUsuario);
    
    // Buscar usuarios activos
    List<Usuario> findByActivoTrue();
    
    // Buscar usuarios inactivos
    List<Usuario> findByActivoFalse();
    
    // Verificar si existe un usuario por email
    boolean existsByEmail(String email);
    
    // Verificar si existe un usuario por Microsoft ID
    boolean existsByMicrosoftId(String microsoftId);
    
    // Buscar usuarios por nombre completo (búsqueda parcial)
    List<Usuario> findByNombreCompletoContainingIgnoreCase(String nombre);
    
    // Contar usuarios por rol
    long countByRolUsuario(String rolUsuario);
    
    // Contar usuarios activos
    long countByActivoTrue();
} 