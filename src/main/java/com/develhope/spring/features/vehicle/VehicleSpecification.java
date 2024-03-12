package com.develhope.spring.features.vehicle;

import com.develhope.spring.features.vehicle.PropertiesEnum.FuelType;
import com.develhope.spring.features.vehicle.PropertiesEnum.ShiftType;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class VehicleSpecification {
    public static Specification<VehicleEntity> filterVehicleEntity(
            String model,
            String brand,
            Integer displacement,
            String color,
            Integer power,
            String shiftType,
            Integer yearOfMatriculation,
            String fuelType,
            Long price,
            Long dailyCostRental,
            Integer discount,
            String accessories,
            Boolean used,
            String vehicleStatus,
            String vehicleType) {
        return (root, query, criteriaBuilder) -> {
            Predicate modelPredicate = criteriaBuilder.like(root.get("model"), StringUtils.isBlank(model) ? likePattern("") : model);
            Predicate brandPredicate = criteriaBuilder.like(root.get("brand"), StringUtils.isBlank(brand) ? likePattern("") : brand);
            Predicate displacementPredicate = criteriaBuilder.equal(root.get("displacement"), displacement);
            Predicate colorPredicate = criteriaBuilder.like(root.get("color"), StringUtils.isBlank(color) ? likePattern("") : color);
            Predicate powerPredicate = criteriaBuilder.equal(root.get("power"), power);
            Predicate shiftTypePredicate = criteriaBuilder.equal(root.get("shiftType"), ShiftType.isValidShiftType(shiftType) ? ShiftType.valueOf(shiftType) : ShiftType.MANUAL);
            Predicate yearOfMatriculationPredicate = criteriaBuilder.equal(root.get("yearOfMatriculation"), yearOfMatriculation);
            Predicate fuelTypePredicate = criteriaBuilder.equal(root.get("fuelType"), FuelType.isValidFuelType(fuelType) ? FuelType.valueOf(fuelType) : FuelType.PETROL);
            Predicate pricePredicate = criteriaBuilder.equal(root.get("price"), price);
            Predicate dailyCostRentalPredicate = criteriaBuilder.equal(root.get("dailyCostRental"), dailyCostRental);
            Predicate discountPredicate = criteriaBuilder.equal(root.get("discount"), discount);
            Predicate accessoriesPredicate = criteriaBuilder.like(root.get("accessories"), StringUtils.isBlank(accessories) ? likePattern("") : accessories);
            Predicate usedPredicate = criteriaBuilder.equal(root.get("used"), used);
            Predicate vehicleStatusPredicate = criteriaBuilder.equal(root.get("vehicleStatus"), VehicleStatus.isValidVehicleStatus(vehicleStatus) ? VehicleStatus.valueOf(vehicleStatus) : VehicleStatus.CAN_BE_ORDERED);
            Predicate vehicleTypePredicate = criteriaBuilder.equal(root.get("vehicleType"), VehicleType.isValidVehicleType(vehicleType) ? VehicleType.valueOf(vehicleType) : VehicleType.CAR);
            return criteriaBuilder.and(
                    modelPredicate,
                    brandPredicate,
                    displacementPredicate,
                    colorPredicate,
                    powerPredicate,
                    shiftTypePredicate,
                    yearOfMatriculationPredicate,
                    fuelTypePredicate,
                    pricePredicate,
                    dailyCostRentalPredicate,
                    discountPredicate,
                    accessoriesPredicate,
                    usedPredicate,
                    vehicleStatusPredicate,
                    vehicleTypePredicate
            );
        };

    }

    private static String likePattern(String value) {
        return "%" + value + "%";
    }
}