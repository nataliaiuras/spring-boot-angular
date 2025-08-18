package com.example.controllers;

import com.example.dtos.BranchDto;
import com.example.dtos.overview.BranchOverviewDto;
import com.example.services.BranchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/branches")
public class BranchController {
    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping(value = {"/", ""})
    public ResponseEntity<List<BranchOverviewDto>> getAllBranches() {
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
    public ResponseEntity<BranchDto> deleteBranch(@PathVariable Long id) {
        return ResponseEntity.ok(branchService.deleteBranch(id));
    }
}