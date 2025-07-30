package com.example.demo.repository;

import com.example.demo.entity.Maletin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaletinRepository extends JpaRepository<Maletin, Long> {
    
    // Buscar maletines por cliente
    List<Maletin> findByCliente(String cliente);
    
    // Buscar maletines por cajero
    List<Maletin> findByCajero(String cajero);
    
    // Buscar maletines por disco
    List<Maletin> findByIdDisco(Long idDisco);
    
    // Buscar maletines enviados en un rango de fechas
    List<Maletin> findByFechaEnvioBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    // Buscar maletines entregados en un rango de fechas
    List<Maletin> findByFechaEntregaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    // Buscar maletines pendientes de entrega (sin fecha de entrega)
    List<Maletin> findByFechaEntregaIsNull();
    
    // Buscar maletines entregados
    List<Maletin> findByFechaEntregaIsNotNull();
    
    // Verificar si existe un maletín para un disco específico
    boolean existsByIdDisco(Long idDisco);
} 