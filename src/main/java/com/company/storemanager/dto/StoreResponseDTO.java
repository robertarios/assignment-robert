package com.company.storemanager.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StoreResponseDTO {
    private Long id;
    private String name;
    private String code;
    private String address;
    private BranchResponseDTO branch;
    private Boolean isActive;
    private Boolean isDeleted;
    private Boolean isWhitelisted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}