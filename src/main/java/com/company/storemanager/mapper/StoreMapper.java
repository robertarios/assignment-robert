package com.company.storemanager.mapper;

import com.company.storemanager.dto.BranchResponseDTO;
import com.company.storemanager.dto.ProvinceResponseDTO;
import com.company.storemanager.dto.StoreResponseDTO;
import com.company.storemanager.dto.WhitelistStoreResponseDTO;
import com.company.storemanager.model.Branch;
import com.company.storemanager.model.Province;
import com.company.storemanager.model.Store;
import com.company.storemanager.model.WhitelistStore;
import com.company.storemanager.repository.WhitelistStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StoreMapper {

    @Autowired
    private WhitelistStoreRepository whitelistStoreRepository;

    public StoreResponseDTO toStoreResponseDTO(Store store) {
        StoreResponseDTO dto = new StoreResponseDTO();
        dto.setId(store.getId());
        dto.setName(store.getName());
        dto.setCode(store.getCode());
        dto.setAddress(store.getAddress());
        dto.setBranch(toBranchResponseDTO(store.getBranch()));
        dto.setIsActive(store.getIsActive());
        dto.setIsDeleted(store.getIsDeleted());
        dto.setIsWhitelisted(isStoreWhitelisted(store.getId()));
        dto.setCreatedAt(store.getCreatedAt());
        dto.setUpdatedAt(store.getUpdatedAt());
        return dto;
    }

    public BranchResponseDTO toBranchResponseDTO(Branch branch) {
        BranchResponseDTO dto = new BranchResponseDTO();
        dto.setId(branch.getId());
        dto.setName(branch.getName());
        dto.setCode(branch.getCode());
        dto.setProvince(toProvinceResponseDTO(branch.getProvince()));
        dto.setIsActive(branch.getIsActive());
        dto.setIsDeleted(branch.getIsDeleted());
        dto.setCreatedAt(branch.getCreatedAt());
        dto.setUpdatedAt(branch.getUpdatedAt());
        return dto;
    }

    public ProvinceResponseDTO toProvinceResponseDTO(Province province) {
        ProvinceResponseDTO dto = new ProvinceResponseDTO();
        dto.setId(province.getId());
        dto.setName(province.getName());
        dto.setIsActive(province.getIsActive());
        return dto;
    }

    public WhitelistStoreResponseDTO toWhitelistStoreResponseDTO(WhitelistStore whitelistStore) {
        WhitelistStoreResponseDTO dto = new WhitelistStoreResponseDTO();
        dto.setId(whitelistStore.getId());
        dto.setStore(toStoreResponseDTO(whitelistStore.getStore()));
        dto.setIsActive(whitelistStore.getIsActive());
        dto.setCreatedAt(whitelistStore.getCreatedAt());
        dto.setUpdatedAt(whitelistStore.getUpdatedAt());
        return dto;
    }

    public List<StoreResponseDTO> toStoreResponseDTOList(List<Store> stores) {
        return stores.stream()
                .map(this::toStoreResponseDTO)
                .collect(Collectors.toList());
    }

    private Boolean isStoreWhitelisted(Long storeId) {
        return whitelistStoreRepository.findByStoreIdAndIsActiveTrue(storeId).isPresent();
    }
}