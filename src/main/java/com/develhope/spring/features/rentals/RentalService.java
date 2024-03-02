package com.develhope.spring.features.rentals;

import com.develhope.spring.features.rentals.dto.RentalResponse;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.users.UserRepository;
import com.develhope.spring.features.users.UserService;
import com.develhope.spring.features.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final RentalMapper rentalMapper;
    private final UserRepository userRepository;

    public RentalModel createRental(RentalModel rentalModel) {
        RentalEntity rentalEntity = rentalMapper.convertRentalModelToEntity(rentalModel);
        RentalEntity rentalEntitySaved = rentalRepository.saveAndFlush(rentalEntity);
        RentalModel rentalModelResponse = rentalMapper.convertRentalEntityToModel(rentalEntitySaved);
        return rentalModelResponse;
    }

    public Boolean deleteSingleRental(Long id) {
        if (rentalRepository.existsById(id)) {
            rentalRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // momentaneamente questo è solo un patch ma non un update.
    public RentalModel patchRental(Long id, RentalModel rentalModel) {
        Optional<RentalEntity> foundRental = rentalRepository.findById(id);
        if (foundRental.isPresent()) {
            RentalEntity rentalEntity = foundRental.get();
            if (!rentalModel.getStartOfRental().isEqual(rentalEntity.getStartOfRental())) {
                rentalEntity.setStartOfRental(rentalModel.getStartOfRental());
            }
            if (!rentalModel.getEndOfRental().isEqual(rentalEntity.getEndOfRental())) {
                rentalEntity.setEndOfRental(rentalModel.getEndOfRental());
            }
            if (rentalEntity.getDailyCostRental().isNaN()) {
                rentalEntity.setDailyCostRental(rentalModel.getDailyCostRental());
            }
            if (rentalEntity.getTotalCostRental().isNaN()) {
                rentalEntity.setTotalCostRental(rentalModel.getTotalCostRental());
            }
            if (rentalEntity.getVehicleEntity() == null) {
                if (rentalModel.getVehicleEntity() != null) {
                    rentalEntity.setVehicleEntity(rentalModel.getVehicleEntity());
                }
            }
            if (rentalEntity.getUserEntity() == null) {
                if (rentalModel.getUserEntity() != null) {
                    rentalEntity.setUserEntity(rentalModel.getUserEntity());
                }
            }
            RentalEntity dbSaved = rentalRepository.saveAndFlush(rentalEntity);
            return rentalMapper.convertRentalEntityToModel(dbSaved);

        } else return null;
    }

    public RentalModel updateRental(Long id, RentalModel rentalModel) {
        Optional<RentalEntity> foundRental = rentalRepository.findById(id);
        if (foundRental.isPresent()) {
            RentalEntity rentalEntity = foundRental.get();
            rentalEntity.setStartOfRental(rentalModel.getStartOfRental());
            rentalEntity.setEndOfRental(rentalModel.getEndOfRental());
            rentalEntity.setTotalCostRental(rentalModel.getTotalCostRental());
            rentalEntity.setDailyCostRental(rentalModel.getDailyCostRental());
            rentalEntity.setVehicleEntity(rentalModel.getVehicleEntity());
            rentalEntity.setUserEntity(rentalModel.getUserEntity());
            rentalEntity.setStatus(rentalModel.getStatus());
            RentalEntity dbSaved = rentalRepository.saveAndFlush(rentalEntity);
            return rentalMapper.convertRentalEntityToModel(dbSaved);
        }
        return null;
    }

    public RentalModel createRentalForUser(Long idUser, RentalModel rentalModel) {
        Optional<UserEntity> user = userRepository.findById(idUser);
        if (user.isPresent()) {
            RentalModel dbRentalModel = createRental(rentalModel);
            dbRentalModel.setUserEntity(user.get());
            RentalModel dbRentalModelUpdated = updateRental(dbRentalModel.getId(), dbRentalModel);
            return dbRentalModelUpdated;
        }
        return null;
    }

    public Boolean deleteRentalForUser(Long idUser, Long idRental) {
        Optional<UserEntity> user = userRepository.findById(idUser);
        if (user.isPresent()) {
            Optional<RentalEntity> rental = rentalRepository.findByUserIdAndRentalId(idUser, idRental);
            if (rental.isPresent()) {
                rentalRepository.deleteById(idRental);
                return true;
            }
        }
        return false;
    }

    public RentalModel updateRentalForUser(Long idUser, RentalModel rentalModel, Long rentalID) {
        Optional<UserEntity> user = userRepository.findById(idUser);
        if (user.isPresent()) {
            Optional<RentalEntity> rental = rentalRepository.findByUserIdAndRentalId(idUser, rentalID);
            if (rental.isPresent()) {
                return updateRental(rentalID, rentalModel);
            }
        }
        return null;
    }

    public List<RentalModel> getAllRentals (long userID) {
        List<RentalEntity> rentalLIst = rentalRepository.findByUserID(userID);
        List<RentalModel> modelList = new ArrayList<>();
        for(RentalEntity n : rentalLIst){
            modelList.add(rentalMapper.convertRentalEntityToModel(n));
        } return modelList;
    }
}

