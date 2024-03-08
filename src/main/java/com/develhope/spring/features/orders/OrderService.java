package com.develhope.spring.features.orders;

import com.develhope.spring.features.orders.dto.CreateOrderRequest;
import com.develhope.spring.features.orders.dto.OrderResponse;
import com.develhope.spring.features.orders.dto.PatchOrderRequest;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.users.UserRepository;
import com.develhope.spring.features.users.UserType;
import com.develhope.spring.features.vehicle.VehicleEntity;
import com.develhope.spring.features.vehicle.VehicleRepository;
import com.develhope.spring.features.vehicle.VehicleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    private final OrderMapper orderMapper;

    public Boolean deleteOrder(Long orderId, Long requester_id) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isEmpty()) {
            return false; //order of orderId not exists
        }

        UserEntity userBuyer = orderEntity.get().getBuyer();
        if (userBuyer == null) {
            return false;
        }

        Optional<UserEntity> userRequester = userRepository.findById(requester_id);

        if (userRequester.isEmpty()) {
            return false;
        }

        UserEntity userSeller = orderEntity.get().getSeller();
        if (userSeller == null) {
            return false;
        }

        if (userRequester.get().getUserType() != UserType.ADMIN) {
            if (userRequester.get().getUserType() == UserType.SELLER) {
                if (userRequester.get().getId() != userSeller.getId()) {
                    return false;
                }
            } else {
                if (userRequester.get().getId() != userBuyer.getId()) {
                    return false;
                }
            }
        }
        orderRepository.deleteById(orderId);
        return true;
    }

    public OrderResponse patchOrder(Long orderId, PatchOrderRequest patchOrderRequest, Long requester_id) {
        Optional<UserEntity> userRequester = userRepository.findById(requester_id);
        if (userRequester.isEmpty()) {
            return null;
        }

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

        if (userRequester.get().getUserType() != UserType.ADMIN) {
            if (userRequester.get().getUserType() == UserType.SELLER) {
                if (userRequester.get().getId() != userSeller.getId()) {
                    return null;
                }
            } else {
                if (userRequester.get().getId() != userBuyer.getId()) {
                    return null;
                }
            }
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
            //CHECK VEHICLE ENTITY
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

        if (userRequester.get().getUserType() != UserType.ADMIN) {
            if (userRequester.get().getUserType() == UserType.SELLER) {
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

    public List<OrderResponse> getOrderListById(Long userId, Long requester_id) {
        Optional<UserEntity> userRequester = userRepository.findById(requester_id);
        if (userRequester.isEmpty()) {
            return null;
        }

        if (userRequester.get().getUserType() != UserType.ADMIN) {
            if (userRequester.get().getId() != userId) {
                return null;
            }
        }

        List<OrderEntity> orderEntityList = orderRepository.findAllByBuyer(userId);
        return orderMapper.mapList(orderEntityList, OrderResponse.class);
    }

    public List<OrderResponse> getOrdersCompletedListById(Long userId, Long requester_id) {
        Optional<UserEntity> userRequester = userRepository.findById(requester_id);
        if (userRequester.isEmpty()) {
            return null;
        }

        if (userRequester.get().getUserType() != UserType.ADMIN) {
            if (userRequester.get().getId() != userId) {
                return null;
            }
        }

        List<OrderEntity> ordersCompleteEntityList = orderRepository.findAllByBuyerPaymentStatusIsPaid(userId);
        return orderMapper.mapList(ordersCompleteEntityList, OrderResponse.class);
    }

    public OrderResponse patchOrderStatus(Long orderId, String status, Long requester_id) {
        final String statusString = status.toUpperCase();
        if (OrderStatus.isValidOrderStatus(statusString)) {
            return null;
        }

        Optional<OrderEntity> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            return null;
        }

        Optional<UserEntity> userRequester = userRepository.findById(requester_id);
        if (userRequester.isEmpty()) {
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

        if (userRequester.get().getUserType() != UserType.ADMIN) {
            if (userRequester.get().getUserType() == UserType.SELLER) {
                if (userRequester.get().getId() != userSeller.getId()) {
                    return null;
                }
            } else {
                if (userRequester.get().getId() != userBuyer.getId()) {
                    return null;
                }
            }
        }

        order.get().setOrderStatus(OrderStatus.valueOf(statusString));
        return orderMapper.convertOrderEntityToResponse(orderRepository.save(order.get()));
    }

    public ResponseEntity<?> findByStatus(String status, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        final String statusString = status.toUpperCase();
        if (!OrderStatus.isValidOrderStatus(status)) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<OrderEntity> orderEntityList = orderRepository.findByOrderStatus(OrderStatus.valueOf(statusString));
        return new ResponseEntity<>(orderMapper.mapList(orderEntityList, OrderResponse.class), HttpStatus.OK);
    }

    public OrderStatus getOrderStatus(Long orderId, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return null;
        }


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

        if (requesterUser.get().getUserType() != UserType.ADMIN) {
            if (requesterUser.get().getUserType() == UserType.SELLER) {
                if (requesterUser.get().getId() != userSeller.getId()) {
                    return null;
                }
            } else {
                if (requesterUser.get().getId() != userBuyer.getId()) {
                    return null;
                }
            }
        }

        return orderEntity.get().getOrderStatus();
    }


    public OrderResponse prepareOrderByVehicleId(CreateOrderRequest orderRequest, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);

        if (requesterUser.isEmpty()) {
            return null; //usernotfound
        }

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(orderRequest.getVehicleId());
        if (vehicleEntity.isEmpty()) {
            return null; //invalid vehicle id
        }

        if (orderRequest.getSellerId() == orderRequest.getCustomerId()) {
            return null;
        }

        if (requesterUser.get().getUserType() == UserType.SELLER) {
            if (orderRequest.getSellerId() != requester_id) {
                return null;
            }
        }

        if (orderRequest.getDeposit() <= 0) {
            return null; //invalid deposit
        }


        Optional<UserEntity> userSeller = userRepository.findById(orderRequest.getSellerId());
        if (userSeller.isEmpty()) {
            return null; //invalid seller
        }

        if (userSeller.get().getUserType() != UserType.SELLER) {
            return null;
        }

        if (requesterUser.get().getUserType() != UserType.ADMIN && requesterUser.get().getUserType() != UserType.CUSTOMER && userSeller.get().getId() != requester_id) {
            return null;
        }

        if (requesterUser.get().getUserType() != UserType.ADMIN && requesterUser.get().getUserType() != UserType.SELLER && userSeller.get().getId() == requester_id) {
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
        orderEntity.setBuyer(requesterUser.get());
        orderEntity.setSeller(userSeller.get());
        Long totalPrice = (vehicleEntity.get().getDiscount() / 100 * vehicleEntity.get().getPrice()) + vehicleEntity.get().getPrice();
        orderEntity.setOrderPrice(totalPrice);
        OrderEntity orderEntitySaved = orderRepository.saveAndFlush(orderEntity);
        return orderMapper.convertOrderEntityToResponse(orderEntitySaved);
    }

    public OrderResponse createOrderByVehicleId(CreateOrderRequest orderRequest, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);

        if (requesterUser.isEmpty()) {
            return null; //usernotfound
        }

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(orderRequest.getVehicleId());
        if (vehicleEntity.isEmpty()) {
            return null; //invalid vehicle id
        }

        if (orderRequest.getSellerId() == orderRequest.getCustomerId()) {
            return null;
        }

        if (requesterUser.get().getUserType() == UserType.SELLER) {
            if (orderRequest.getSellerId() != requester_id) {
                return null;
            }
        }

        if (orderRequest.getDeposit() <= 0) {
            return null; //invalid deposit
        }


        Optional<UserEntity> userSeller = userRepository.findById(orderRequest.getSellerId());
        if (userSeller.isEmpty()) {
            return null; //invalid seller
        }

        if (userSeller.get().getUserType() != UserType.SELLER) {
            return null;
        }

        if (requesterUser.get().getUserType() != UserType.ADMIN && requesterUser.get().getUserType() != UserType.CUSTOMER && userSeller.get().getId() != requester_id) {
            return null;
        }

        if (requesterUser.get().getUserType() != UserType.ADMIN && requesterUser.get().getUserType() != UserType.SELLER && userSeller.get().getId() == requester_id) {
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
        orderEntity.setBuyer(requesterUser.get());
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
