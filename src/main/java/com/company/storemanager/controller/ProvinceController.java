package com.company.storemanager.controller;

import com.company.storemanager.dto.ProvinceResponseDTO;
import com.company.storemanager.model.Province;
import com.company.storemanager.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {

    @Autowired
    private ProvinceService provinceService;

    @GetMapping
    public List<ProvinceResponseDTO> getAllProvinces() {
        return provinceService.getAllActiveProvinces();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProvinceResponseDTO> getProvinceById(@PathVariable Long id) {
        Optional<ProvinceResponseDTO> province = provinceService.getProvinceById(id);
        return province.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ProvinceResponseDTO> getProvinceByName(@PathVariable String name) {
        Optional<ProvinceResponseDTO> province = provinceService.getProvinceByName(name);
        return province.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Province createProvince(@RequestBody Province province) {
        return provinceService.createProvince(province);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Province> updateProvince(@PathVariable Long id, @RequestBody Province provinceDetails) {
        Province updatedProvince = provinceService.updateProvince(id, provinceDetails);
        if (updatedProvince != null) {
            return ResponseEntity.ok(updatedProvince);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProvince(@PathVariable Long id) {
        boolean deleted = provinceService.deleteProvince(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}