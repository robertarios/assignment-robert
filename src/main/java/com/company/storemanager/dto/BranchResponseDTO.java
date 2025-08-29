package com.company.storemanager.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BranchResponseDTO {
    private Long id;
    private String name;
    private String code;
    private ProvinceResponseDTO province;
    private Boolean isActive;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}