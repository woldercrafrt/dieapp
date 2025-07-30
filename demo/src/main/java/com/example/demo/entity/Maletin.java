package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "maletines")
public class Maletin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String cliente;
    
    @Column(nullable = false)
    private String cajero;
    
    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;
    
    @Column(name = "id_disco", nullable = false)
    private Long idDisco;
    
    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;
    
    // Constructores
    public Maletin() {}
    
    public Maletin(Long id, String cliente, String cajero, LocalDateTime fechaEnvio, Long idDisco, LocalDateTime fechaEntrega) {
        this.id = id;
        this.cliente = cliente;
        this.cajero = cajero;
        this.fechaEnvio = fechaEnvio;
        this.idDisco = idDisco;
        this.fechaEntrega = fechaEntrega;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCliente() {
        return cliente;
    }
    
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
    
    public String getCajero() {
        return cajero;
    }
    
    public void setCajero(String cajero) {
        this.cajero = cajero;
    }
    
    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }
    
    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
    
    public Long getIdDisco() {
        return idDisco;
    }
    
    public void setIdDisco(Long idDisco) {
        this.idDisco = idDisco;
    }
    
    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }
    
    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
} 