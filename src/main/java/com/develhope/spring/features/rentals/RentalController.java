package com.develhope.spring.features.rentals;

import com.develhope.spring.features.rentals.dto.CreateRentalRequest;
import com.develhope.spring.features.rentals.dto.PatchRentalRequest;
import com.develhope.spring.features.rentals.dto.RentalResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RentalController {

    public static final String RENTAL_PATH = "/rentals";
    public static final String RENTAL_PATH_ID = RENTAL_PATH + "/{orderId}";

    private final RentalService rentalService;

    @PutMapping(path = RENTAL_PATH + "/create")
    public ResponseEntity<?> createRentalByVehicleId(@RequestBody CreateRentalRequest rentalRequest,
                                                     @RequestParam(required = true) Long requester_id) {
        RentalResponse rentalResponse = rentalService.createRentalByVehicleId(rentalRequest, requester_id);
        if (rentalResponse == null) {
            return new ResponseEntity<>(rentalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(rentalResponse, HttpStatus.OK);
    }

    @PatchMapping(path = RENTAL_PATH_ID)
    public RentalResponse patchRental(@PathVariable Long rentalId, @RequestBody PatchRentalRequest patchRentalRequest, @RequestParam(required = true) Long requester_id) {
        return rentalService.patchRental(rentalId, patchRentalRequest, requester_id);
    }


    @GetMapping(path = RENTAL_PATH + "/byuser/{userId}")
    public ResponseEntity<?> getRentalsByUserId(@PathVariable Long userId, @RequestParam(required = true) Long requester_id) {
        List<RentalResponse> rentalResponseList = rentalService.getRentalListByRenterId(userId, requester_id);
        return new ResponseEntity<>(rentalResponseList, HttpStatus.OK);
    }

    @DeleteMapping(path = RENTAL_PATH_ID)
    public ResponseEntity<?> deleteRental(@PathVariable Long rentalId, @RequestParam(required = true) Long requester_id) {
        if (rentalService.deleteRental(rentalId, requester_id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
