package com.develhope.spring.features.rentals;

import com.develhope.spring.exception.NotFoundException;
import com.develhope.spring.features.rentals.dto.RentalResponse;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalController {

}
