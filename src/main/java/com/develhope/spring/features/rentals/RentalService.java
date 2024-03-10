package com.develhope.spring.features.rentals;

import com.develhope.spring.exception.NotFoundException;
import com.develhope.spring.exception.UnauthorizedException;
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

    public ResponseEntity<?> createRentalByVehicleId(UserEntity user, CreateRentalRequest rentalRequest) {
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(rentalRequest.getVehicleId());
        if (vehicleEntity.isEmpty()) {
            return new ResponseEntity<>("Vehicle with id " + rentalRequest.getVehicleId() + " not found", HttpStatus.NOT_FOUND);
        }

        if (rentalRequest.getSellerId() == rentalRequest.getCustomerId()) {
            return new ResponseEntity<>("Seller and Customer cannot be the same", HttpStatus.BAD_REQUEST);
        }

        if (user.getRole() == Role.SELLER) {
            if (rentalRequest.getSellerId() != user.getId()) {
                return new ResponseEntity<>("Seller and User ID do not match.", HttpStatus.UNAUTHORIZED);
            }
        }
        final var customerSameUser = rentalRequest.getCustomerId().equals(user.getId());

        Optional<UserEntity> userRenter = customerSameUser ? Optional.of(user) : userRepository.findById(rentalRequest.getCustomerId());
        if (userRenter.isEmpty()) {
            return new ResponseEntity<>("Customer with id " + rentalRequest.getCustomerId() + " not found", HttpStatus.NOT_FOUND);
        }

        if (userRenter.get().getRole() != Role.CUSTOMER) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        if (user.getRole() != Role.ADMIN && user.getRole() != Role.SELLER && userRenter.get().getId() != user.getId()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        final var sellerSameUser = rentalRequest.getSellerId().equals(user.getId());
        Optional<UserEntity> userSeller = sellerSameUser ? Optional.of(user) : userRepository.findById(rentalRequest.getSellerId());

        if (userSeller.isEmpty()) {
            return new ResponseEntity<>("Seller with id " + rentalRequest.getSellerId() + " not found", HttpStatus.NOT_FOUND);
        }

        if (userSeller.get().getRole() != Role.SELLER) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        if (user.getRole() != Role.ADMIN && user.getRole() != Role.CUSTOMER && userSeller.get().getId() != user.getId()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        if (user.getRole() != Role.ADMIN && user.getRole() != Role.SELLER && userSeller.get().getId() == user.getId()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        if (rentalRequest.getStartOfRental().isBefore(OffsetDateTime.now(ZoneOffset.UTC))) {
            return new ResponseEntity<>("Start of rental cannot be in the past", HttpStatus.BAD_REQUEST);
        }
        if (rentalRequest.getEndOfRental().isBefore(rentalRequest.getStartOfRental())) {
            return new ResponseEntity<>("End of rental cannot be before start of rental", HttpStatus.BAD_REQUEST);
        }

        RentalEntity rentalEntity = rentalMapper.convertCreateRentalRequestToEntity(rentalRequest);
        rentalEntity.setPaymentStatus(PaymentStatus.PENDING); //let's assume user hasn't confirmed yet
        rentalEntity.setVehicleEntity(vehicleEntity.get());
        rentalEntity.setRenter(userRenter.get());
        rentalEntity.setSeller(userSeller.get());


        Long days = ChronoUnit.DAYS.between(rentalRequest.getStartOfRental(), rentalRequest.getEndOfRental()) + 1;
        rentalEntity.setTotalCostRental(vehicleEntity.get().getDailyCostRental() * days.intValue());
        RentalEntity rentalEntitySaved = rentalRepository.saveAndFlush(rentalEntity);

        var rentalResponse = rentalMapper.convertRentalEntityToResponse(rentalEntitySaved);
        return new ResponseEntity<>(rentalResponse, HttpStatus.OK);
    }

    public Boolean deleteRental(UserEntity user, Long id_to_delete) {
        Optional<RentalEntity> rentalEntity = rentalRepository.findById(id_to_delete);
        if (rentalEntity.isEmpty()) {
            throw new NotFoundException("Rental with id: " + id_to_delete + " not found");
        }

        UserEntity userRenter = rentalEntity.get().getRenter();
        if (userRenter == null) {
            throw new NotFoundException("Renter in the rental with id: " + id_to_delete + " not found");
        }

        UserEntity userSeller = rentalEntity.get().getSeller();
        if (userSeller == null) {
            throw new NotFoundException("Seller in the rental with id: " + id_to_delete + " not found");
        }

        if (user.getRole() != Role.ADMIN) {
            if (user.getRole() == Role.SELLER) {
                if (user.getId() != userSeller.getId()) {
                    throw new UnauthorizedException();
                }
            } else {
                if (user.getId() != userRenter.getId()) {
                    throw new UnauthorizedException();
                }
            }
        }

        rentalRepository.deleteById(id_to_delete);
        return true;
    }


    public ResponseEntity<?> patchRental(UserEntity user, Long rentalId, PatchRentalRequest rentalRequest) {
        if (user.getRole() == Role.CUSTOMER) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Optional<RentalEntity> rentalEntity = rentalRepository.findById(rentalId);
        if (rentalEntity.isEmpty()) {
            return new ResponseEntity<>("Rental with id " + rentalId + " not found", HttpStatus.NOT_FOUND);
        }


        UserEntity userRenter = rentalEntity.get().getRenter();
        if (userRenter == null) {
            return new ResponseEntity<>("Renter in the rental with id: " + rentalId + " not found", HttpStatus.NOT_FOUND);
        }

        UserEntity userSeller = rentalEntity.get().getSeller();
        if (userSeller == null) {
            return new ResponseEntity<>("Seller in the rental with id: " + rentalId + " not found", HttpStatus.NOT_FOUND);
        }

        final var sellerSameUser = userSeller.getId().equals(user.getId());

        userSeller = sellerSameUser ? user : userSeller;

        if (user.getRole() != Role.ADMIN && user.getRole() == Role.SELLER && !sellerSameUser) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
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
        var rentalResponse = rentalMapper.convertRentalEntityToResponse(rentalRepository.save(rentalEntity.get()));
        return new ResponseEntity<>(rentalResponse, HttpStatus.OK);
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

