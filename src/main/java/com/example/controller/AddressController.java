package com.example.controller;

import com.example.dto.request.address.AddressCreateRequest;
import com.example.dto.request.address.AddressUpdateRequest;
import com.example.dto.response.address.AddressDetailResponse;
import com.example.dto.response.address.AddressResponse;
import com.example.service.AddressService;
import com.example.dto.response.general.ApiResponse;
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
    public ResponseEntity<ApiResponse<Page<AddressResponse>>> getAllAddresses(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<AddressResponse> responsePage = addressService.getAllAddresses(pageable);
        return ResponseEntity.ok(ApiResponse.success(responsePage));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> getAddress(@PathVariable Long id) {
        AddressResponse response = addressService.getAddressById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressDetailResponse>> createAddress(
            @Valid @RequestBody AddressCreateRequest request) {
        AddressDetailResponse addressDetailResponse = addressService.createAddress(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addressDetailResponse.id())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(ApiResponse.success(addressDetailResponse, "Address created successfully"));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<AddressDetailResponse>> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressUpdateRequest request) {
        AddressDetailResponse response = addressService.updateAddress(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Address updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok(ApiResponse.success("Address deleted successfully"));
    }


}
