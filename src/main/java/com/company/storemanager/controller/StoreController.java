package com.company.storemanager.controller;

import com.company.storemanager.dto.StoreResponseDTO;
import com.company.storemanager.model.Store;
import com.company.storemanager.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping
    public List<StoreResponseDTO> getAllStores() {
        return storeService.getAllActiveStores();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDTO> getStoreById(@PathVariable Long id) {
        Optional<StoreResponseDTO> store = storeService.getStoreById(id);
        return store.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/province/{provinceName}")
    public List<StoreResponseDTO> getStoresByProvinceName(@PathVariable String provinceName) {
        return storeService.getStoresByProvinceName(provinceName);
    }

    @GetMapping("/whitelisted")
    public List<StoreResponseDTO> getWhitelistedStores() {
        return storeService.getWhitelistedStores();
    }

    @PostMapping
    public Store createStore(@RequestBody Store store) {
        return storeService.createStore(store);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Store> updateStore(@PathVariable Long id, @RequestBody Store storeDetails) {
        Store updatedStore = storeService.updateStore(id, storeDetails);
        if (updatedStore != null) {
            return ResponseEntity.ok(updatedStore);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable Long id) {
        boolean deleted = storeService.deleteStore(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}