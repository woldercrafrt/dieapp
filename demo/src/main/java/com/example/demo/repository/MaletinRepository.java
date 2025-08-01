package com.example.demo.repository;

import com.example.demo.entity.Maletin;
import com.example.demo.entity.Disco;
import com.example.demo.entity.EstadoMaletin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MaletinRepository extends JpaRepository<Maletin, Long>, JpaSpecificationExecutor<Maletin> {
    List<Maletin> findByCliente(String cliente);
    List<Maletin> findByCajero(String cajero);
    List<Maletin> findBySucursal(String sucursal);
    List<Maletin> findByDisco(Disco disco);
    List<Maletin> findByFechaEnvioBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Maletin> findByEstado(EstadoMaletin estado);
}