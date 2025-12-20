package com.spring.memory.controller;

import com.spring.memory.dto.tag.CreateTagDTO;
import com.spring.memory.dto.tag.TagDTO;
import com.spring.memory.dto.tag.UpdateTagDTO;
import com.spring.memory.entity.Tag;
import com.spring.memory.dto.DeleteResponseDTO;
import com.spring.memory.service.TagService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/create")
    public ResponseEntity<TagDTO> createTag(@Valid @RequestBody CreateTagDTO req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tagService.createTag(req));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TagDTO> updateTag(@PathVariable String id, @Valid @RequestBody UpdateTagDTO req) {
        return ResponseEntity.ok(tagService.updateTag(id, req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getTagById(@PathVariable String id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TagDTO>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DeleteResponseDTO<TagDTO>> deleteTag(@PathVariable String id) {
        DeleteResponseDTO<TagDTO> response = tagService.deleteTagById(id);
        return ResponseEntity.ok(response);
    }
}
