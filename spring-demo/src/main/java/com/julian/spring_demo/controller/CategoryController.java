package com.julian.spring_demo.controller;

import com.julian.spring_demo.model.Category;
import com.julian.spring_demo.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/api/categories")
@Tag (name = "Categories", description = "Category management API")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController (CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Get all categories", description = "Returns paginated list of categories with optional filters")
    @GetMapping
    public ResponseEntity<List<Category>> getAll (@RequestParam (required = false) String name,
                                                  @RequestParam (required = false) String description) {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @Operation(summary = "Get category by ID")
    @GetMapping ("/{id}")
    public ResponseEntity<Category> getById (@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @Operation(summary = "Create a new category")
    @PostMapping
    public ResponseEntity<Category> create (@Valid @RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(category));
    }

    @Operation(summary = "Update a category")
    @PutMapping ("/{id}")
    public ResponseEntity<Category> update (@PathVariable Long id, @Valid @RequestBody Category category) {
        return ResponseEntity.ok(categoryService.update(id, category));
    }

    @Operation(summary = "Delete a category")
    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
