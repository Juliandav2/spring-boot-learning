package com.julian.spring_demo.controller;

import com.julian.spring_demo.dto.ProductRequestDTO;
import com.julian.spring_demo.dto.ProductResponseDTO;
import com.julian.spring_demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping ("/api/products")
@Tag (name = "Products", description = "Product management API")
public class ProductController {

    private final ProductService productService;

    public ProductController (ProductService productService) {
        this.productService = productService;
    }

    @Operation (summary = "Get all products", description = "Returns paginated list of products with optional filters")
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAll (@RequestParam (required = false) String name,
                                                            @RequestParam (required = false) Double maxPrice,
                                                            @RequestParam (required = false) Long categoryId,
                                                            @RequestParam (defaultValue = "0") int page,
                                                            @RequestParam (defaultValue = "10") int size,
                                                            @RequestParam (defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy.trim()));
        if (categoryId != null) return ResponseEntity.ok(productService.findByCategoryId(categoryId, pageable));
        return ResponseEntity.ok(productService.getAll(name, maxPrice, page, size, sortBy));
    }

    @Operation (summary = "Get product by ID")
    @GetMapping ("/{id}")
    public ResponseEntity<ProductResponseDTO> getById (@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @Operation(summary = "Create a new product")
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create (@Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(dto));
    }

    @Operation(summary = "Update a product")
    @PutMapping ("/{id}")
    public ResponseEntity<ProductResponseDTO> update (@PathVariable Long id, @Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @Operation(summary = "Delete a product")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation (summary = "Get products by price range")
    @GetMapping ("/price-range")
    public ResponseEntity<List<ProductResponseDTO>> getByPriceRange (@RequestParam double minPrice,
                                                                     @RequestParam double maxPrice) {
        return ResponseEntity.ok(productService.getByPriceRange(minPrice, maxPrice));
    }

    @Operation (summary = "Get low stock products")
    @GetMapping ("/low-stock")
    public ResponseEntity<List<ProductResponseDTO>> getLowStock (@RequestParam (defaultValue = "5") int minStock) {
        return ResponseEntity.ok(productService.getLowStock(minStock));
    }
}
