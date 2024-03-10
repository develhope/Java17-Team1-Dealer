package com.develhope.spring.features.rentals;

import com.develhope.spring.features.rentals.dto.CreateRentalRequest;
import com.develhope.spring.features.rentals.dto.PatchRentalRequest;
import com.develhope.spring.features.rentals.dto.RentalResponse;
import com.develhope.spring.features.users.UserEntity;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RentalController {

    public static final String RENTAL_PATH = "/rentals";
    public static final String RENTAL_PATH_ID = RENTAL_PATH + "/{orderId}";

    private final RentalService rentalService;

    @PutMapping(path = RENTAL_PATH + "/create")
    public ResponseEntity<?> createRentalByVehicleId(@AuthenticationPrincipal UserEntity user,
                                                     @RequestBody CreateRentalRequest rentalRequest) {
        RentalResponse rentalResponse = rentalService.createRentalByVehicleId(user, rentalRequest);
        if (rentalResponse == null) {
            return new ResponseEntity<>(rentalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(rentalResponse, HttpStatus.OK);
    }

    @PatchMapping(path = RENTAL_PATH_ID)
    public RentalResponse patchRental(@AuthenticationPrincipal UserEntity user,
                                      @PathVariable Long rentalId,
                                      @RequestBody PatchRentalRequest patchRentalRequest) {
        return rentalService.patchRental(user, rentalId, patchRentalRequest);
    }


    @GetMapping(path = RENTAL_PATH + "/byuser/{userId}")
    public ResponseEntity<?> getRentalsByUserId(@AuthenticationPrincipal UserEntity user,
                                                @PathVariable Long userId) {
        return rentalService.getRentalListByRenterId(user, userId);
    }

    @DeleteMapping(path = RENTAL_PATH_ID)
    public ResponseEntity<?> deleteRental(@AuthenticationPrincipal UserEntity user,
                                          @PathVariable Long rentalId) {
        if (rentalService.deleteRental(user, rentalId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
