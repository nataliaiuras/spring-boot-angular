package com.example.controllers;

import com.example.dtos.AddressDto;
import com.example.dtos.overview.AddressOverviewDto;
import com.example.dtos.overview.BranchOverviewDto;
import com.example.dtos.response.ApiResponse;
import com.example.services.AddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/addresses")
@AllArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AddressOverviewDto>>> getAllAddresses(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<AddressOverviewDto> pagedAddress = addressService.getAllAddresses(pageable);
        return ResponseEntity.ok(ApiResponse.success(pagedAddress));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<AddressOverviewDto>> getAddress(@PathVariable Long id) {
        AddressOverviewDto addressDto = addressService.getAddressById(id);
        return ResponseEntity.ok(ApiResponse.success(addressDto));
    }

    @GetMapping("{id}/branch")
    public ResponseEntity<ApiResponse<BranchOverviewDto>> getBranch(@PathVariable Long id) {
        BranchOverviewDto branch = addressService.getBranchByAddressId(id);
        return ResponseEntity.ok(ApiResponse.success(branch));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressDto>> createAddress(@Valid @RequestBody AddressDto addressDto) {
        AddressDto createdAddress = addressService.createAddress(addressDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdAddress.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(ApiResponse.success(createdAddress, "Address created successfully"));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<AddressDto>> updateAddress(@PathVariable Long id,
                                                                 @Valid @RequestBody AddressDto addressDto) {
        AddressDto updateAddress = addressService.updateAddress(id, addressDto);
        return ResponseEntity.ok(ApiResponse.success(updateAddress, "Address updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok(ApiResponse.success("Address deleted successfully"));
    }


}
