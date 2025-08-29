package com.company.storemanager.service;

import com.company.storemanager.dto.WhitelistStoreResponseDTO;
import com.company.storemanager.mapper.StoreMapper;
import com.company.storemanager.model.WhitelistStore;
import com.company.storemanager.repository.StoreRepository;
import com.company.storemanager.repository.WhitelistStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WhitelistStoreService {

    @Autowired
    private WhitelistStoreRepository whitelistStoreRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreMapper storeMapper;

    public List<WhitelistStoreResponseDTO> getAllActiveWhitelistStores() {
        List<WhitelistStore> whitelistStores = whitelistStoreRepository.findByIsActiveTrue();
        return whitelistStores.stream()
                .map(this::toWhitelistStoreResponseDTO)
                .collect(Collectors.toList());
    }

    public WhitelistStoreResponseDTO addStoreToWhitelist(Long storeId) {
        Optional<WhitelistStore> existing = whitelistStoreRepository.findByStoreIdAndIsActiveTrue(storeId);
        if (existing.isPresent()) {
            return toWhitelistStoreResponseDTO(existing.get());
        }

        return storeRepository.findByIdAndIsActiveTrueAndIsDeletedFalse(storeId)
                .map(store -> {
                    WhitelistStore whitelistStore = new WhitelistStore();
                    whitelistStore.setStore(store);
                    whitelistStore.setIsActive(true);
                    WhitelistStore saved = whitelistStoreRepository.save(whitelistStore);
                    return toWhitelistStoreResponseDTO(saved);
                })
                .orElse(null);
    }

    public boolean removeStoreFromWhitelist(Long id) {
        Optional<WhitelistStore> optionalWhitelistStore = whitelistStoreRepository.findById(id);
        if (optionalWhitelistStore.isPresent() && optionalWhitelistStore.get().getIsActive()) {
            whitelistStoreRepository.deactivate(id);
            return true;
        }
        return false;
    }

    private WhitelistStoreResponseDTO toWhitelistStoreResponseDTO(WhitelistStore whitelistStore) {
        WhitelistStoreResponseDTO dto = new WhitelistStoreResponseDTO();
        dto.setId(whitelistStore.getId());
        dto.setStore(storeMapper.toStoreResponseDTO(whitelistStore.getStore()));
        dto.setIsActive(whitelistStore.getIsActive());
        dto.setCreatedAt(whitelistStore.getCreatedAt());
        dto.setUpdatedAt(whitelistStore.getUpdatedAt());
        return dto;
    }
}