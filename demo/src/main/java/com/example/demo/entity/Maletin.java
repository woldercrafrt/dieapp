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
    
    @Column(nullable = false)
    private String sucursal;
    
    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;
    
    @ManyToOne
    @JoinColumn(name = "id_disco", nullable = false)
    private Disco disco;
    
    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoMaletin estado = EstadoMaletin.SIN_ENVIAR;
    
    @PrePersist
    protected void onCreate() {
        fechaEnvio = LocalDateTime.now();
        if (estado == null) {
            estado = EstadoMaletin.SIN_ENVIAR;
        }
    }
    
    // Constructors
    public Maletin() {}
    
    public Maletin(Long id, String cliente, String cajero, String sucursal, LocalDateTime fechaEnvio, 
                 Disco disco, LocalDateTime fechaEntrega, EstadoMaletin estado) {
        this.id = id;
        this.cliente = cliente;
        this.cajero = cajero;
        this.sucursal = sucursal;
        this.fechaEnvio = fechaEnvio;
        this.disco = disco;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado != null ? estado : EstadoMaletin.SIN_ENVIAR;
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
    
    public String getSucursal() {
        return sucursal;
    }
    
    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
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
    
    public EstadoMaletin getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoMaletin estado) {
        this.estado = estado;
    }
}