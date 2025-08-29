package com.company.storemanager.controller;

import com.company.storemanager.dto.BranchResponseDTO;
import com.company.storemanager.model.Branch;
import com.company.storemanager.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @GetMapping
    public List<BranchResponseDTO> getAllBranches() {
        return branchService.getAllActiveBranches();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponseDTO> getBranchById(@PathVariable Long id) {
        Optional<BranchResponseDTO> branch = branchService.getBranchById(id);
        return branch.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Branch createBranch(@RequestBody Branch branch) {
        return branchService.createBranch(branch);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Branch> updateBranch(@PathVariable Long id, @RequestBody Branch branchDetails) {
        Branch updatedBranch = branchService.updateBranch(id, branchDetails);
        if (updatedBranch != null) {
            return ResponseEntity.ok(updatedBranch);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBranch(@PathVariable Long id) {
        boolean deleted = branchService.deleteBranch(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}