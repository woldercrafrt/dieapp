package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "discos")
public class Disco {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String estado;
    
    @Column(name = "horas_uso")
    private Integer horasUso;
    
    // Constructors
    public Disco() {}
    
    public Disco(Long id, String estado, Integer horasUso) {
        this.id = id;
        this.estado = estado;
        this.horasUso = horasUso;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public Integer getHorasUso() {
        return horasUso;
    }
    
    public void setHorasUso(Integer horasUso) {
        this.horasUso = horasUso;
    }
}