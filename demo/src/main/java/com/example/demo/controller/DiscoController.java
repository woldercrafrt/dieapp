package com.example.demo.controller;

import com.example.demo.entity.Disco;
import com.example.demo.service.DiscoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/discos")
public class DiscoController {

    private final DiscoService discoService;

    @Autowired
    public DiscoController(DiscoService discoService) {
        this.discoService = discoService;
    }

    @GetMapping
    public ResponseEntity<List<Disco>> getAllDiscos() {
        return ResponseEntity.ok(discoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disco> getDiscoById(@PathVariable Long id) {
        Optional<Disco> disco = discoService.findById(id);
        return disco.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Disco>> getDiscosByEstado(@PathVariable String estado) {
        List<Disco> discos = discoService.findByEstado(estado);
        return ResponseEntity.ok(discos);
    }

    @PostMapping
    public ResponseEntity<Disco> createDisco(@RequestBody Disco disco) {
        Disco nuevoDisco = discoService.save(disco);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDisco);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Disco> updateDisco(@PathVariable Long id, @RequestBody Disco disco) {
        if (!discoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        disco.setId(id);
        return ResponseEntity.ok(discoService.save(disco));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisco(@PathVariable Long id) {
        if (!discoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        discoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Disco> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        Disco disco = discoService.actualizarEstado(id, estado);
        if (disco != null) {
            return ResponseEntity.ok(disco);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/incrementar-horas")
    public ResponseEntity<Disco> incrementarHorasUso(@PathVariable Long id, @RequestParam Integer horas) {
        Disco disco = discoService.incrementarHorasUso(id, horas);
        if (disco != null) {
            return ResponseEntity.ok(disco);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}