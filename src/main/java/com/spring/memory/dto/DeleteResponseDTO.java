package com.spring.memory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DeleteResponseDTO<T> {
    private String message;
    private T data;
}
