package com.spring.memory.enumeration;

public enum MemoryStatus {
    DRAFT("DRAFT"),
    SAVED("SAVED");
   private final String status;
    MemoryStatus(String status) {
        this.status = status;
    }
}
