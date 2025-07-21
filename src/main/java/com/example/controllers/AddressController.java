package com.example.controllers;


import com.example.dtos.AddressDto;
import com.example.services.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/addresses")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping(value = {"/", ""})
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        return ResponseEntity.ok(addressService.allAddress());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressDto> createAddresses(@Valid @RequestBody AddressDto addressDto) {
        AddressDto createdAddress = addressService.createAddress(addressDto);
        return ResponseEntity.created(URI.create("/" + addressDto.getId())).body(createdAddress);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressDto> getAddresses(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getAddress(id));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressDto> updateAddresses(@PathVariable Long id, @Valid @RequestBody AddressDto addressDto) {
        return ResponseEntity.ok(addressService.updateAddress(id, addressDto));
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressDto> patchAddresses(@PathVariable Long id, @RequestBody AddressDto addressDto) {
        return ResponseEntity.ok(addressService.patchAddress(id, addressDto));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressDto> deleteAddresses(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.deleteAddress(id));
    }
}
