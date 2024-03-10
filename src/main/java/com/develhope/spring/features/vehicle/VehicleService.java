package com.develhope.spring.features.vehicle;

import com.develhope.spring.exception.NotFoundException;
import com.develhope.spring.exception.UnauthorizedException;
import com.develhope.spring.features.orders.OrderRepository;
import com.develhope.spring.features.users.Role;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.users.UserMapper;
import com.develhope.spring.features.users.UserRepository;
import com.develhope.spring.features.vehicle.PropertiesEnum.FuelType;
import com.develhope.spring.features.vehicle.PropertiesEnum.ShiftType;
import com.develhope.spring.features.vehicle.dto.CreateVehicleRequest;
import com.develhope.spring.features.vehicle.dto.PatchVehicleRequest;
import com.develhope.spring.features.vehicle.dto.VehicleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final OrderRepository orderRepository;


    public ResponseEntity<?> createVehicle(UserEntity user, CreateVehicleRequest createVehicleRequest) {
        if (user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }


        if (createVehicleRequest.getBrand() == null || !StringUtils.hasText(createVehicleRequest.getBrand())) {
            return new ResponseEntity<>("Invalid brand: cannot be empty", HttpStatus.BAD_REQUEST); //brand is required
        }

        if (createVehicleRequest.getModel() == null || !StringUtils.hasText(createVehicleRequest.getModel())) {
            return new ResponseEntity<>("Invalid model: cannot be empty", HttpStatus.BAD_REQUEST); //model is required
        }


        if (createVehicleRequest.getDisplacement() == null || createVehicleRequest.getDisplacement() <= 0) {
            return new ResponseEntity<>("Invalid displacement: cannot be empty", HttpStatus.BAD_REQUEST); //displacement is required
        }

        if (createVehicleRequest.getColor() == null || !StringUtils.hasText(createVehicleRequest.getColor())) {
            return new ResponseEntity<>("Invalid color: cannot be empty", HttpStatus.BAD_REQUEST); //color is required
        }

        if (createVehicleRequest.getPower() == null || createVehicleRequest.getPower() <= 0) {
            return new ResponseEntity<>("Invalid power: cannot be empty", HttpStatus.BAD_REQUEST); //power is required
        }

        if (createVehicleRequest.getShiftType() == null) {
            return new ResponseEntity<>("Invalid shift: cannot be empty", HttpStatus.BAD_REQUEST); //shiftType is required
        }

        final String shiftTypeString = createVehicleRequest.getShiftType().toUpperCase();
        if (!ShiftType.isValidShiftType(shiftTypeString)) {
            return new ResponseEntity<>("Invalid shift", HttpStatus.BAD_REQUEST); //shiftType is required
        }

        if (createVehicleRequest.getYearOfMatriculation() == null || createVehicleRequest.getYearOfMatriculation() <= 1950) {
            return new ResponseEntity<>("Invalid year of matriculation: cannot be empty", HttpStatus.BAD_REQUEST); //yearOfMatriculation is required
        }

        if (createVehicleRequest.getFuelType() == null) {
            return new ResponseEntity<>("Invalid fuel type: cannot be empty", HttpStatus.BAD_REQUEST); //fuelType is required
        }

        final String fuelTypeString = createVehicleRequest.getFuelType().toUpperCase();
        if (!FuelType.isValidFuelType(fuelTypeString)) {
            return new ResponseEntity<>("Invalid fuel type", HttpStatus.BAD_REQUEST); //fuelType is required
        }

        if (createVehicleRequest.getPrice() == null || createVehicleRequest.getPrice() < 0) {
            return new ResponseEntity<>("Invalid price: cannot be empty", HttpStatus.BAD_REQUEST); //price is required
        }

        if (createVehicleRequest.getDiscount() == null || createVehicleRequest.getDiscount() < 0) {
            return new ResponseEntity<>("Invalid discount: cannot be empty", HttpStatus.BAD_REQUEST); //discount is required
        }

        if (createVehicleRequest.getUsed() == null) {
            return new ResponseEntity<>("Invalid used: cannot be empty", HttpStatus.BAD_REQUEST); //used is required
        }

        if (createVehicleRequest.getVehicleStatus() == null) {
            return new ResponseEntity<>("Invalid vehicle status: cannot be empty", HttpStatus.BAD_REQUEST); //vehicleStatus is required
        }

        final String vehicleStatusString = createVehicleRequest.getVehicleStatus().toUpperCase();
        if (!VehicleStatus.isValidVehicleStatus(vehicleStatusString)) {
            return new ResponseEntity<>("Invalid vehicle status", HttpStatus.BAD_REQUEST); //vehicleStatus is required
        }

        if (createVehicleRequest.getVehicleType() == null) {
            return new ResponseEntity<>("Invalid vehicle type: cannot be empty", HttpStatus.BAD_REQUEST); //vehicleType is required
        }

        final String vehicleTypeString = createVehicleRequest.getVehicleType().toUpperCase();
        if (!VehicleType.isValidVehicleType(vehicleTypeString)) {
            return new ResponseEntity<>("Invalid vehicle type", HttpStatus.BAD_REQUEST); //vehicleType is required
        }


        //lower all text
        //make query to check if there's a match
        //if there's a match, return null
        VehicleEntity vehicleEntity = vehicleMapper.convertCreateVehicleRequestToEntity(createVehicleRequest);
        var vehicleResponse = vehicleMapper.convertVehicleEntityToResponse(vehicleRepository.saveAndFlush(vehicleEntity));
        return new ResponseEntity<>(vehicleResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> getSingleVehicle(Long id) {
        Optional<VehicleEntity> vehicleFound = vehicleRepository.findById(id);
        if (vehicleFound.isEmpty()) {
            return new ResponseEntity<>("Vechile with id" + id + " not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(vehicleMapper.convertVehicleEntityToResponse(vehicleFound.get()), HttpStatus.OK);
    }


    public ResponseEntity<?> patchVehicle(UserEntity user, Long id, PatchVehicleRequest patchVehicleRequest) {
        if (user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(id);

        if (vehicleEntity.isEmpty()) {
            return new ResponseEntity<>("Vehicle with id " + id + " not found", HttpStatus.NOT_FOUND);
        }

        if (patchVehicleRequest.getBrand() != null) {
            vehicleEntity.get().setBrand(patchVehicleRequest.getBrand());
        }
        if (patchVehicleRequest.getModel() != null) {
            vehicleEntity.get().setModel(patchVehicleRequest.getModel());
        }

        if (patchVehicleRequest.getDisplacement() != null) {
            vehicleEntity.get().setDisplacement(patchVehicleRequest.getDisplacement());
        }

        if (patchVehicleRequest.getColor() != null && StringUtils.hasText(patchVehicleRequest.getColor())) {
            vehicleEntity.get().setColor(patchVehicleRequest.getColor());
        }

        if (patchVehicleRequest.getPower() != null && patchVehicleRequest.getPower() > 0) {
            vehicleEntity.get().setPower(patchVehicleRequest.getPower());
        }

        if (patchVehicleRequest.getShiftType() != null) {
            if (ShiftType.isValidShiftType(patchVehicleRequest.getShiftType())) {
                vehicleEntity.get().setShiftType(ShiftType.valueOf(patchVehicleRequest.getShiftType()));
            }
        }

        if (patchVehicleRequest.getYearOfMatriculation() != null) {
            vehicleEntity.get().setYearOfMatriculation(patchVehicleRequest.getYearOfMatriculation());
        }


        if (patchVehicleRequest.getFuelType() != null && StringUtils.hasText(patchVehicleRequest.getFuelType())) {
            if (FuelType.isValidFuelType(patchVehicleRequest.getFuelType())) {
                vehicleEntity.get().setFuelType(FuelType.valueOf(patchVehicleRequest.getFuelType()));
            }
        }


        if (patchVehicleRequest.getPrice() != null && patchVehicleRequest.getPrice() > 0) {
            vehicleEntity.get().setPrice(patchVehicleRequest.getPrice());
        }

        if (patchVehicleRequest.getDiscount() != null && patchVehicleRequest.getDiscount() >= 0) {
            vehicleEntity.get().setDiscount(patchVehicleRequest.getDiscount());
        }

        if (patchVehicleRequest.getAccessories() != null && StringUtils.hasText(patchVehicleRequest.getAccessories())) {
            vehicleEntity.get().setAccessories(patchVehicleRequest.getAccessories());
        }

        if (patchVehicleRequest.getUsed() != null) {
            vehicleEntity.get().setUsed(patchVehicleRequest.getUsed());
        }

        if (patchVehicleRequest.getVehicleStatus() != null) {
            if (VehicleStatus.isValidVehicleStatus(patchVehicleRequest.getVehicleStatus())) {
                vehicleEntity.get().setVehicleStatus(VehicleStatus.valueOf(patchVehicleRequest.getVehicleStatus()));
            }
        }
        if (patchVehicleRequest.getVehicleType() != null) {
            if (VehicleType.isValidVehicleType(patchVehicleRequest.getVehicleType())) {
                vehicleEntity.get().setVehicleType(VehicleType.valueOf(patchVehicleRequest.getVehicleType()));
            }
        }

        return new ResponseEntity<>(vehicleMapper.convertVehicleEntityToResponse(vehicleRepository.save(vehicleEntity.get())), HttpStatus.OK);
    }

    public Boolean deleteVehicle(UserEntity user, Long id) {
        if (user.getRole() != Role.ADMIN) {
            throw new UnauthorizedException();
        }

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(id);
        if (vehicleEntity.isEmpty()) {
            throw new NotFoundException("Vehicle with id " + id + " not found");
        }

        vehicleRepository.deleteById(id);
        return true;
    }

    public ResponseEntity<?> patchStatus(UserEntity user, Long id, String status) {
        if (user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(id);
        if (vehicleEntity.isEmpty()) {
            return new ResponseEntity<>("Vehicle with id " + id + " not found", HttpStatus.NOT_FOUND);
        }

        if (VehicleStatus.isValidVehicleStatus(status)) {
            vehicleEntity.get().setVehicleStatus(VehicleStatus.valueOf(status));
        }

        return new ResponseEntity<>(vehicleMapper.convertVehicleEntityToResponse(vehicleRepository.save(vehicleEntity.get())), HttpStatus.OK);
    }

    public ResponseEntity<?> findByStatus(UserEntity user, String status) {
        if (user.getRole() != Role.ADMIN && user.getRole() != Role.SELLER) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }


        String statusString = status.toUpperCase();
        if (VehicleStatus.isValidVehicleStatus(statusString)) {
            return new ResponseEntity<>(vehicleRepository.findByVehicleStatus(VehicleStatus.valueOf(statusString)), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    public List<VehicleResponse> getAllVehicles() {
        List<VehicleEntity> vehicleEntities = vehicleRepository.findAll();
        return vehicleMapper.mapList(vehicleEntities, VehicleResponse.class);
    }


    //ADMIN ROUTES
    public ResponseEntity<?> getMostSoldVehiclePeriod(String startDate, String endDate) {
        try {
            OffsetDateTime.parse(startDate);

        } catch (Exception e) {
            return new ResponseEntity<>("Invalid start date", HttpStatus.BAD_REQUEST);
        }

        try {
            OffsetDateTime.parse(endDate);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid end date", HttpStatus.BAD_REQUEST);
        }

        OffsetDateTime start = OffsetDateTime.parse(startDate);
        OffsetDateTime end = OffsetDateTime.parse(endDate);
        VehicleEntity mostVehicleSold = orderRepository.findMostSoldInAPeriod(start, end);


        if (mostVehicleSold != null) {
            return new ResponseEntity<>(vehicleMapper.convertVehicleEntityToResponse(mostVehicleSold), HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<?> getMostOrderedVehiclePeriod(String startDate, String endDate) {
        try {
            OffsetDateTime.parse(startDate);

        } catch (Exception e) {
            return new ResponseEntity<>("Invalid start date", HttpStatus.BAD_REQUEST);
        }

        try {
            OffsetDateTime.parse(endDate);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid end date", HttpStatus.BAD_REQUEST);
        }

        OffsetDateTime start = OffsetDateTime.parse(startDate);
        OffsetDateTime end = OffsetDateTime.parse(endDate);
        VehicleEntity mostVehicleSold = orderRepository.findMostOrderedInAPeriod(start, end);


        if (mostVehicleSold != null) {
            return new ResponseEntity<>(vehicleMapper.convertVehicleEntityToResponse(mostVehicleSold), HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<?> getSalesCountBySellerId(Long seller_id) {
        Long salesCount = orderRepository.getSalesCountBySellerId(seller_id);
        if (salesCount != null) {
            return new ResponseEntity<>(salesCount, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<?> getHighestPriceSold() {
        VehicleEntity highestPriceVehicleSold = orderRepository.findHighestPriceSold();


        if (highestPriceVehicleSold != null) {
            return new ResponseEntity<>(vehicleMapper.convertVehicleEntityToResponse(highestPriceVehicleSold), HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<?> getLowestPriceSold() {
        VehicleEntity lowestPriceVehicleSold = orderRepository.findLowestPriceSold();


        if (lowestPriceVehicleSold != null) {
            return new ResponseEntity<>(vehicleMapper.convertVehicleEntityToResponse(lowestPriceVehicleSold), HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

}
