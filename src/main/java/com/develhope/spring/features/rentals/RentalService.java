package com.develhope.spring.features.rentals;

import com.develhope.spring.features.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
}
