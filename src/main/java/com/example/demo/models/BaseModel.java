package com.example.demo.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
public abstract class BaseModel {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}