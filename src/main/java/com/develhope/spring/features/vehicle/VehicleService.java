package com.develhope.spring.features.vehicle;

import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.users.UserRepository;
import com.develhope.spring.features.users.UserType;
import com.develhope.spring.features.vehicle.PropertiesEnum.FuelType;
import com.develhope.spring.features.vehicle.PropertiesEnum.ShiftType;
import com.develhope.spring.features.vehicle.dto.CreateVehicleRequest;
import com.develhope.spring.features.vehicle.dto.PatchVehicleRequest;
import com.develhope.spring.features.vehicle.dto.VehicleResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final UserRepository userRepository;


    public VehicleResponse createVehicle(CreateVehicleRequest createVehicleRequest, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return null;
        }

        final var userType = requesterUser.get().getUserType();
        if (userType != UserType.ADMIN && userType != UserType.SELLER) {
            return null; //unhaoutrized
        }

        /*if (!StringUtils.hasText(createVehicleRequest.getBrand())) {
            return null; //brand is required
        }

        if (!StringUtils.hasText(createVehicleRequest.getModel())) {
            return null; //model is required
        }

        if (createVehicleRequest.getDisplacement() == null || createVehicleRequest.getDisplacement() <= 0) {
            return null; //displacement is required
        }

        if (!StringUtils.hasText(createVehicleRequest.getColor())) {
            return null; //color is required
        }

        if (createVehicleRequest.getPower() == null || createVehicleRequest.getPower() <= 0) {
            return null; //power is required
        }

        if (createVehicleRequest.getShiftType() == null || !ShiftType.isValidShiftType(createVehicleRequest.getShiftType()) {
            return null; //shiftType is required
        }

        if (createVehicleRequest.getYearOfMatriculation() == null || createVehicleRequest.getYearOfMatriculation() <= 1950) {
            return null; //yearOfMatriculation is required
        }

        if (!StringUtils.hasText(createVehicleRequest.getFuelType()) || !FuelType.isValidFuelType(createVehicleRequest.getFuelType()) {
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

        if (createVehicleRequest.getVehicleStatus() == null || !VehicleStatus.isValidVehicleStatus(createVehicleRequest.getVehicleStatus()) {
            return null; //vehicleStatus is required
        }

        if (createVehicleRequest.getVehicleType() == null || !VehicleType.isValidVehicleType(createVehicleRequest.getVehicleType() {
            return null; //vehicleType is required
        }*/

        VehicleEntity vehicleEntity = vehicleMapper.convertCreateVehicleRequestToEntity(createVehicleRequest);
        return vehicleMapper.convertVehicleEntityToResponse(vehicleRepository.save(vehicleEntity));
    }

    public VehicleEntity getSingleVehicle(Long id) {
        Optional<VehicleEntity> user = vehicleRepository.findById(id);
        return user.orElse(null);
    }


    public VehicleResponse patchVehicle(Long id, PatchVehicleRequest patchVehicleRequest, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return null;
        }

        final var userType = requesterUser.get().getUserType();
        if (userType != UserType.ADMIN && userType != UserType.SELLER) {
            return null; //unhaoutrized
        }

        AtomicReference<Optional<VehicleResponse>> atomicReference = new AtomicReference<>();

        vehicleRepository.findById(id).ifPresentOrElse(vehicleEntity -> {

            if (patchVehicleRequest.getBrand() != null) {
                vehicleEntity.setBrand(patchVehicleRequest.getBrand());
            }
            if (patchVehicleRequest.getModel() != null) {
                vehicleEntity.setModel(patchVehicleRequest.getModel());
            }

            if (patchVehicleRequest.getDisplacement() != null) {
                vehicleEntity.setDisplacement(patchVehicleRequest.getDisplacement());
            }

            if (patchVehicleRequest.getColor() != null && StringUtils.hasText(patchVehicleRequest.getColor())) {
                vehicleEntity.setColor(patchVehicleRequest.getColor());
            }

            if (patchVehicleRequest.getPower() != null && patchVehicleRequest.getPower() > 0) {
                vehicleEntity.setPower(patchVehicleRequest.getPower());
            }

            if (patchVehicleRequest.getShiftType() != null) {
                if (ShiftType.isValidShiftType(patchVehicleRequest.getShiftType())) {
                    vehicleEntity.setShiftType(ShiftType.valueOf(patchVehicleRequest.getShiftType()));
                }
            }

            if (patchVehicleRequest.getYearOfMatriculation() != null) {
                vehicleEntity.setYearOfMatriculation(patchVehicleRequest.getYearOfMatriculation());
            }


            if (patchVehicleRequest.getFuelType() != null && StringUtils.hasText(patchVehicleRequest.getFuelType())) {
                if (FuelType.isValidFuelType(patchVehicleRequest.getFuelType())) {
                    vehicleEntity.setFuelType(FuelType.valueOf(patchVehicleRequest.getFuelType()));
                }
            }


            if (patchVehicleRequest.getPrice() != null && patchVehicleRequest.getPrice() > 0) {
                vehicleEntity.setPrice(patchVehicleRequest.getPrice());
            }

            if (patchVehicleRequest.getDiscount() != null && patchVehicleRequest.getDiscount() >= 0) {
                vehicleEntity.setDiscount(patchVehicleRequest.getDiscount());
            }

            if (patchVehicleRequest.getAccessories() != null && StringUtils.hasText(patchVehicleRequest.getAccessories())) {
                vehicleEntity.setAccessories(patchVehicleRequest.getAccessories());
            }

            if (patchVehicleRequest.getUsed() != null) {
                vehicleEntity.setUsed(patchVehicleRequest.getUsed());
            }

            if (patchVehicleRequest.getVehicleStatus() != null) {
                if (VehicleStatus.isValidVehicleStatus(patchVehicleRequest.getVehicleStatus())) {
                    vehicleEntity.setVehicleStatus(VehicleStatus.valueOf(patchVehicleRequest.getVehicleStatus()));
                }
            }
            if (patchVehicleRequest.getVehicleType() != null) {
                if (VehicleType.isValidVehicleType(patchVehicleRequest.getVehicleType())) {
                    vehicleEntity.setVehicleType(VehicleType.valueOf(patchVehicleRequest.getVehicleType()));
                }
            }

            atomicReference.set(Optional.of(vehicleMapper.convertVehicleEntityToResponse(vehicleRepository.save(vehicleEntity))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        if (atomicReference.get().isEmpty()) {
            return null;
        }
        return atomicReference.get().get();
    }

    public Boolean deleteVehicle(Long id, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return false;
        }

        final var userType = requesterUser.get().getUserType();
        if (userType != UserType.ADMIN && userType != UserType.SELLER) {
            return false; //unhaoutrized
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
        if (userType != UserType.ADMIN && userType != UserType.SELLER) {
            return null; //unhaoutrized
        }

        AtomicReference<Optional<VehicleResponse>> atomicReference = new AtomicReference<>();

        vehicleRepository.findById(id).ifPresentOrElse(vehicleEntity -> {
            if (VehicleStatus.isValidVehicleStatus(status)) {
                vehicleEntity.setVehicleStatus(VehicleStatus.valueOf(status));
                atomicReference.set(Optional.of(vehicleMapper.convertVehicleEntityToResponse(vehicleRepository.save(vehicleEntity))));
            } else {
                atomicReference.set(Optional.empty());
            }
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        if (atomicReference.get().isEmpty()) {
            return null;
        }
        return atomicReference.get().get();
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

    public VehicleEntity getDetailsOfVehicle(Long id) {
        return getSingleVehicle(id);
    }

    public List<VehicleEntity> getAllVehicles() {
        return vehicleRepository.findAll();
    }

}
