package com.company.storemanager.controller;

import com.company.storemanager.dto.WhitelistStoreResponseDTO;
import com.company.storemanager.service.WhitelistStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/whitelist-stores")
public class WhitelistStoreController {

    @Autowired
    private WhitelistStoreService whitelistStoreService;

    @GetMapping
    public List<WhitelistStoreResponseDTO> getAllWhitelistStores() {
        return whitelistStoreService.getAllActiveWhitelistStores();
    }

    @PostMapping("/{storeId}")
    public ResponseEntity<WhitelistStoreResponseDTO> addStoreToWhitelist(@PathVariable Long storeId) {
        WhitelistStoreResponseDTO whitelistStore = whitelistStoreService.addStoreToWhitelist(storeId);
        if (whitelistStore != null) {
            return ResponseEntity.ok(whitelistStore);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeStoreFromWhitelist(@PathVariable Long id) {
        boolean removed = whitelistStoreService.removeStoreFromWhitelist(id);
        if (removed) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}