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
    
    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;
    
    @ManyToOne
    @JoinColumn(name = "id_disco", nullable = false)
    private Disco disco;
    
    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;
    
    @PrePersist
    protected void onCreate() {
        fechaEnvio = LocalDateTime.now();
    }
    
    // Constructors
    public Maletin() {}
    
    public Maletin(Long id, String cliente, String cajero, LocalDateTime fechaEnvio, 
                 Disco disco, LocalDateTime fechaEntrega) {
        this.id = id;
        this.cliente = cliente;
        this.cajero = cajero;
        this.fechaEnvio = fechaEnvio;
        this.disco = disco;
        this.fechaEntrega = fechaEntrega;
    }
    
    // Getters and Setters
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
    
    public Disco getDisco() {
        return disco;
    }
    
    public void setDisco(Disco disco) {
        this.disco = disco;
    }
    
    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }
    
    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
}