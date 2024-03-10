package com.develhope.spring.features.orders;

import com.develhope.spring.exception.NotFoundException;
import com.develhope.spring.exception.UnauthorizedException;
import com.develhope.spring.features.orders.dto.CreateOrderRequest;
import com.develhope.spring.features.orders.dto.OrderResponse;
import com.develhope.spring.features.orders.dto.PatchOrderRequest;
import com.develhope.spring.features.users.Role;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.users.UserRepository;
import com.develhope.spring.features.vehicle.VehicleEntity;
import com.develhope.spring.features.vehicle.VehicleRepository;
import com.develhope.spring.features.vehicle.VehicleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    private final OrderMapper orderMapper;

    public Boolean deleteOrder(UserEntity user, Long id_to_delete) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(id_to_delete);
        if (orderEntity.isEmpty()) {
            throw new NotFoundException("Order with id: " + id_to_delete + " not found"); //order of orderId not exists
        }

        UserEntity userBuyer = orderEntity.get().getBuyer();
        if (userBuyer == null) {
            throw new NotFoundException("Buyer in the order with id: " + id_to_delete + " not found");
        }

        UserEntity userSeller = orderEntity.get().getSeller();
        if (userSeller == null) {
            throw new NotFoundException("Seller in the order with id: " + id_to_delete + " not found");
        }

        if (user.getRole() != Role.ADMIN) {
            if (user.getRole() == Role.SELLER) {
                if (user.getId() != userSeller.getId()) {
                    throw new UnauthorizedException();
                }
            } else {
                if (user.getId() != userBuyer.getId()) {
                    throw new UnauthorizedException();
                }
            }
        }
        orderRepository.deleteById(id_to_delete);
        return true;
    }

    public ResponseEntity<?> patchOrder(UserEntity user, Long orderId, PatchOrderRequest patchOrderRequest) {
        Optional<OrderEntity> foundOrderEntity = orderRepository.findById(orderId);
        if (foundOrderEntity.isEmpty()) {
            return new ResponseEntity<>("Id: " + orderId + " not found.", HttpStatus.NOT_FOUND);
        }

        UserEntity userBuyer = foundOrderEntity.get().getBuyer();
        if (userBuyer == null) {
            return new ResponseEntity<>("Invalid buyer in the order with id: " + orderId + ".", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        UserEntity userSeller = foundOrderEntity.get().getSeller();
        if (userSeller == null) {
            return new ResponseEntity<>("Invalid seller in the order with id: " + orderId + ".", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        final var sellerSameUser = userSeller.getId().equals(user.getId());

        userSeller = sellerSameUser ? user : userSeller;

        if (user.getRole() != Role.ADMIN && user.getRole() == Role.SELLER && !sellerSameUser) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        final var customerSameUser = userBuyer.getId().equals(user.getId());
        userBuyer = customerSameUser ? user : userBuyer;

        if (user.getRole() != Role.ADMIN && user.getRole() == Role.CUSTOMER && !customerSameUser) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }


        final var orderRequestPaymentStatusString = patchOrderRequest.getPaymentStatus().toUpperCase();
        if (OrderStatus.isValidOrderStatus(orderRequestPaymentStatusString)) {
            foundOrderEntity.get().setPaymentStatus(PaymentStatus.valueOf(orderRequestPaymentStatusString));
        }

        final var orderRequestOrderStatusString = patchOrderRequest.getOrderStatus().toUpperCase();
        if (OrderStatus.isValidOrderStatus(orderRequestOrderStatusString)) {
            foundOrderEntity.get().setOrderStatus(OrderStatus.valueOf(orderRequestOrderStatusString));
        }

        if (patchOrderRequest.getDeposit() >= 0) {
            foundOrderEntity.get().setDeposit(patchOrderRequest.getDeposit());
        }

        if (patchOrderRequest.getVehicleEntity() != null) {
            foundOrderEntity.get().setVehicleEntity(patchOrderRequest.getVehicleEntity());
        }
        OrderResponse orderResponse = orderMapper.convertOrderEntityToResponse(orderRepository.save(foundOrderEntity.get()));
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }


    public ResponseEntity<?> getOrderListByBuyerId(UserEntity user, Long buyerId) {
        if (user.getRole() == Role.CUSTOMER) {
            if (user.getId() != buyerId) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
        }

        switch (user.getRole()) {
            case ADMIN:
            case CUSTOMER: {
                var orderEntityList = orderRepository.findAllByBuyerId(buyerId);
                return new ResponseEntity<>(orderMapper.mapList(orderEntityList, OrderResponse.class), HttpStatus.OK);
            }
            case SELLER: {
                if (user.getId() != buyerId) {
                    var orderEntityList = orderRepository.findAllByBuyerIdAndSellerId(buyerId, user.getId());
                    return new ResponseEntity<>(orderMapper.mapList(orderEntityList, OrderResponse.class), HttpStatus.OK);
                } else { //fallback
                    var orderEntityList = orderRepository.findAllBySellerId(user.getId());
                    return new ResponseEntity<>(orderMapper.mapList(orderEntityList, OrderResponse.class), HttpStatus.OK);
                }
            }
            default:
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    public ResponseEntity<?> getOrdersCompletedListByBuyerId(UserEntity user, Long buyerId) {
        if (user.getRole() == Role.CUSTOMER) {
            if (user.getId() != buyerId) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }

        switch (user.getRole()) {
            case ADMIN:
            case CUSTOMER: {
                var orderEntityList = orderRepository.findAllByBuyerPaymentStatusIsPaid(buyerId);
                return new ResponseEntity<>(orderMapper.mapList(orderEntityList, OrderResponse.class), HttpStatus.OK);
            }
            case SELLER: {
                if (user.getId() != buyerId) {
                    var orderEntityList = orderRepository.findAllByBuyerAndSellerPaymentStatusIsPaid(buyerId, user.getId());
                    return new ResponseEntity<>(orderMapper.mapList(orderEntityList, OrderResponse.class), HttpStatus.OK);
                } else {
                    var orderEntityList = orderRepository.findAllBySellerPaymentStatusIsPaid(user.getId());
                    return new ResponseEntity<>(orderMapper.mapList(orderEntityList, OrderResponse.class), HttpStatus.OK);
                }
            }
            default:
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> patchOrderStatus(UserEntity user, Long orderId, String status) {
        final String statusString = status.toUpperCase();
        if (OrderStatus.isValidOrderStatus(statusString)) {
            return new ResponseEntity<>("Invalid status for id: " + orderId + ".", HttpStatus.BAD_REQUEST);
        }

        Optional<OrderEntity> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            return new ResponseEntity<>("Id: " + orderId + " not found.", HttpStatus.NOT_FOUND);
        }

        UserEntity userBuyer = order.get().getBuyer();
        if (userBuyer == null) {
            return new ResponseEntity<>("Invalid buyer for id: " + orderId + ".", HttpStatus.BAD_REQUEST);
        }

        UserEntity userSeller = order.get().getSeller();
        if (userSeller == null) {
            return new ResponseEntity<>("Invalid seller for id: " + orderId + ".", HttpStatus.BAD_REQUEST);
        }

        final var sellerSameUser = userSeller.getId().equals(user.getId());

        userSeller = sellerSameUser ? user : userSeller;

        if (user.getRole() != Role.ADMIN && user.getRole() == Role.SELLER && !sellerSameUser) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        order.get().setOrderStatus(OrderStatus.valueOf(statusString));
        OrderResponse orderResponse = orderMapper.convertOrderEntityToResponse(orderRepository.save(order.get()));
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> getOrdersByStatus(UserEntity user, String status) {
        final String statusString = status.toUpperCase();
        if (!OrderStatus.isValidOrderStatus(status)) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        switch (user.getRole()) {
            case ADMIN: {
                var orderEntityList = orderRepository.findByOrderStatus(OrderStatus.valueOf(statusString));
                return new ResponseEntity<>(orderMapper.mapList(orderEntityList, OrderResponse.class), HttpStatus.OK);
            }
            case SELLER: {
                var orderEntityList = orderRepository.findByOrderStatusSeller(OrderStatus.valueOf(statusString), user.getId());
                return new ResponseEntity<>(orderMapper.mapList(orderEntityList, OrderResponse.class), HttpStatus.OK);
            }
            case CUSTOMER: {
                var orderEntityList = orderRepository.findByOrderStatusBuyer(OrderStatus.valueOf(statusString), user.getId());
                return new ResponseEntity<>(orderMapper.mapList(orderEntityList, OrderResponse.class), HttpStatus.OK);
            }
            default:
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }


    public ResponseEntity<?> getOrderStatus(UserEntity user, Long orderId) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isEmpty()) {
            return new ResponseEntity<>("Id: " + orderId + " not found.", HttpStatus.NOT_FOUND);
        }

        UserEntity userBuyer = orderEntity.get().getBuyer();
        if (userBuyer == null) {
            return new ResponseEntity<>("Invalid buyer for id: " + orderId + ".", HttpStatus.BAD_REQUEST);
        }

        UserEntity userSeller = orderEntity.get().getSeller();
        if (userSeller == null) {
            return new ResponseEntity<>("Invalid seller for id: " + orderId + ".", HttpStatus.BAD_REQUEST);
        }

        if (user.getRole() != Role.ADMIN) {
            if (user.getRole() == Role.SELLER) {
                if (user.getId() != userSeller.getId()) {
                    return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
                }
            } else {
                if (user.getId() != userBuyer.getId()) {
                    return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
                }
            }
        }
        return new ResponseEntity<>(orderEntity.get().getOrderStatus(), HttpStatus.OK);
    }


    public ResponseEntity<?> prepareOrderByVehicleId(UserEntity user, CreateOrderRequest orderRequest) {
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(orderRequest.getVehicleId());
        if (vehicleEntity.isEmpty()) {
            return new ResponseEntity<>("Vehicle: " + orderRequest.getVehicleId() + " not found.", HttpStatus.NOT_FOUND);
        }

        if (orderRequest.getSellerId() == orderRequest.getCustomerId()) {
            return new ResponseEntity<>("Customer id cannot be the same as the seller id", HttpStatus.BAD_REQUEST);
        }

        if (user.getRole() == Role.SELLER) {
            if (orderRequest.getSellerId() != user.getId()) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
        }

        if (orderRequest.getDeposit() <= 0) {
            return new ResponseEntity<>("Invalid deposit", HttpStatus.BAD_REQUEST);
        }

        final var sellerSameUser = orderRequest.getSellerId().equals(user.getId());

        Optional<UserEntity> userSeller = sellerSameUser ? Optional.of(user) : userRepository.findById(orderRequest.getSellerId());
        if (userSeller.isEmpty()) {
            return new ResponseEntity<>("Invalid seller", HttpStatus.BAD_REQUEST);
        }

        if (userSeller.get().getRole() != Role.SELLER) {
            return new ResponseEntity<>("Invalid seller", HttpStatus.BAD_REQUEST);
        }

        //probably definitely redundant
        if (user.getRole() != Role.ADMIN && user.getRole() == Role.SELLER && userSeller.get().getId() != user.getId()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        final var customerSameUser = orderRequest.getCustomerId().equals(user.getId());
        Optional<UserEntity> userBuyer = customerSameUser ? Optional.of(user) : userRepository.findById(orderRequest.getCustomerId());
        if (userBuyer.isEmpty()) {
            return new ResponseEntity<>("Invalid buyer", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (user.getRole() != Role.ADMIN && user.getRole() == Role.CUSTOMER && userBuyer.get().getId() != user.getId()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        if (orderRequest.getDeposit() > vehicleEntity.get().getPrice()) {
            return new ResponseEntity<>("Invalid deposit", HttpStatus.BAD_REQUEST);
        }

        if (vehicleEntity.get().getVehicleStatus() != VehicleStatus.CAN_BE_ORDERED) {
            return new ResponseEntity<>("Invalid status", HttpStatus.BAD_REQUEST);
        }


        final PaymentStatus paymentStatus = orderRequest.getDeposit() == vehicleEntity.get().getPrice() ? PaymentStatus.PAID : PaymentStatus.DEPOSIT;
        OrderEntity orderEntity = orderMapper.convertOrderRequestToEntity(orderRequest);
        orderEntity.setOrderStatus(OrderStatus.TO_SEND);
        orderEntity.setPaymentStatus(paymentStatus);
        orderEntity.setVehicleEntity(vehicleEntity.get());
        orderEntity.setBuyer(userBuyer.get());
        orderEntity.setSeller(userSeller.get());
        Long totalPrice = (vehicleEntity.get().getDiscount() / 100 * vehicleEntity.get().getPrice()) + vehicleEntity.get().getPrice();
        orderEntity.setOrderPrice(totalPrice);
        OrderEntity orderEntitySaved = orderRepository.saveAndFlush(orderEntity);
        OrderResponse orderResponse = orderMapper.convertOrderEntityToResponse(orderEntitySaved);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> createOrderByVehicleId(UserEntity user, CreateOrderRequest orderRequest) {
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(orderRequest.getVehicleId());
        if (vehicleEntity.isEmpty()) {
            return new ResponseEntity<>("Vechile with id: " + orderRequest.getVehicleId() + "not found.", HttpStatus.NOT_FOUND);
        }

        if (orderRequest.getDeposit() <= 0) {
            return new ResponseEntity<>("Invalid deposit", HttpStatus.BAD_REQUEST);
        }

        if (user.getRole() == Role.SELLER) {
            if (orderRequest.getSellerId() != user.getId()) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
        }

        final var sellerSameUser = orderRequest.getSellerId().equals(user.getId());

        Optional<UserEntity> userSeller = sellerSameUser ? Optional.of(user) : userRepository.findById(orderRequest.getSellerId());
        if (userSeller.isEmpty()) {
            return new ResponseEntity<>("Invalid seller", HttpStatus.BAD_REQUEST);
        }

        if (userSeller.get().getRole() != Role.SELLER) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        if (user.getRole() != Role.ADMIN && user.getRole() == Role.SELLER && userSeller.get().getId() != user.getId()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        final var customerSameUser = orderRequest.getCustomerId().equals(user.getId());
        Optional<UserEntity> userBuyer = customerSameUser ? Optional.of(user) : userRepository.findById(orderRequest.getCustomerId());
        if (userBuyer.isEmpty()) {
            return new ResponseEntity<>("Invalid buyer", HttpStatus.BAD_REQUEST);
        }

        if (user.getRole() != Role.ADMIN && user.getRole() == Role.CUSTOMER && userBuyer.get().getId() != user.getId()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        if (orderRequest.getDeposit() > vehicleEntity.get().getPrice()) {
            return new ResponseEntity<>("Invalid deposit", HttpStatus.BAD_REQUEST);
        }

        if (vehicleEntity.get().getVehicleStatus() != VehicleStatus.PROMPT_DELIVERY) {
            return new ResponseEntity<>("Invalid status", HttpStatus.BAD_REQUEST);
        }

        final PaymentStatus paymentStatus = orderRequest.getDeposit() == vehicleEntity.get().getPrice() ? PaymentStatus.PAID : PaymentStatus.DEPOSIT;

        OrderEntity orderEntity = orderMapper.convertOrderRequestToEntity(orderRequest);
        orderEntity.setOrderStatus(OrderStatus.TO_SEND);
        orderEntity.setPaymentStatus(paymentStatus);
        orderEntity.setVehicleEntity(vehicleEntity.get());
        orderEntity.setBuyer(userBuyer.get());
        orderEntity.setSeller(userSeller.get());
        Long totalPrice = (vehicleEntity.get().getDiscount() / 100 * vehicleEntity.get().getPrice()) + vehicleEntity.get().getPrice();
        orderEntity.setOrderPrice(totalPrice);
        OrderEntity orderEntitySaved = orderRepository.saveAndFlush(orderEntity);
        OrderResponse orderResponse = orderMapper.convertOrderEntityToResponse(orderEntitySaved);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> getTotalSalesPriceInAPeriod(String startDate, String endDate) {
        try {
            OffsetDateTime.parse(startDate);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            OffsetDateTime.parse(endDate);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Long totalPrice = orderRepository.getTotalSalesPriceInAPeriod(OffsetDateTime.parse(startDate), OffsetDateTime.parse(endDate));
        return new ResponseEntity<>(totalPrice, HttpStatus.OK);
    }
}
