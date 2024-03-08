package com.develhope.spring.features.vehicle;

import com.develhope.spring.features.orders.OrderRepository;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.users.UserMapper;
import com.develhope.spring.features.users.UserRepository;
import com.develhope.spring.features.users.UserType;
import com.develhope.spring.features.users.dto.UserResponse;
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


    public VehicleResponse createVehicle(CreateVehicleRequest createVehicleRequest, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return null;
        }

        final var userType = requesterUser.get().getUserType();
        if (userType != UserType.ADMIN) {
            return null; //unhaoutrized
        }

        /*
        if (createVehicleRequest.getBrand() == null || !StringUtils.hasText(createVehicleRequest.getBrand())) {
            return null; //brand is required
        }

        if (createVehicleRequest.getModel() == null || !StringUtils.hasText(createVehicleRequest.getModel())) {
            return null; //model is required
        }
        

        if (createVehicleRequest.getDisplacement() == null || createVehicleRequest.getDisplacement() <= 0) {
            return null; //displacement is required
        }

        if (createVehicleRequest.getColor() == null || !StringUtils.hasText(createVehicleRequest.getColor())) {
            return null; //color is required
        }

        if (createVehicleRequest.getPower() == null || createVehicleRequest.getPower() <= 0) {
            return null; //power is required
        }
        
        if (createVehicleRequest.getShiftType() == null) {
            return null; //shiftType is required
        }

        final String shiftTypeString = createVehicleRequest.getShiftType().toUpperCase();
        if (!ShiftType.isValidShiftType(shiftTypeString)) {
            return null; //shiftType is required
        }

        if (createVehicleRequest.getYearOfMatriculation() == null || createVehicleRequest.getYearOfMatriculation() <= 1950) {
            return null; //yearOfMatriculation is required
        }

        if (createVehicleRequest.getFuelType() == null) {
            return null; //fuelType is required
        }

        final String fuelTypeString = createVehicleRequest.getFuelType().toUpperCase();
        if (!FuelType.isValidFuelType(fuelTypeString)) {
            return null; //fuelType is required
        }

        if (createVehicleRequest.getPrice() == null || createVehicleRequest.getPrice() < 0) {
            return null; //price is required
        }

        if (createVehicleRequest.getDiscount() == null || createVehicleRequest.getDiscount() < 0) {
            return null; //discount is required
        }

        if (createVehicleRequest.getUsed() == null) {
            return null; //used is required
        }

        if (createVehicleRequest.getVehicleStatus() == null) {
            return null; //vehicleStatus is required
        }

        final String vehicleStatusString = createVehicleRequest.getVehicleStatus().toUpperCase();
        if (!VehicleStatus.isValidVehicleStatus(vehicleStatusString)) {
            return null; //vehicleStatus is required
        }

        if (createVehicleRequest.getVehicleType() == null) {
            return null; //vehicleType is required
        }

        final String vehicleTypeString = createVehicleRequest.getVehicleType().toUpperCase();
        if (!VehicleType.isValidVehicleType(vehicleTypeString)) {
            return null; //vehicleType is required
        }
        
        */

        //lower all text
        //make query to check if there's a match
        //if there's a match, return null
        VehicleEntity vehicleEntity = vehicleMapper.convertCreateVehicleRequestToEntity(createVehicleRequest);
        UserResponse sellerResponse = userMapper.convertUserEntityToResponse(requesterUser.get());
        return vehicleMapper.convertVehicleEntityToResponse(vehicleRepository.saveAndFlush(vehicleEntity), sellerResponse);
    }

    public VehicleResponse getSingleVehicle(Long id) {
        Optional<VehicleEntity> vehicleFound = vehicleRepository.findById(id);
        if (vehicleFound.isEmpty()) {
            return null;
        }

        return vehicleMapper.convertVehicleEntityToResponse(vehicleFound.get());
    }


    public VehicleResponse patchVehicle(Long id, PatchVehicleRequest patchVehicleRequest, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return null;
        }

        final var userType = requesterUser.get().getUserType();
        if (userType != UserType.ADMIN) {
            return null; //unhaoutrized
        }

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(id);

        if (vehicleEntity.isEmpty()) {
            return null;
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

        return vehicleMapper.convertVehicleEntityToResponse(vehicleRepository.save(vehicleEntity.get()));
    }

    public Boolean deleteVehicle(Long id, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return false;
        }

        final var userType = requesterUser.get().getUserType();
        if (userType != UserType.ADMIN) {
            return false; //unhaoutrized
        }

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(id);
        if (vehicleEntity.isEmpty()) {
            return false;
        }

        vehicleRepository.deleteById(id);
        return true;
    }

    public VehicleResponse patchStatus(Long id, String status, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return null;
        }

        final var userType = requesterUser.get().getUserType();
        if (userType != UserType.ADMIN) {
            return null; //unhaoutrized
        }

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(id);
        if (vehicleEntity.isPresent()) {

            if (VehicleStatus.isValidVehicleStatus(status)) {
                vehicleEntity.get().setVehicleStatus(VehicleStatus.valueOf(status));
                return vehicleMapper.convertVehicleEntityToResponse(vehicleRepository.save(vehicleEntity.get()));
            }
        }
        return null;
    }


    public ResponseEntity<?> findByStatus(String status, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        final var userType = requesterUser.get().getUserType();
        if (userType != UserType.ADMIN && userType != UserType.SELLER) {
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
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        try {
            OffsetDateTime.parse(endDate);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        try {
            OffsetDateTime.parse(endDate);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
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
