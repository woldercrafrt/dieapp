package com.example.demo.service;

import com.example.demo.entity.Maletin;
import com.example.demo.repository.MaletinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MaletinService {
    
    @Autowired
    private MaletinRepository maletinRepository;
    
    public List<Maletin> getAllMaletines() {
        return maletinRepository.findAll();
    }
    
    public Optional<Maletin> getMaletinById(Long id) {
        return maletinRepository.findById(id);
    }
    
    public Maletin createMaletin(Maletin maletin) {
        if (maletin.getFechaEnvio() == null) {
            maletin.setFechaEnvio(LocalDateTime.now());
        }
        return maletinRepository.save(maletin);
    }
    
    public Maletin updateMaletin(Long id, Maletin maletinDetails) {
        Maletin maletin = maletinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maletín no encontrado"));
        
        maletin.setCliente(maletinDetails.getCliente());
        maletin.setCajero(maletinDetails.getCajero());
        maletin.setFechaEnvio(maletinDetails.getFechaEnvio());
        maletin.setIdDisco(maletinDetails.getIdDisco());
        maletin.setFechaEntrega(maletinDetails.getFechaEntrega());
        
        return maletinRepository.save(maletin);
    }
    
    public void deleteMaletin(Long id) {
        maletinRepository.deleteById(id);
    }
    
    public List<Maletin> getMaletinesByCliente(String cliente) {
        return maletinRepository.findByCliente(cliente);
    }
    
    public List<Maletin> getMaletinesByCajero(String cajero) {
        return maletinRepository.findByCajero(cajero);
    }
    
    public List<Maletin> getMaletinesByIdDisco(Long idDisco) {
        return maletinRepository.findByIdDisco(idDisco);
    }
    
    public List<Maletin> getMaletinesByFechaEnvio(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return maletinRepository.findByFechaEnvioBetween(fechaInicio, fechaFin);
    }
    
    public List<Maletin> getMaletinesByFechaEntrega(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return maletinRepository.findByFechaEntregaBetween(fechaInicio, fechaFin);
    }
    
    public List<Maletin> getMaletinesPendientes() {
        return maletinRepository.findByFechaEntregaIsNull();
    }
    
    public List<Maletin> getMaletinesEntregados() {
        return maletinRepository.findByFechaEntregaIsNotNull();
    }
    
    public Maletin entregarMaletin(Long id) {
        Maletin maletin = maletinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maletín no encontrado"));
        
        maletin.setFechaEntrega(LocalDateTime.now());
        return maletinRepository.save(maletin);
    }
    
    public boolean existsByIdDisco(Long idDisco) {
        return maletinRepository.existsByIdDisco(idDisco);
    }
} 