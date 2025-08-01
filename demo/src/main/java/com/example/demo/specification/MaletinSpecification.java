package com.example.demo.specification;

import com.example.demo.entity.Maletin;
import com.example.demo.entity.EstadoMaletin;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class MaletinSpecification {

    public static Specification<Maletin> filtrarPorParametros(
            String cliente, String sucursal, String cajero, EstadoMaletin estado) {
        
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (cliente != null && !cliente.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("cliente")), 
                    "%" + cliente.toLowerCase() + "%"
                ));
            }
            
            if (sucursal != null && !sucursal.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("sucursal")), 
                    "%" + sucursal.toLowerCase() + "%"
                ));
            }
            
            if (cajero != null && !cajero.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("cajero")), 
                    "%" + cajero.toLowerCase() + "%"
                ));
            }
            
            if (estado != null) {
                predicates.add(criteriaBuilder.equal(root.get("estado"), estado));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}