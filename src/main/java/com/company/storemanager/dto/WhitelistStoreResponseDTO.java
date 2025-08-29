package com.company.storemanager.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WhitelistStoreResponseDTO {
    private Long id;
    private StoreResponseDTO store;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}