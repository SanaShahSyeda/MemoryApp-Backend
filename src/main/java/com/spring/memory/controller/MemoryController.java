package com.spring.memory.controller;

import com.spring.memory.dto.DeleteResponseDTO;
import com.spring.memory.dto.memory.UpdateMemoryDTO;
import com.spring.memory.dto.memory.CreateMemoryDTO;
import com.spring.memory.dto.memory.MemoryDTO;
import com.spring.memory.entity.CustomUserDetails;
import com.spring.memory.service.MemoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/memories")
public class MemoryController {
    private final MemoryService memoryService;

    public MemoryController(MemoryService memoryService) {
        this.memoryService = memoryService;
    }

    @PostMapping(value = "/create/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MemoryDTO> createMemory(
            @PathVariable Integer boardId,
            @Valid @ModelAttribute CreateMemoryDTO req,
            @AuthenticationPrincipal CustomUserDetails user) {
        System.out.println("Files count: " +
                (req.getMediaFiles() == null ? 0 : req.getMediaFiles().length));
        MemoryDTO dto = memoryService.createMemory(req, user.getId(), boardId);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PatchMapping(value="update/{boardId}/{memoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MemoryDTO> updateMemory(@PathVariable Integer boardId,
            @PathVariable Integer memoryId,
            @AuthenticationPrincipal CustomUserDetails user,
            @ModelAttribute @Valid UpdateMemoryDTO req) {
        MemoryDTO dto = memoryService.updateMemory(boardId, memoryId, user.getId(), req);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("board/all/{id}")
    public ResponseEntity<Page<MemoryDTO>> getAllMemoriesOfBoard(@PathVariable Integer id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Page<MemoryDTO> memoryPage = memoryService.getAllMemoriesOfBoard(id, page, limit);
        return ResponseEntity.ok(memoryPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemoryDTO> getMemoryById(@PathVariable Integer id) {
        MemoryDTO dto = memoryService.getMemoryById(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<DeleteResponseDTO<MemoryDTO>> deleteMemory(@PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails user) {
        MemoryDTO dto = memoryService.deleteMemory(id, user.getId());
        DeleteResponseDTO<MemoryDTO> response = new DeleteResponseDTO<>("Memory deleted successfully", dto);
        return ResponseEntity.ok(response);
    }
}
