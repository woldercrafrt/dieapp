package com.example.demo.repository;

import com.example.demo.entity.Disco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscoRepository extends JpaRepository<Disco, Long> {
    List<Disco> findByEstado(String estado);
}