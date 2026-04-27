package com.julian.spring_demo.controller;

import com.julian.spring_demo.dto.ProductResponseDTO;
import com.julian.spring_demo.dto.SupplierRequestDTO;
import com.julian.spring_demo.dto.SupplierResponseDTO;
import com.julian.spring_demo.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/api/suppliers")
@Tag (name = "Suppliers", description = "Supplier management API")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController (SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Operation (summary = "Get all suppliers", description = "Returns paginated of suppliers with optional filters")
    @GetMapping
    public ResponseEntity<Page<SupplierResponseDTO>> getAll (@RequestParam (required = false) String name,
                                                             @RequestParam (defaultValue = "0") int page,
                                                             @RequestParam (defaultValue =  "10") int size,
                                                             @RequestParam (defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(supplierService.getAll(name, page, size, sortBy));
    }

    @Operation (summary = "Get supplier by ID")
    @GetMapping ("/{id}")
    public ResponseEntity<SupplierResponseDTO> getById (@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getById(id));
    }

    @Operation (summary = "Create a new supplier")
    @PostMapping
    public ResponseEntity<SupplierResponseDTO> create (@Valid @RequestBody SupplierRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.create(dto));
    }

    @Operation (summary = "Update a supplier")
    @PutMapping ("/{id}")
    public ResponseEntity<SupplierResponseDTO> update (@PathVariable Long id,
                                                       @Valid @RequestBody SupplierRequestDTO dto) {
        return ResponseEntity.ok(supplierService.update(id, dto));
    }

    @Operation (summary = "Delete a supplier")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        supplierService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping ("/{id}/products")
    public ResponseEntity<List<ProductResponseDTO>> getProductsBySupplier (@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getProductsBySupplier(id));
    }
}
