package com.example.demo.controller;

import com.example.demo.entity.Maletin;
import com.example.demo.entity.Disco;
import com.example.demo.entity.EstadoMaletin;
import com.example.demo.service.MaletinService;
import com.example.demo.service.DiscoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/maletines")
public class MaletinController {

    private final MaletinService maletinService;
    private final DiscoService discoService;

    @Autowired
    public MaletinController(MaletinService maletinService, DiscoService discoService) {
        this.maletinService = maletinService;
        this.discoService = discoService;
    }

    @GetMapping
    public ResponseEntity<List<Maletin>> getAllMaletines() {
        return ResponseEntity.ok(maletinService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Maletin> getMaletinById(@PathVariable Long id) {
        Optional<Maletin> maletin = maletinService.findById(id);
        return maletin.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{cliente}")
    public ResponseEntity<List<Maletin>> getMaletinesByCliente(@PathVariable String cliente) {
        List<Maletin> maletines = maletinService.findByCliente(cliente);
        return ResponseEntity.ok(maletines);
    }

    @GetMapping("/cajero/{cajero}")
    public ResponseEntity<List<Maletin>> getMaletinesByCajero(@PathVariable String cajero) {
        List<Maletin> maletines = maletinService.findByCajero(cajero);
        return ResponseEntity.ok(maletines);
    }
    
    @GetMapping("/sucursal/{sucursal}")
    public ResponseEntity<List<Maletin>> getMaletinesBySucursal(@PathVariable String sucursal) {
        List<Maletin> maletines = maletinService.findBySucursal(sucursal);
        return ResponseEntity.ok(maletines);
    }

    @GetMapping("/disco/{discoId}")
    public ResponseEntity<List<Maletin>> getMaletinesByDisco(@PathVariable Long discoId) {
        Optional<Disco> discoOpt = discoService.findById(discoId);
        if (!discoOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        List<Maletin> maletines = maletinService.findByDisco(discoOpt.get());
        return ResponseEntity.ok(maletines);
    }

    @GetMapping("/fecha-envio")
    public ResponseEntity<List<Maletin>> getMaletinesByFechaEnvioBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<Maletin> maletines = maletinService.findByFechaEnvioBetween(inicio, fin);
        return ResponseEntity.ok(maletines);
    }

    @PostMapping
    public ResponseEntity<Maletin> createMaletin(@RequestBody Maletin maletin) {
        Maletin nuevoMaletin = maletinService.save(maletin);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMaletin);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Maletin> updateMaletin(@PathVariable Long id, @RequestBody Maletin maletin) {
        if (!maletinService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        maletin.setId(id);
        return ResponseEntity.ok(maletinService.save(maletin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaletin(@PathVariable Long id) {
        if (!maletinService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        maletinService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/registrar-entrega")
    public ResponseEntity<Maletin> registrarEntrega(@PathVariable Long id) {
        Maletin maletin = maletinService.registrarEntrega(id);
        if (maletin != null) {
            return ResponseEntity.ok(maletin);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Maletin>> getMaletinesByEstado(@PathVariable EstadoMaletin estado) {
        List<Maletin> maletines = maletinService.findByEstado(estado);
        return ResponseEntity.ok(maletines);
    }
    
    @GetMapping("/filtrar")
    public ResponseEntity<List<Maletin>> filtrarMaletines(
            @RequestParam(required = false) String cliente,
            @RequestParam(required = false) String sucursal,
            @RequestParam(required = false) String cajero,
            @RequestParam(required = false) EstadoMaletin estado) {
        List<Maletin> maletines = maletinService.filtrarMaletines(cliente, sucursal, cajero, estado);
        return ResponseEntity.ok(maletines);
    }
    
    @PatchMapping(value = "/{id}/cambiar-estado", consumes = {"application/json", "text/plain"})
    public ResponseEntity<Maletin> cambiarEstadoPatch(@PathVariable Long id, @RequestBody String nuevoEstado) {
        return cambiarEstadoInterno(id, nuevoEstado);
    }
    
    @PutMapping(value = "/{id}/cambiar-estado", consumes = {"application/json", "text/plain"})
    public ResponseEntity<Maletin> cambiarEstadoPut(@PathVariable Long id, @RequestBody String nuevoEstado) {
        return cambiarEstadoInterno(id, nuevoEstado);
    }
    
    private ResponseEntity<Maletin> cambiarEstadoInterno(Long id, String nuevoEstado) {
        try {
            System.out.println("DEBUG: Valor recibido: '" + nuevoEstado + "'");
            // Remover comillas si están presentes
            String estadoLimpio = nuevoEstado.replaceAll("\"", "");
            System.out.println("DEBUG: Valor limpio: '" + estadoLimpio + "'");
            EstadoMaletin estado = EstadoMaletin.valueOf(estadoLimpio);
            Maletin maletin = maletinService.cambiarEstado(id, estado);
            if (maletin != null) {
                return ResponseEntity.ok(maletin);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: No se pudo convertir '" + nuevoEstado + "' a EstadoMaletin. Error: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}