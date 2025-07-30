package com.example.demo.controller;

import com.example.demo.entity.Maletin;
import com.example.demo.service.MaletinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/maletines")
@CrossOrigin(origins = "*")
public class MaletinController {
    
    @Autowired
    private MaletinService maletinService;
    
    @GetMapping
    public ResponseEntity<List<Maletin>> getAllMaletines() {
        return ResponseEntity.ok(maletinService.getAllMaletines());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Maletin> getMaletinById(@PathVariable Long id) {
        return maletinService.getMaletinById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Maletin> createMaletin(@RequestBody Maletin maletin) {
        return ResponseEntity.ok(maletinService.createMaletin(maletin));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Maletin> updateMaletin(@PathVariable Long id, @RequestBody Maletin maletinDetails) {
        try {
            Maletin updatedMaletin = maletinService.updateMaletin(id, maletinDetails);
            return ResponseEntity.ok(updatedMaletin);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaletin(@PathVariable Long id) {
        maletinService.deleteMaletin(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/cliente/{cliente}")
    public ResponseEntity<List<Maletin>> getMaletinesByCliente(@PathVariable String cliente) {
        return ResponseEntity.ok(maletinService.getMaletinesByCliente(cliente));
    }
    
    @GetMapping("/cajero/{cajero}")
    public ResponseEntity<List<Maletin>> getMaletinesByCajero(@PathVariable String cajero) {
        return ResponseEntity.ok(maletinService.getMaletinesByCajero(cajero));
    }
    
    @GetMapping("/disco/{idDisco}")
    public ResponseEntity<List<Maletin>> getMaletinesByIdDisco(@PathVariable Long idDisco) {
        return ResponseEntity.ok(maletinService.getMaletinesByIdDisco(idDisco));
    }
    
    @GetMapping("/pendientes")
    public ResponseEntity<List<Maletin>> getMaletinesPendientes() {
        return ResponseEntity.ok(maletinService.getMaletinesPendientes());
    }
    
    @GetMapping("/entregados")
    public ResponseEntity<List<Maletin>> getMaletinesEntregados() {
        return ResponseEntity.ok(maletinService.getMaletinesEntregados());
    }
    
    @PostMapping("/{id}/entregar")
    public ResponseEntity<Maletin> entregarMaletin(@PathVariable Long id) {
        try {
            Maletin maletinEntregado = maletinService.entregarMaletin(id);
            return ResponseEntity.ok(maletinEntregado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/fecha-envio")
    public ResponseEntity<List<Maletin>> getMaletinesByFechaEnvio(
            @RequestParam LocalDateTime fechaInicio,
            @RequestParam LocalDateTime fechaFin) {
        return ResponseEntity.ok(maletinService.getMaletinesByFechaEnvio(fechaInicio, fechaFin));
    }
    
    @GetMapping("/fecha-entrega")
    public ResponseEntity<List<Maletin>> getMaletinesByFechaEntrega(
            @RequestParam LocalDateTime fechaInicio,
            @RequestParam LocalDateTime fechaFin) {
        return ResponseEntity.ok(maletinService.getMaletinesByFechaEntrega(fechaInicio, fechaFin));
    }
} 