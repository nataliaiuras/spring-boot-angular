package com.example.controllers;

import com.example.dtos.AddressDto;
import com.example.dtos.overview.AddressOverviewDto;
import com.example.services.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/addresses")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping(value = {"/", ""})
    public ResponseEntity<Set<AddressOverviewDto>> getAllAddresses() {
        return ResponseEntity.ok(addressService.allAddress());
    }

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody AddressDto addressDto) {
        AddressDto createdAddress = addressService.createAddress(addressDto);
        return ResponseEntity.created(URI.create("/" + addressDto.getId())).body(createdAddress);
    }

    @GetMapping("{id}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getAddress(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressDto addressDto) {
        return ResponseEntity.ok(addressService.updateAddress(id, addressDto));
    }

    @PatchMapping("{id}")
    public ResponseEntity<AddressDto> patchAddress(@PathVariable Long id, @RequestBody AddressDto addressDto) {
        return ResponseEntity.ok(addressService.patchAddress(id, addressDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<AddressDto> deleteAddress(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.deleteAddress(id));
    }


}
