package com.example.demo.controller;

import com.example.demo.entity.Disco;
import com.example.demo.service.DiscoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discos")
@CrossOrigin(origins = "*")
public class DiscoController {
    
    @Autowired
    private DiscoService discoService;
    
    @GetMapping
    public ResponseEntity<List<Disco>> getAllDiscos() {
        return ResponseEntity.ok(discoService.getAllDiscos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Disco> getDiscoById(@PathVariable Long id) {
        return discoService.getDiscoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Disco> createDisco(@RequestBody Disco disco) {
        return ResponseEntity.ok(discoService.createDisco(disco));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Disco> updateDisco(@PathVariable Long id, @RequestBody Disco discoDetails) {
        try {
            Disco updatedDisco = discoService.updateDisco(id, discoDetails);
            return ResponseEntity.ok(updatedDisco);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisco(@PathVariable Long id) {
        discoService.deleteDisco(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Disco>> getDiscosByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(discoService.getDiscosByEstado(estado));
    }
    
    @GetMapping("/disponibles")
    public ResponseEntity<List<Disco>> getDiscosDisponibles() {
        return ResponseEntity.ok(discoService.getDiscosDisponibles());
    }
    
    @GetMapping("/horas-uso/mayor/{horas}")
    public ResponseEntity<List<Disco>> getDiscosByHorasUsoGreaterThan(@PathVariable Integer horas) {
        return ResponseEntity.ok(discoService.getDiscosByHorasUsoGreaterThan(horas));
    }
    
    @GetMapping("/horas-uso/menor/{horas}")
    public ResponseEntity<List<Disco>> getDiscosByHorasUsoLessThan(@PathVariable Integer horas) {
        return ResponseEntity.ok(discoService.getDiscosByHorasUsoLessThan(horas));
    }
    
    @GetMapping("/horas-uso/rango")
    public ResponseEntity<List<Disco>> getDiscosByHorasUsoBetween(
            @RequestParam Integer horasMin,
            @RequestParam Integer horasMax) {
        return ResponseEntity.ok(discoService.getDiscosByHorasUsoBetween(horasMin, horasMax));
    }
    
    @PostMapping("/{id}/actualizar-horas")
    public ResponseEntity<Disco> actualizarHorasUso(@PathVariable Long id, @RequestParam Integer horasAdicionales) {
        try {
            Disco discoActualizado = discoService.actualizarHorasUso(id, horasAdicionales);
            return ResponseEntity.ok(discoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{id}/cambiar-estado")
    public ResponseEntity<Disco> cambiarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        try {
            Disco discoActualizado = discoService.cambiarEstado(id, nuevoEstado);
            return ResponseEntity.ok(discoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/estado/{estado}/count")
    public ResponseEntity<Long> countByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(discoService.countByEstado(estado));
    }
} 