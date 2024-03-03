package com.develhope.spring.features.orders;

import com.develhope.spring.features.orders.dto.CreateOrderRequest;
import com.develhope.spring.features.orders.dto.OrderResponse;
import com.develhope.spring.features.orders.dto.PatchOrderRequest;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.users.UserRepository;
import com.develhope.spring.features.users.UserService;
import com.develhope.spring.features.users.UserType;
import com.develhope.spring.features.vehicle.VehicleEntity;
import com.develhope.spring.features.vehicle.VehicleService;
import com.develhope.spring.features.vehicle.VehicleStatus;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final VehicleService vehicleService;
    private final UserRepository userRepository;

    private final OrderMapper orderMapper;

    public Boolean deleteOrder(Long orderId, Long requester_id) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isEmpty()) {
            return false; //order of orderId not exists
        }

        UserEntity originalUser = orderEntity.get().getBuyer();
        if (!userService.isRequesterIDValid(originalUser, requester_id)) {
            return false;
        }


        orderRepository.deleteById(orderId);
        return true;
    }

    public OrderResponse patchOrder(Long orderId, PatchOrderRequest patchOrderRequest, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (requesterUser.isEmpty()) {
            return null;
        }

        Optional<OrderEntity> foundOrderEntity = orderRepository.findById(orderId);
        if (foundOrderEntity.isEmpty()) {
            return null; //order of orderId not exists
        }

        UserEntity originalUser = foundOrderEntity.get().getBuyer();
        if (originalUser == null) {
            return null;
        }

        if ((originalUser.getUserType() == UserType.SELLER && originalUser.getId() != requester_id) || originalUser.getUserType() == UserType.CUSTOMER) {
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

        //can a seller change vehicle of an ongoing order?
        if (patchOrderRequest.getVehicleEntity() != null) {
            //CHECK VEHICLE ENTITY
            foundOrderEntity.get().setVehicleEntity(patchOrderRequest.getVehicleEntity());
        }
        return orderMapper.convertOrderEntityToResponse(orderRepository.saveAndFlush(foundOrderEntity.get()));

    }


    public OrderStatus getOrderStatusFromId(Long orderId, Long requester_id) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isEmpty()) {
            return null; //order of orderId not exists
        }

        UserEntity originalUser = orderEntity.get().getBuyer();
        if (!userService.isRequesterIDValid(originalUser, requester_id)) {
            return null;
        }

        return orderEntity.get().getOrderStatus();
    }

    public List<OrderResponse> getOrderListById(Long userId, Long requester_id) {
        List<OrderEntity> orderEntityList = orderRepository.findAllByBuyer(userId);
        return orderMapper.mapList(orderEntityList, OrderResponse.class);
    }

    public OrderResponse patchOrderStatus(Long id, String status) {
        final String statusString = status.toUpperCase();
        if (OrderStatus.isValidOrderStatus(statusString)) {
            return null;
        }

        Optional<OrderEntity> order = orderRepository.findById(id);
        if (order.isPresent()) {
            order.get().setOrderStatus(OrderStatus.valueOf(statusString));
            return orderMapper.convertOrderEntityToResponse(orderRepository.saveAndFlush(order.get()));
        } else {
            return null;
        }
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

    public OrderResponse prepareOrderByVehicleId(Long vehicleId, CreateOrderRequest orderRequest, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);

        if (requesterUser.isEmpty()) {
            return null; //usernotfound
        }

        final var userType = requesterUser.get().getUserType();
        if (userType == UserType.SELLER) {
            return null; //unhaoutrized
        }

        if (orderRequest.getDeposit() <= 0) {
            return null; //invalid deposit
        }

        final VehicleEntity vehicleEntity = vehicleService.getSingleVehicle(vehicleId);
        if (vehicleEntity == null) {
            return null; //invalid vehicle id
        }

        if (orderRequest.getDeposit() > vehicleEntity.getPrice()) {
            return null; //invalid price
        }

        OrderEntity orderEntity = orderMapper.convertOrderRequestToEntity(orderRequest);
        orderEntity.setOrderStatus(OrderStatus.TO_SEND);
        orderEntity.setPaymentStatus(PaymentStatus.DEPOSIT);
        orderEntity.setVehicleEntity(vehicleEntity);
        orderEntity.setBuyer(requesterUser.get());
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

        OrderEntity orderEntity = orderMapper.convertOrderRequestToEntity(orderRequest);
        orderEntity.setOrderStatus(OrderStatus.TO_SEND);
        orderEntity.setPaymentStatus(PaymentStatus.PAID);
        orderEntity.setVehicleEntity(vehicleEntity);
        orderEntity.setBuyer(requesterUser.get());
        OrderEntity orderEntitySaved = orderRepository.save(orderEntity);
        return orderMapper.convertOrderEntityToResponse(orderEntitySaved);
    }
}
