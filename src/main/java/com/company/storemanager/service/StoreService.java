package com.company.storemanager.service;

import com.company.storemanager.dto.StoreResponseDTO;
import com.company.storemanager.mapper.StoreMapper;
import com.company.storemanager.model.Store;
import com.company.storemanager.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreMapper storeMapper;

    public List<StoreResponseDTO> getAllActiveStores() {
        List<Store> stores = storeRepository.findByIsActiveTrueAndIsDeletedFalse();
        return storeMapper.toStoreResponseDTOList(stores);
    }

    public Optional<StoreResponseDTO> getStoreById(Long id) {
        return storeRepository.findByIdAndIsActiveTrueAndIsDeletedFalse(id)
                .map(storeMapper::toStoreResponseDTO);
    }

    public List<StoreResponseDTO> getStoresByProvinceName(String provinceName) {
        List<Store> stores = storeRepository.findByProvinceName(provinceName);
        return storeMapper.toStoreResponseDTOList(stores);
    }

    public List<StoreResponseDTO> getWhitelistedStores() {
        List<Store> stores = storeRepository.findWhitelistedStores();
        List<StoreResponseDTO> responseDTOs = storeMapper.toStoreResponseDTOList(stores);
        
        // Force set isWhitelisted to true for whitelisted stores
        responseDTOs.forEach(dto -> dto.setIsWhitelisted(true));
        
        return responseDTOs;
    }

    public Store createStore(Store store) {
        return storeRepository.save(store);
    }

    public Store updateStore(Long id, Store storeDetails) {
        Optional<Store> optionalStore = storeRepository.findByIdAndIsActiveTrueAndIsDeletedFalse(id);
        if (optionalStore.isPresent()) {
            Store store = optionalStore.get();
            store.setName(storeDetails.getName());
            store.setCode(storeDetails.getCode());
            store.setAddress(storeDetails.getAddress());
            store.setBranch(storeDetails.getBranch());
            return storeRepository.save(store);
        }
        return null;
    }

    public boolean deleteStore(Long id) {
        Optional<Store> optionalStore = storeRepository.findByIdAndIsActiveTrueAndIsDeletedFalse(id);
        if (optionalStore.isPresent()) {
            Store store = optionalStore.get();
            store.setIsActive(false);
            store.setIsDeleted(true);
            storeRepository.save(store);
            return true;
        }
        return false;
    }
}