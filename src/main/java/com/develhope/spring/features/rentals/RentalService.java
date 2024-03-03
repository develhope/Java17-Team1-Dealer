package com.develhope.spring.features.rentals;

import com.develhope.spring.features.orders.OrderEntity;
import com.develhope.spring.features.orders.PaymentStatus;
import com.develhope.spring.features.orders.dto.PatchOrderRequest;
import com.develhope.spring.features.rentals.dto.CreateRentalRequest;
import com.develhope.spring.features.rentals.dto.PatchRentalRequest;
import com.develhope.spring.features.rentals.dto.RentalResponse;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.users.UserRepository;
import com.develhope.spring.features.users.UserService;
import com.develhope.spring.features.users.UserType;
import com.develhope.spring.features.vehicle.VehicleEntity;
import com.develhope.spring.features.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public RentalResponse createRentalByVehicleId(Long vehicleId, CreateRentalRequest rentalRequest, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return null;
        }

        final var userType = requesterUser.get().getUserType();
        if (userType == UserType.SELLER) {
            return null; //unhaoutrized
        }

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            return null; //invalid vehicle id
        }


        RentalEntity rentalEntity = rentalMapper.convertCreateRentalRequestToEntity(rentalRequest);
        rentalEntity.setPaymentStatus(PaymentStatus.PENDING); //let's assume user hasn't confirmed yet
        rentalEntity.setVehicleEntity(vehicleEntity.get());
        rentalEntity.setRenter(requesterUser.get());

        RentalEntity rentalEntitySaved = rentalRepository.saveAndFlush(rentalEntity);

        return rentalMapper.convertRentalEntityToResponse(rentalEntitySaved);
    }

    public Boolean deleteRental(Long orderId, Long requester_id) {
        Optional<RentalEntity> rentalEntity = rentalRepository.findById(orderId);
        if (rentalEntity.isEmpty()) {
            return false; //order of orderId not exists
        }

        UserEntity originalUser = rentalEntity.get().getRenter();
        if (!userService.isRequesterIDValid(originalUser, requester_id)) {
            return false;
        }


        rentalRepository.deleteById(orderId);
        return true;
    }


    public RentalResponse patchRental(Long rental_id, PatchRentalRequest rentalRequest, Long requester_id) {
        AtomicReference<Optional<RentalResponse>> atomicReference = new AtomicReference<>();

        rentalRepository.findById(rental_id).ifPresentOrElse(foundRental -> {
            if (rentalRequest.getStartOfRental() != null && rentalRequest.getEndOfRental() != null) {
                OffsetDateTime startOfRental = rentalRequest.getStartOfRental();
                OffsetDateTime endOfRental = rentalRequest.getEndOfRental();

                if (endOfRental.isAfter(startOfRental)) {
                    foundRental.setStartOfRental(startOfRental);
                    foundRental.setEndOfRental(endOfRental);
                }
            }

            if (rentalRequest.getDailyCostRental() != null) {
                foundRental.setDailyCostRental(rentalRequest.getDailyCostRental());
            }

            if (rentalRequest.getTotalCostRental() != null) {
                foundRental.setTotalCostRental(rentalRequest.getTotalCostRental());
            }

            if (rentalRequest.getVehicleEntity() != null) {
                foundRental.setVehicleEntity(rentalRequest.getVehicleEntity());
            }

            if (rentalRequest.getUserEntity() != null) {
                foundRental.setRenter(rentalRequest.getUserEntity());
            }

            if (rentalRequest.getPaymentStatus() != null) {
                if (PaymentStatus.isValidPaymentStatus(rentalRequest.getPaymentStatus())) {
                    foundRental.setPaymentStatus(PaymentStatus.valueOf(rentalRequest.getPaymentStatus()));
                }
            }
            atomicReference.set(Optional.of(rentalMapper.convertRentalEntityToResponse(rentalRepository.save(foundRental))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        if (atomicReference.get().isEmpty()) {
            return null;
        }
        
        return atomicReference.get().get();
    }

    public List<RentalResponse> getRentalListById(Long id, Long requester_id) {
        List<RentalEntity> rentalEntityList = rentalRepository.findAllByRenter(id);
        if (rentalEntityList.isEmpty()) {
            return null;
        }
        return rentalMapper.mapList(rentalEntityList, RentalResponse.class);
    }
}

