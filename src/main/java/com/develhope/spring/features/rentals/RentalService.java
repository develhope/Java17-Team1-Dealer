package com.develhope.spring.features.rentals;

import com.develhope.spring.features.orders.PaymentStatus;
import com.develhope.spring.features.rentals.dto.CreateRentalRequest;
import com.develhope.spring.features.rentals.dto.PatchRentalRequest;
import com.develhope.spring.features.rentals.dto.RentalResponse;
import com.develhope.spring.features.users.Role;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.users.UserRepository;
import com.develhope.spring.features.vehicle.VehicleEntity;
import com.develhope.spring.features.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public RentalResponse createRentalByVehicleId(UserEntity user, CreateRentalRequest rentalRequest) {
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(rentalRequest.getVehicleId());
        if (vehicleEntity.isEmpty()) {
            return null; //invalid vehicle id
        }

        if (rentalRequest.getSellerId() == rentalRequest.getCustomerId()) {
            return null;
        }

        if (user.getRole() == Role.SELLER) {
            if (rentalRequest.getSellerId() != user.getId()) {
                return null;
            }
        }
        final var customerSameUser = rentalRequest.getCustomerId().equals(user.getId());

        Optional<UserEntity> userRenter = customerSameUser ? Optional.of(user) : userRepository.findById(rentalRequest.getCustomerId());
        if (userRenter.isEmpty()) {
            return null; //invalid renter
        }

        if (userRenter.get().getRole() != Role.CUSTOMER) {
            return null;
        }

        if (user.getRole() != Role.ADMIN && user.getRole() != Role.SELLER && userRenter.get().getId() != user.getId()) {
            return null;
        }

        final var sellerSameUser = rentalRequest.getSellerId().equals(user.getId());
        Optional<UserEntity> userSeller = sellerSameUser ? Optional.of(user) : userRepository.findById(rentalRequest.getSellerId());

        if (userSeller.isEmpty()) {
            return null; //invalid seller
        }

        if (userSeller.get().getRole() != Role.SELLER) {
            return null;
        }

        if (user.getRole() != Role.ADMIN && user.getRole() != Role.CUSTOMER && userSeller.get().getId() != user.getId()) {
            return null;
        }

        if (user.getRole() != Role.ADMIN && user.getRole() != Role.SELLER && userSeller.get().getId() == user.getId()) {
            return null;
        }

        if (rentalRequest.getStartOfRental().isBefore(OffsetDateTime.now(ZoneOffset.UTC))) {
            return null;
        }
        if (rentalRequest.getEndOfRental().isBefore(rentalRequest.getStartOfRental())) {
            return null;
        }

        RentalEntity rentalEntity = rentalMapper.convertCreateRentalRequestToEntity(rentalRequest);
        rentalEntity.setPaymentStatus(PaymentStatus.PENDING); //let's assume user hasn't confirmed yet
        rentalEntity.setVehicleEntity(vehicleEntity.get());
        rentalEntity.setRenter(userRenter.get());
        rentalEntity.setSeller(userSeller.get());


        Long days = ChronoUnit.DAYS.between(rentalRequest.getStartOfRental(), rentalRequest.getEndOfRental()) + 1;
        rentalEntity.setTotalCostRental(vehicleEntity.get().getDailyCostRental() * days.intValue());
        RentalEntity rentalEntitySaved = rentalRepository.saveAndFlush(rentalEntity);

        return rentalMapper.convertRentalEntityToResponse(rentalEntitySaved);
    }

    public Boolean deleteRental(UserEntity user, Long id_do_delete) {
        Optional<RentalEntity> rentalEntity = rentalRepository.findById(id_do_delete);
        if (rentalEntity.isEmpty()) {
            return false; //order of orderId not exists
        }

        UserEntity userRenter = rentalEntity.get().getRenter();
        if (userRenter == null) {
            return false;
        }

        UserEntity userSeller = rentalEntity.get().getSeller();
        if (userSeller == null) {
            return false;
        }

        if (user.getRole() != Role.ADMIN) {
            if (user.getRole() == Role.SELLER) {
                if (user.getId() != userSeller.getId()) {
                    return false;
                }
            } else {
                if (user.getId() != userRenter.getId()) {
                    return false;
                }
            }
        }

        rentalRepository.deleteById(id_do_delete);
        return true;
    }


    public RentalResponse patchRental(UserEntity user, Long rentalId, PatchRentalRequest rentalRequest) {
        if (user.getRole() == Role.CUSTOMER) {
            return null;
        }

        Optional<RentalEntity> rentalEntity = rentalRepository.findById(rentalId);
        if (rentalEntity.isEmpty()) {
            return null; //order of orderId not exists
        }


        UserEntity userRenter = rentalEntity.get().getRenter();
        if (userRenter == null) {
            return null;
        }

        UserEntity userSeller = rentalEntity.get().getSeller();
        if (userSeller == null) {
            return null;
        }

        final var sellerSameUser = userSeller.getId().equals(user.getId());

        userSeller = sellerSameUser ? user : userSeller;

        if (user.getRole() != Role.ADMIN && user.getRole() == Role.SELLER && !sellerSameUser) {
            return null;
        }

        if (rentalRequest.getStartOfRental() != null && rentalRequest.getEndOfRental() != null) {
            OffsetDateTime startOfRental = rentalRequest.getStartOfRental();
            OffsetDateTime endOfRental = rentalRequest.getEndOfRental();

            if (endOfRental.isAfter(startOfRental)) {
                rentalEntity.get().setStartOfRental(startOfRental);
                rentalEntity.get().setEndOfRental(endOfRental);
            }
        }

        if (rentalRequest.getDailyCostRental() != null) {
            rentalEntity.get().setDailyCostRental(rentalRequest.getDailyCostRental());
        }

        if (rentalRequest.getTotalCostRental() != null) {
            rentalEntity.get().setTotalCostRental(rentalRequest.getTotalCostRental());
        }

        if (rentalRequest.getVehicleEntity() != null) {
            rentalEntity.get().setVehicleEntity(rentalRequest.getVehicleEntity());
        }

        if (rentalRequest.getUserEntity() != null) {
            rentalEntity.get().setRenter(rentalRequest.getUserEntity());
        }

        if (rentalRequest.getPaymentStatus() != null) {
            if (PaymentStatus.isValidPaymentStatus(rentalRequest.getPaymentStatus())) {
                rentalEntity.get().setPaymentStatus(PaymentStatus.valueOf(rentalRequest.getPaymentStatus()));
            }
        }
        return rentalMapper.convertRentalEntityToResponse(rentalRepository.save(rentalEntity.get()));

    }

    public ResponseEntity<?> getRentalListByRenterId(UserEntity user, Long renterId) {
        if (user.getRole() == Role.CUSTOMER) {
            if (user.getId() != renterId) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        switch (user.getRole()) {
            case ADMIN:
            case CUSTOMER: {
                var rentalEntityList = rentalRepository.findAllByRenterId(renterId);
                return new ResponseEntity<>(rentalMapper.mapList(rentalEntityList, RentalResponse.class), HttpStatus.OK);
            }
            case SELLER: {
                if (user.getId() != renterId) {
                    var rentalEntityList = rentalRepository.findAllByRenterIdAndSellerId(renterId, user.getId());
                    return new ResponseEntity<>(rentalMapper.mapList(rentalEntityList, RentalResponse.class), HttpStatus.OK);
                } else { //fallback
                    var rentalEntityList = rentalRepository.findAllBySellerId(user.getId());
                    return new ResponseEntity<>(rentalMapper.mapList(rentalEntityList, RentalResponse.class), HttpStatus.OK);
                }
            }
            default:
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }
}

