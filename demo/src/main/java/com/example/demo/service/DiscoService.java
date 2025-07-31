package com.example.demo.service;

import com.example.demo.entity.Disco;
import com.example.demo.repository.DiscoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscoService {

    private final DiscoRepository discoRepository;

    @Autowired
    public DiscoService(DiscoRepository discoRepository) {
        this.discoRepository = discoRepository;
    }

    public List<Disco> findAll() {
        return discoRepository.findAll();
    }

    public Optional<Disco> findById(Long id) {
        return discoRepository.findById(id);
    }

    public List<Disco> findByEstado(String estado) {
        return discoRepository.findByEstado(estado);
    }

    public Disco save(Disco disco) {
        return discoRepository.save(disco);
    }

    public void deleteById(Long id) {
        discoRepository.deleteById(id);
    }

    public Disco actualizarEstado(Long id, String nuevoEstado) {
        Optional<Disco> discoOpt = discoRepository.findById(id);
        if (discoOpt.isPresent()) {
            Disco disco = discoOpt.get();
            disco.setEstado(nuevoEstado);
            return discoRepository.save(disco);
        }
        return null;
    }

    public Disco incrementarHorasUso(Long id, Integer horasAdicionales) {
        Optional<Disco> discoOpt = discoRepository.findById(id);
        if (discoOpt.isPresent()) {
            Disco disco = discoOpt.get();
            Integer horasActuales = disco.getHorasUso() != null ? disco.getHorasUso() : 0;
            disco.setHorasUso(horasActuales + horasAdicionales);
            return discoRepository.save(disco);
        }
        return null;
    }
}