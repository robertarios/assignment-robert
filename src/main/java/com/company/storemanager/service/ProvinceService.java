package com.company.storemanager.service;

import com.company.storemanager.dto.ProvinceResponseDTO;
import com.company.storemanager.mapper.StoreMapper;
import com.company.storemanager.model.Province;
import com.company.storemanager.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProvinceService {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private StoreMapper storeMapper;

    public List<ProvinceResponseDTO> getAllActiveProvinces() {
        List<Province> provinces = provinceRepository.findByIsActiveTrue();
        return provinces.stream()
                .map(storeMapper::toProvinceResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProvinceResponseDTO> getProvinceById(Long id) {
        return provinceRepository.findByIdAndIsActiveTrue(id)
                .map(storeMapper::toProvinceResponseDTO);
    }

    public Optional<ProvinceResponseDTO> getProvinceByName(String name) {
        return provinceRepository.findByNameAndIsActiveTrue(name)
                .map(storeMapper::toProvinceResponseDTO);
    }

    public Province createProvince(Province province) {
        province.setIsActive(true);
        return provinceRepository.save(province);
    }

    @Transactional
    public Province updateProvince(Long id, Province provinceDetails) {
        Optional<Province> optionalProvince = provinceRepository.findByIdAndIsActiveTrue(id);
        if (optionalProvince.isPresent()) {
            Province province = optionalProvince.get();
            province.setName(provinceDetails.getName());
            return provinceRepository.save(province);
        }
        return null;
    }

    @Transactional
    public boolean deleteProvince(Long id) {
        Optional<Province> optionalProvince = provinceRepository.findByIdAndIsActiveTrue(id);
        if (optionalProvince.isPresent()) {
            Province province = optionalProvince.get();
            province.setIsActive(false);
            provinceRepository.save(province);
            return true;
        }
        return false;
    }
}