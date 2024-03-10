package com.develhope.spring.features.orders;

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

    public Boolean deleteOrder(UserEntity user, Long id_do_delete) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(id_do_delete);
        if (orderEntity.isEmpty()) {
            return false; //order of orderId not exists
        }

        UserEntity userBuyer = orderEntity.get().getBuyer();
        if (userBuyer == null) {
            return false;
        }

        UserEntity userSeller = orderEntity.get().getSeller();
        if (userSeller == null) {
            return false;
        }

        if (user.getRole() != Role.ADMIN) {
            if (user.getRole() == Role.SELLER) {
                if (user.getId() != userSeller.getId()) {
                    return false;
                }
            } else {
                if (user.getId() != userBuyer.getId()) {
                    return false;
                }
            }
        }
        orderRepository.deleteById(id_do_delete);
        return true;
    }

    public OrderResponse patchOrder(UserEntity user, Long orderId, PatchOrderRequest patchOrderRequest) {
        Optional<OrderEntity> foundOrderEntity = orderRepository.findById(orderId);
        if (foundOrderEntity.isEmpty()) {
            return null; //order of orderId not exists
        }

        UserEntity userBuyer = foundOrderEntity.get().getBuyer();
        if (userBuyer == null) {
            return null;
        }

        UserEntity userSeller = foundOrderEntity.get().getSeller();
        if (userSeller == null) {
            return null;
        }

        final var sellerSameUser = userSeller.getId().equals(user.getId());

        userSeller = sellerSameUser ? user : userSeller;

        if (user.getRole() != Role.ADMIN && user.getRole() == Role.SELLER && !sellerSameUser) {
            return null;
        }

        final var customerSameUser = userBuyer.getId().equals(user.getId());
        userBuyer = customerSameUser ? user : userBuyer;

        if (user.getRole() != Role.ADMIN && user.getRole() == Role.CUSTOMER && !customerSameUser) {
            return null;
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
        return orderMapper.convertOrderEntityToResponse(orderRepository.save(foundOrderEntity.get()));

    }


    public OrderStatus getOrderStatusFromId(Long orderId, Long requester_id) {
        Optional<OrderEntity> foundOrderEntity = orderRepository.findById(orderId);
        if (foundOrderEntity.isEmpty()) {
            return null; //order of orderId not exists
        }

        Optional<UserEntity> userRequester = userRepository.findById(requester_id);
        if (userRequester.isEmpty()) {
            return null;
        }

        UserEntity userBuyer = foundOrderEntity.get().getBuyer();
        if (userBuyer == null) {
            return null;
        }

        UserEntity userSeller = foundOrderEntity.get().getSeller();
        if (userSeller == null) {
            return null;
        }

        if (userRequester.get().getRole() != Role.ADMIN) {
            if (userRequester.get().getRole() == Role.SELLER) {
                if (userRequester.get().getId() != userSeller.getId()) {
                    return null;
                }
            } else {
                if (userRequester.get().getId() != userBuyer.getId()) {
                    return null;
                }
            }
        }

        return foundOrderEntity.get().getOrderStatus();
    }

    public ResponseEntity<?> getOrderListByBuyerId(UserEntity user, Long buyerId) {
        if (user.getRole() == Role.CUSTOMER) {
            if (user.getId() != buyerId) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
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

    public OrderResponse patchOrderStatus(UserEntity user, Long orderId, String status) {
        final String statusString = status.toUpperCase();
        if (OrderStatus.isValidOrderStatus(statusString)) {
            return null;
        }

        Optional<OrderEntity> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            return null;
        }

        UserEntity userBuyer = order.get().getBuyer();
        if (userBuyer == null) {
            return null;
        }

        UserEntity userSeller = order.get().getSeller();
        if (userSeller == null) {
            return null;
        }

        final var sellerSameUser = userSeller.getId().equals(user.getId());

        userSeller = sellerSameUser ? user : userSeller;

        if (user.getRole() != Role.ADMIN && user.getRole() == Role.SELLER && !sellerSameUser) {
            return null;
        }

        order.get().setOrderStatus(OrderStatus.valueOf(statusString));
        return orderMapper.convertOrderEntityToResponse(orderRepository.save(order.get()));
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


    public OrderStatus getOrderStatus(UserEntity user, Long orderId) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isEmpty()) {
            return null;
        }

        UserEntity userBuyer = orderEntity.get().getBuyer();
        if (userBuyer == null) {
            return null;
        }

        UserEntity userSeller = orderEntity.get().getSeller();
        if (userSeller == null) {
            return null;
        }

        if (user.getRole() != Role.ADMIN) {
            if (user.getRole() == Role.SELLER) {
                if (user.getId() != userSeller.getId()) {
                    return null;
                }
            } else {
                if (user.getId() != userBuyer.getId()) {
                    return null;
                }
            }
        }

        return orderEntity.get().getOrderStatus();
    }


    public OrderResponse prepareOrderByVehicleId(UserEntity user, CreateOrderRequest orderRequest) {
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(orderRequest.getVehicleId());
        if (vehicleEntity.isEmpty()) {
            return null; //invalid vehicle id
        }

        if (orderRequest.getSellerId() == orderRequest.getCustomerId()) {
            return null;
        }

        if (user.getRole() == Role.SELLER) {
            if (orderRequest.getSellerId() != user.getId()) {
                return null;
            }
        }

        if (orderRequest.getDeposit() <= 0) {
            return null; //invalid deposit
        }

        final var sellerSameUser = orderRequest.getSellerId().equals(user.getId());

        Optional<UserEntity> userSeller = sellerSameUser ? Optional.of(user) : userRepository.findById(orderRequest.getSellerId());
        if (userSeller.isEmpty()) {
            return null; //invalid seller
        }

        if (userSeller.get().getRole() != Role.SELLER) {
            return null;
        }

        //probably definitely redundant
        if (user.getRole() != Role.ADMIN && user.getRole() == Role.SELLER && userSeller.get().getId() != user.getId()) {
            return null;
        }

        final var customerSameUser = orderRequest.getCustomerId().equals(user.getId());
        Optional<UserEntity> userBuyer = customerSameUser ? Optional.of(user) : userRepository.findById(orderRequest.getCustomerId());
        if (userBuyer.isEmpty()) {
            return null; //invalid seller
        }

        if (user.getRole() != Role.ADMIN && user.getRole() == Role.CUSTOMER && userBuyer.get().getId() != user.getId()) {
            return null;
        }

        if (orderRequest.getDeposit() > vehicleEntity.get().getPrice()) {
            return null; //invalid price
        }

        if (vehicleEntity.get().getVehicleStatus() != VehicleStatus.CAN_BE_ORDERED) {
            return null;
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
        return orderMapper.convertOrderEntityToResponse(orderEntitySaved);
    }

    public OrderResponse createOrderByVehicleId(UserEntity user, CreateOrderRequest orderRequest) {
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(orderRequest.getVehicleId());
        if (vehicleEntity.isEmpty()) {
            return null; //invalid vehicle id
        }

        if (orderRequest.getDeposit() <= 0) {
            return null; //invalid deposit
        }

        if (user.getRole() == Role.SELLER) {
            if (orderRequest.getSellerId() != user.getId()) {
                return null;
            }
        }

        final var sellerSameUser = orderRequest.getSellerId().equals(user.getId());

        Optional<UserEntity> userSeller = sellerSameUser ? Optional.of(user) : userRepository.findById(orderRequest.getSellerId());
        if (userSeller.isEmpty()) {
            return null; //invalid seller
        }

        if (userSeller.get().getRole() != Role.SELLER) {
            return null;
        }

        if (user.getRole() != Role.ADMIN && user.getRole() == Role.SELLER && userSeller.get().getId() != user.getId()) {
            return null;
        }

        final var customerSameUser = orderRequest.getCustomerId().equals(user.getId());
        Optional<UserEntity> userBuyer = customerSameUser ? Optional.of(user) : userRepository.findById(orderRequest.getCustomerId());
        if (userBuyer.isEmpty()) {
            return null; //invalid seller
        }

        if (user.getRole() != Role.ADMIN && user.getRole() == Role.CUSTOMER && userBuyer.get().getId() != user.getId()) {
            return null;
        }

        if (orderRequest.getDeposit() > vehicleEntity.get().getPrice()) {
            return null; //invalid price
        }

        if (vehicleEntity.get().getVehicleStatus() != VehicleStatus.PROMPT_DELIVERY) {
            return null;
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
        return orderMapper.convertOrderEntityToResponse(orderEntitySaved);
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
