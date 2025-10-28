package com.nphuonha.servletkit.models;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
@EqualsAndHashCode
public abstract class BaseModel {
    @Expose
    private Long id;
    @Expose
    private LocalDateTime createdAt;
    @Expose
    private LocalDateTime updatedAt;
    
    /**
     * Convert createdAt to java.util.Date for JSP compatibility
     */
    public Date getCreatedAtAsDate() {
        if (createdAt == null) {
            return null;
        }
        return Date.from(createdAt.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * Convert updatedAt to java.util.Date for JSP compatibility
     */
    public Date getUpdatedAtAsDate() {
        if (updatedAt == null) {
            return null;
        }
        return Date.from(updatedAt.atZone(ZoneId.systemDefault()).toInstant());
    }
}