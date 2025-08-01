package com.example.demo.entity;

public enum EstadoMaletin {
    SIN_ENVIAR("Sin enviar"),
    ENVIADO("Enviado"),
    ENVIADO_SIN_RESPUESTA("Enviado sin respuesta"),
    RECIBIDO("Recibido"),
    PROBLEMAS("Problemas");
    
    private final String descripcion;
    
    EstadoMaletin(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}