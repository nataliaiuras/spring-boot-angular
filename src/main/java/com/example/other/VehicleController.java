package com.example.other;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping(value = {"/", ""})
 //   @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<VehicleDto>> allVehicles() {
        return ResponseEntity.ok(vehicleService.allVehicles());
    }

    @PostMapping
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<VehicleDto> createVehicle(@Valid @RequestBody VehicleDto vehicleDto) {
        VehicleDto createdVehicle = vehicleService.createVehicle(vehicleDto);
        return ResponseEntity.created(URI.create("/api/vehicles/" + vehicleDto.getId())).body(createdVehicle);
    }

    @GetMapping("{id}")
 //   @PreAuthorize("hasRole('USER')")
    public ResponseEntity<VehicleDto> getVehicle(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicle(id));
    }

    @PutMapping("{id}")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<VehicleDto> updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleDto vehicleDto) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicleDto));
    }

    @PatchMapping("{id}")
  //  @PreAuthorize("hasRole('USER')")
    public ResponseEntity<VehicleDto> patchVehicle(@PathVariable Long id, @RequestBody VehicleDto vehicleDto) {
        return ResponseEntity.ok(vehicleService.patchVehicle(id, vehicleDto));
    }

    @DeleteMapping("{id}")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<VehicleDto> deleteVehicle(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.deleteVehicle(id));
    }

}
