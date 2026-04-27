package com.julian.spring_demo.repository;

import com.julian.spring_demo.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    boolean existsByEmail (String email);
    Page<Supplier> findByNameContainingIgnoreCase (String name, Pageable pageable);

}
