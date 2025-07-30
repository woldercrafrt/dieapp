package com.example.demo.service;

import com.example.demo.entity.Disco;
import com.example.demo.repository.DiscoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscoService {
    
    @Autowired
    private DiscoRepository discoRepository;
    
    public List<Disco> getAllDiscos() {
        return discoRepository.findAll();
    }
    
    public Optional<Disco> getDiscoById(Long id) {
        return discoRepository.findById(id);
    }
    
    public Disco createDisco(Disco disco) {
        if (disco.getHorasUso() == null) {
            disco.setHorasUso(0);
        }
        if (disco.getEstado() == null) {
            disco.setEstado("disponible");
        }
        return discoRepository.save(disco);
    }
    
    public Disco updateDisco(Long id, Disco discoDetails) {
        Disco disco = discoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disco no encontrado"));
        
        disco.setEstado(discoDetails.getEstado());
        disco.setHorasUso(discoDetails.getHorasUso());
        
        return discoRepository.save(disco);
    }
    
    public void deleteDisco(Long id) {
        discoRepository.deleteById(id);
    }
    
    public List<Disco> getDiscosByEstado(String estado) {
        return discoRepository.findByEstado(estado);
    }
    
    public List<Disco> getDiscosDisponibles() {
        return discoRepository.findByEstadoIgnoreCase("disponible");
    }
    
    public List<Disco> getDiscosByHorasUsoGreaterThan(Integer horas) {
        return discoRepository.findByHorasUsoGreaterThan(horas);
    }
    
    public List<Disco> getDiscosByHorasUsoLessThan(Integer horas) {
        return discoRepository.findByHorasUsoLessThan(horas);
    }
    
    public List<Disco> getDiscosByHorasUsoBetween(Integer horasMin, Integer horasMax) {
        return discoRepository.findByHorasUsoBetween(horasMin, horasMax);
    }
    
    public Disco actualizarHorasUso(Long id, Integer horasAdicionales) {
        Disco disco = discoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disco no encontrado"));
        
        disco.setHorasUso(disco.getHorasUso() + horasAdicionales);
        return discoRepository.save(disco);
    }
    
    public Disco cambiarEstado(Long id, String nuevoEstado) {
        Disco disco = discoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disco no encontrado"));
        
        disco.setEstado(nuevoEstado);
        return discoRepository.save(disco);
    }
    
    public boolean existsByEstado(String estado) {
        return discoRepository.existsByEstado(estado);
    }
    
    public long countByEstado(String estado) {
        return discoRepository.countByEstado(estado);
    }
} 