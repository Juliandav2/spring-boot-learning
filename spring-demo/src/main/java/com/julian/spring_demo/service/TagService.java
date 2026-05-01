package com.julian.spring_demo.service;

import com.julian.spring_demo.dto.TagRequestDTO;
import com.julian.spring_demo.dto.TagResponseDTO;
import com.julian.spring_demo.exception.TagNotFoundException;
import com.julian.spring_demo.model.Tag;
import com.julian.spring_demo.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService (TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    private  TagResponseDTO toDTO (Tag tag) {
        return new TagResponseDTO(
                tag.getId(),
                tag.getName(),
                tag.getProducts().size()
        );
    }

    @Transactional (readOnly = true)
    public List<TagResponseDTO> getAll () {
        return tagRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional (readOnly = true)
    public TagResponseDTO getById (Long id) {
        return toDTO(tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id)));
    }

    @Transactional
    public TagResponseDTO create (TagRequestDTO dto) {
        if (tagRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Tag already exists: " + dto.getName());
        }
        Tag tag = new Tag();
        tag.setName(dto.getName());
        return toDTO(tagRepository.save(tag));
    }

    @Transactional
    public void delete (Long id) {
        if (!tagRepository.existsById(id)) {
            throw new TagNotFoundException(id);
        }
        tagRepository.deleteById(id);
    }
}
