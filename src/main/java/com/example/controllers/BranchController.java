package com.example.controllers;

import com.example.dtos.BranchDto;
import com.example.dtos.overview.BranchOverviewDto;
import com.example.services.BranchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/branches")
public class BranchController {
    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping(value = {"/", ""})
    public ResponseEntity<Set<BranchOverviewDto>> getAllBranches() {
        return ResponseEntity.ok(branchService.allBranches());
    }

    @PostMapping
    public ResponseEntity<BranchDto> createBranch(@Valid @RequestBody BranchDto branchDto) {
        BranchDto createdBranch = branchService.createBranch(branchDto);
        return ResponseEntity.created(URI.create("/" + branchDto.getId())).body(createdBranch);
    }

    @GetMapping("{id}")
    public ResponseEntity<BranchDto> getBranch(@PathVariable Long id) {
        return ResponseEntity.ok(branchService.getBranch(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<BranchDto> updateBranch(@PathVariable Long id, @Valid @RequestBody BranchDto branchDto) {
        return ResponseEntity.ok(branchService.updateBranch(id, branchDto));
    }

    @PatchMapping("{id}")
    public ResponseEntity<BranchDto> patchBranch(@PathVariable Long id, @RequestBody BranchDto branchDto) {
        return ResponseEntity.ok(branchService.patchBranch(id, branchDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{branchId}/customer/{customerId}")
    public ResponseEntity<BranchDto> addCustomerToBranch(@PathVariable Long branchId, @PathVariable Long customerId) {
        BranchDto updatedBranch = branchService.addCustomerToBranch(branchId, customerId);
        return ResponseEntity.ok(updatedBranch);
    }

    @DeleteMapping("/{branchId}/customer/{customerId}")
    public ResponseEntity<BranchDto> removeCustomerFromBranch(@PathVariable Long branchId, @PathVariable Long customerId) {
        BranchDto updatedBranch = branchService.removeCustomerFromBranch(branchId, customerId);
        return ResponseEntity.ok(updatedBranch);
    }

    @PostMapping("/{branchId}/address/{addressId}")
    public ResponseEntity<BranchDto> setAddressToBranch(@PathVariable Long branchId, @PathVariable Long addressId) {
        BranchDto updatedBranch = branchService.setAddressToBranch(branchId, addressId);
        return ResponseEntity.ok(updatedBranch);
    }

    @DeleteMapping("/{branchId}/address/{addressId}")
    public ResponseEntity<BranchDto> removeAddressFromBranch(@PathVariable Long branchId, @PathVariable Long addressId) {
        BranchDto updatedBranch = branchService.removeAddressFromBranch(branchId, addressId);
        return ResponseEntity.ok(updatedBranch);
    }


}