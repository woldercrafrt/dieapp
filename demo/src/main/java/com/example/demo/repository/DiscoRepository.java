package com.example.demo.repository;

import com.example.demo.entity.Disco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscoRepository extends JpaRepository<Disco, Long> {
    
    // Buscar discos por estado
    List<Disco> findByEstado(String estado);
    
    // Buscar discos disponibles (estado = "disponible")
    List<Disco> findByEstadoIgnoreCase(String estado);
    
    // Buscar discos con horas de uso mayor a un valor
    List<Disco> findByHorasUsoGreaterThan(Integer horas);
    
    // Buscar discos con horas de uso menor a un valor
    List<Disco> findByHorasUsoLessThan(Integer horas);
    
    // Buscar discos en un rango de horas de uso
    List<Disco> findByHorasUsoBetween(Integer horasMin, Integer horasMax);
    
    // Verificar si existe un disco con un estado específico
    boolean existsByEstado(String estado);
    
    // Contar discos por estado
    long countByEstado(String estado);
} 