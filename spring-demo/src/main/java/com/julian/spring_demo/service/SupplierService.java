package com.julian.spring_demo.service;

import com.julian.spring_demo.dto.ProductResponseDTO;
import com.julian.spring_demo.dto.SupplierRequestDTO;
import com.julian.spring_demo.dto.SupplierResponseDTO;

import com.julian.spring_demo.exception.SupplierNotFoundException;

import com.julian.spring_demo.model.Supplier;
import com.julian.spring_demo.model.Tag;
import com.julian.spring_demo.repository.SupplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private static final Logger log = LoggerFactory.getLogger(SupplierService.class);

    public SupplierService (SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional (readOnly = true)
    public Page<SupplierResponseDTO> getAll (String name, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy.trim()));
        Page<Supplier> suppliers;
        if (name != null) {
            suppliers = supplierRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            suppliers = supplierRepository.findAll(pageable);
        }
        return suppliers.map(this::toDTO);
    }

    @Transactional (readOnly = true)
    public SupplierResponseDTO getById (Long id) {
        return toDTO(supplierRepository.findById(id).orElseThrow(() -> new SupplierNotFoundException(id)));
    }

    @Transactional
    public SupplierResponseDTO create (SupplierRequestDTO dto) {
        Supplier supplier = new Supplier();
        supplier.setName(dto.getName());
        supplier.setEmail(dto.getEmail());
        supplier.setPhone(dto.getPhone());

        if (supplierRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Supplier with this email already exists");
        }
        return toDTO(supplierRepository.save(supplier));
    }

    @Transactional
    public SupplierResponseDTO update (Long id, SupplierRequestDTO dto) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException(id));

        supplier.setName(dto.getName());
        supplier.setEmail(dto.getEmail());
        supplier.setPhone(dto.getPhone());

        return toDTO(supplierRepository.save(supplier));
    }

    @Transactional
    public void delete (Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException(id));
        supplier.setDeleted(true);
        supplierRepository.save(supplier);
        log.info("Supplier soft deleted with id: {}", id);
    }

    @Transactional (readOnly = true)
    public List<ProductResponseDTO> getProductsBySupplier (Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException(id));

        return supplier.getProducts().stream()
                .map(product -> new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getTags().stream().map(Tag::getName).collect(Collectors.toSet()), product.getCreatedAt(), product.getUpdatedAt()
        ))
                .collect(Collectors.toList());
    }

    private SupplierResponseDTO toDTO (Supplier supplier) {
        return new SupplierResponseDTO(
                supplier.getId(),
                supplier.getName(),
                supplier.getEmail(),
                supplier.getPhone(),
                supplier.getProducts().size()
        );
    }
}
