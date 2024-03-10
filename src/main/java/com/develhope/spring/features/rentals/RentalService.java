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
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public RentalResponse createRentalByVehicleId(CreateRentalRequest rentalRequest, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return null;
        }
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(rentalRequest.getVehicleId());
        if (vehicleEntity.isEmpty()) {
            return null; //invalid vehicle id
        }

        if (rentalRequest.getSellerId() == rentalRequest.getCustomerId()) {
            return null;
        }


        if (requesterUser.get().getRole() == Role.SELLER) {
            if (rentalRequest.getSellerId() != requester_id) {
                return null;
            }
        }


        Optional<UserEntity> userRenter = userRepository.findById(rentalRequest.getCustomerId());
        if (userRenter.isEmpty()) {
            return null; //invalid renter
        }

        if (userRenter.get().getRole() != Role.CUSTOMER) {
            return null;
        }

        if (requesterUser.get().getRole() != Role.ADMIN && requesterUser.get().getRole() != Role.SELLER && userRenter.get().getId() != requester_id) {
            return null;
        }


        Optional<UserEntity> userSeller = userRepository.findById(rentalRequest.getSellerId());
        if (userSeller.isEmpty()) {
            return null; //invalid seller
        }

        if (userSeller.get().getRole() != Role.SELLER) {
            return null;
        }

        if (requesterUser.get().getRole() != Role.ADMIN && requesterUser.get().getRole() != Role.CUSTOMER && userSeller.get().getId() != requester_id) {
            return null;
        }

        if (requesterUser.get().getRole() != Role.ADMIN && requesterUser.get().getRole() != Role.SELLER && userSeller.get().getId() == requester_id) {
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

    public Boolean deleteRental(Long orderId, Long requester_id) {
        Optional<RentalEntity> rentalEntity = rentalRepository.findById(orderId);
        if (rentalEntity.isEmpty()) {
            return false; //order of orderId not exists
        }

        UserEntity userRenter = rentalEntity.get().getRenter();
        if (userRenter == null) {
            return false;
        }

        Optional<UserEntity> userRequester = userRepository.findById(requester_id);

        if (userRequester.isEmpty()) {
            return false;
        }

        UserEntity userSeller = rentalEntity.get().getSeller();
        if (userSeller == null) {
            return false;
        }

        if (userRequester.get().getRole() != Role.ADMIN) {
            if (userRequester.get().getRole() == Role.SELLER) {
                if (userRequester.get().getId() != userSeller.getId()) {
                    return false;
                }
            } else {
                if (userRequester.get().getId() != userRenter.getId()) {
                    return false;
                }
            }
        }

        rentalRepository.deleteById(orderId);
        return true;
    }


    public RentalResponse patchRental(Long rentalId, PatchRentalRequest rentalRequest, Long requester_id) {

        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return null;
        }

        Optional<RentalEntity> rentalEntity = rentalRepository.findById(rentalId);
        if (rentalEntity.isEmpty()) {
            return null; //order of orderId not exists
        }


        UserEntity originalUser = rentalEntity.get().getRenter();
        if (originalUser == null) {
            return null;
        }


        if ((originalUser.getRole() == Role.SELLER && originalUser.getId() != requester_id) || originalUser.getRole() == Role.CUSTOMER) {
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

    public List<RentalResponse> getRentalListByRenterId(Long renterId, Long requester_id) {
        List<RentalEntity> rentalEntityList = rentalRepository.findAllByRenter(renterId);
        return rentalMapper.mapList(rentalEntityList, RentalResponse.class);
    }
}

