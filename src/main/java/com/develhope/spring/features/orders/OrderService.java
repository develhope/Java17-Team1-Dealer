package com.develhope.spring.features.orders;

import com.develhope.spring.features.orders.dto.CreateOrderRequest;
import com.develhope.spring.features.orders.dto.OrderResponse;
import com.develhope.spring.features.orders.dto.PatchOrderRequest;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.users.UserRepository;
import com.develhope.spring.features.users.UserType;
import com.develhope.spring.features.vehicle.VehicleEntity;
import com.develhope.spring.features.vehicle.VehicleService;
import com.develhope.spring.features.vehicle.VehicleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final VehicleService vehicleService;
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
        return orderMapper.convertOrderEntityToResponse(orderRepository.saveAndFlush(foundOrderEntity.get()));

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
        List<OrderEntity> orderEntityList = orderRepository.findAllByBuyer(userId);
        return orderMapper.mapList(orderEntityList, OrderResponse.class);
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
        return orderMapper.convertOrderEntityToResponse(orderRepository.saveAndFlush(order.get()));
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


    public OrderResponse prepareOrderByVehicleId(Long vehicleId, CreateOrderRequest orderRequest, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        System.out.println("HERE1");
        if (requesterUser.isEmpty()) {
            return null; //usernotfound
        }

        System.out.println("HERE2");
        if (orderRequest.getDeposit() <= 0) {
            return null; //invalid deposit
        }
        System.out.println("HERE3");

        final VehicleEntity vehicleEntity = vehicleService.getSingleVehicle(vehicleId);
        if (vehicleEntity == null) {
            return null; //invalid vehicle id
        }

        if (vehicleEntity.getSeller().getId() == requester_id) {
            return null; //unhaoutrized
        }

        if (orderRequest.getDeposit() > vehicleEntity.getPrice()) {
            return null; //invalid price
        }

        if (vehicleEntity.getVehicleStatus() != VehicleStatus.CAN_BE_ORDERED) {
            return null;
        }


        final PaymentStatus paymentStatus = orderRequest.getDeposit() == vehicleEntity.getPrice() ? PaymentStatus.PAID : PaymentStatus.DEPOSIT;
        OrderEntity orderEntity = orderMapper.convertOrderRequestToEntity(orderRequest);
        orderEntity.setOrderStatus(OrderStatus.TO_SEND);
        orderEntity.setPaymentStatus(paymentStatus);
        orderEntity.setVehicleEntity(vehicleEntity);
        orderEntity.setBuyer(requesterUser.get());
        orderEntity.setSeller(vehicleEntity.getSeller());
        OrderEntity orderEntitySaved = orderRepository.save(orderEntity);
        return orderMapper.convertOrderEntityToResponse(orderEntitySaved);
    }

    public OrderResponse createOrderByVehicleId(Long vehicleId, CreateOrderRequest orderRequest, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);

        if (requesterUser.isEmpty()) {
            return null; //usernotfound
        }

        final var userType = requesterUser.get().getUserType();
        if (userType == UserType.SELLER) {
            return null; //unhaoutrized
        }
        final VehicleEntity vehicleEntity = vehicleService.getSingleVehicle(vehicleId);
        if (vehicleEntity == null) {
            return null; //invalid vehicle id
        }

        if (vehicleEntity.getVehicleStatus() != VehicleStatus.PROMPT_DELIVERY) {
            return null; //return the status trough error?
        }

        if (orderRequest.getDeposit() <= 0 || orderRequest.getDeposit() > vehicleEntity.getPrice()) {
            return null; //invalid price
        }

        final PaymentStatus paymentStatus = orderRequest.getDeposit() == vehicleEntity.getPrice() ? PaymentStatus.PAID : PaymentStatus.DEPOSIT;

        OrderEntity orderEntity = orderMapper.convertOrderRequestToEntity(orderRequest);
        orderEntity.setOrderStatus(OrderStatus.TO_SEND);
        orderEntity.setPaymentStatus(paymentStatus);
        orderEntity.setVehicleEntity(vehicleEntity);
        orderEntity.setBuyer(requesterUser.get());
        orderEntity.setSeller(vehicleEntity.getSeller());
        OrderEntity orderEntitySaved = orderRepository.save(orderEntity);
        return orderMapper.convertOrderEntityToResponse(orderEntitySaved);
    }
}
