package com.company.storemanager.service;

import com.company.storemanager.dto.BranchResponseDTO;
import com.company.storemanager.mapper.StoreMapper;
import com.company.storemanager.model.Branch;
import com.company.storemanager.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private StoreMapper storeMapper;

    public List<BranchResponseDTO> getAllActiveBranches() {
        List<Branch> branches = branchRepository.findByIsActiveTrueAndIsDeletedFalse();
        return branches.stream()
                .map(storeMapper::toBranchResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<BranchResponseDTO> getBranchById(Long id) {
        return branchRepository.findByIdAndIsActiveTrueAndIsDeletedFalse(id)
                .map(storeMapper::toBranchResponseDTO);
    }

    public Branch createBranch(Branch branch) {
        return branchRepository.save(branch);
    }

    @Transactional
    public Branch updateBranch(Long id, Branch branchDetails) {
        Optional<Branch> optionalBranch = branchRepository.findByIdAndIsActiveTrueAndIsDeletedFalse(id);
        if (optionalBranch.isPresent()) {
            Branch branch = optionalBranch.get();
            branch.setName(branchDetails.getName());
            branch.setCode(branchDetails.getCode());
            branch.setProvince(branchDetails.getProvince());
            return branchRepository.save(branch);
        }
        return null;
    }

    @Transactional
    public boolean deleteBranch(Long id) {
        Optional<Branch> optionalBranch = branchRepository.findByIdAndIsActiveTrueAndIsDeletedFalse(id);
        if (optionalBranch.isPresent()) {
            branchRepository.softDelete(id);
            return true;
        }
        return false;
    }
}