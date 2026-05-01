package com.julian.spring_demo.controller;

import com.julian.spring_demo.dto.TagRequestDTO;
import com.julian.spring_demo.dto.TagResponseDTO;
import com.julian.spring_demo.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.DescriptorKey;
import java.util.List;

@RestController
@RequestMapping ("/api/tags")
@Tag (name = "Tags", description = "Tag management API")
public class TagController {

    private final TagService tagService;

    public TagController (TagService tagService) {
        this.tagService = tagService;
    }

    @Operation (summary = "Get all tags")
    @GetMapping
    public ResponseEntity<List<TagResponseDTO>> getAll () {
        return ResponseEntity.ok(tagService.getAll());
    }

    @Operation (summary = "Get tag by ID")
    @GetMapping ("/{id}")
    public ResponseEntity<TagResponseDTO> getById (@PathVariable Long id) {
        return ResponseEntity.ok(tagService.getById(id));
    }

    @Operation (summary = "Create a new tag")
    @PostMapping
    public ResponseEntity<TagResponseDTO> create (@Valid @RequestBody TagRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.create(dto));
    }

    @Operation (summary = "Delete a tag")
    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
