package com.example.demo.service;

import com.example.demo.entity.Maletin;
import com.example.demo.entity.Disco;
import com.example.demo.repository.MaletinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MaletinService {

    private final MaletinRepository maletinRepository;

    @Autowired
    public MaletinService(MaletinRepository maletinRepository) {
        this.maletinRepository = maletinRepository;
    }

    public List<Maletin> findAll() {
        return maletinRepository.findAll();
    }

    public Optional<Maletin> findById(Long id) {
        return maletinRepository.findById(id);
    }

    public List<Maletin> findByCliente(String cliente) {
        return maletinRepository.findByCliente(cliente);
    }

    public List<Maletin> findByCajero(String cajero) {
        return maletinRepository.findByCajero(cajero);
    }

    public List<Maletin> findByDisco(Disco disco) {
        return maletinRepository.findByDisco(disco);
    }

    public List<Maletin> findByFechaEnvioBetween(LocalDateTime inicio, LocalDateTime fin) {
        return maletinRepository.findByFechaEnvioBetween(inicio, fin);
    }

    public Maletin save(Maletin maletin) {
        return maletinRepository.save(maletin);
    }

    public void deleteById(Long id) {
        maletinRepository.deleteById(id);
    }

    public Maletin registrarEntrega(Long id) {
        Optional<Maletin> maletinOpt = maletinRepository.findById(id);
        if (maletinOpt.isPresent()) {
            Maletin maletin = maletinOpt.get();
            maletin.setFechaEntrega(LocalDateTime.now());
            return maletinRepository.save(maletin);
        }
        return null;
    }
}