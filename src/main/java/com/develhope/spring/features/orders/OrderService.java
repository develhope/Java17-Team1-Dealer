package com.develhope.spring.features.orders;

import com.develhope.spring.features.orders.dto.OrderRequest;
import com.develhope.spring.features.orders.dto.OrderResponse;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.users.UserRepository;
import com.develhope.spring.features.users.UserType;
import com.develhope.spring.features.vehicle.VehicleEntity;
import com.develhope.spring.features.vehicle.VehicleRepository;
import com.develhope.spring.features.vehicle.VehicleService;
import com.develhope.spring.features.vehicle.VehicleStatus;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final VehicleService vehicleService;
    private final UserRepository userRepository;

    private final OrderMapper orderMapper;

    public Boolean deleteOrder(Long id) {
        orderRepository.deleteById(id);
        return !orderRepository.existsById(id);
    }

    public OrderEntity patchOrder(Long id, OrderEntity orderEntity) {
        Optional<OrderEntity> foundOrderEntity = orderRepository.findById(id);
        if (foundOrderEntity.isPresent()) {
            foundOrderEntity.get().setOrderStatus(orderEntity.getOrderStatus());
            foundOrderEntity.get().setDeposit(orderEntity.getDeposit());
            foundOrderEntity.get().setPaymentStatus(orderEntity.getPaymentStatus());
            foundOrderEntity.get().setVehicleEntity(orderEntity.getVehicleEntity());
            foundOrderEntity.get().setUserEntity(orderEntity.getUserEntity()); //here should already be a hashed string
            return orderRepository.saveAndFlush(foundOrderEntity.get());
        } else {
            return null;
        }
    }


    public OrderStatus getOrderStatusFromId(Long id) {
        if (orderRepository.existsById(id)) {
            return orderRepository.findById(id).get().getOrderStatus();
        } else {
            return null;
        }
    }

    public OrderEntity patchOrderStatus(Long id, String status) {
        Optional<OrderEntity> order = orderRepository.findById(id);
        if (order.isPresent()) {
            String statusString = status.toUpperCase();
            OrderStatus s = OrderStatus.valueOf(statusString);
            order.get().setOrderStatus(s);
            return orderRepository.saveAndFlush(order.get());
        } else {
            return null;
        }
    }

    public List<OrderEntity> findByStatus(String status) {
        String statusString = status.toUpperCase();
        OrderStatus s = OrderStatus.valueOf(statusString);
        return new ArrayList<>(orderRepository.findByOrderStatus(s));
    }

    public OrderResponse prepareOrderByVehicleId(Long vehicleId, OrderRequest orderRequest, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);

        if (requesterUser.isEmpty()) {
            return null; //usernotfound
        }

        final var userType = requesterUser.get().getUserType();
        if (userType == UserType.SELLER){
            return null; //unhaoutrized
        }

        if(orderRequest.getDeposit() <= 0){
            return null; //invalid deposit
        }

        final VehicleEntity vehicleEntity = vehicleService.getSingleVehicle(vehicleId);
        if(vehicleEntity == null){
            return null; //invalid vehicle id
        }

        if(orderRequest.getDeposit() > vehicleEntity.getPrice()){
            return null; //invalid price
        }

        OrderEntity orderEntity = orderMapper.convertOrderRequestByToEntity(orderRequest);
        orderEntity.setOrderStatus(OrderStatus.TO_SEND);
        orderEntity.setPaymentStatus(PaymentStatus.DEPOSIT);
        orderEntity.setVehicleEntity(vehicleEntity);
        orderEntity.setUserEntity(requesterUser.get());
        OrderEntity orderEntitySaved = orderRepository.save(orderEntity);
        return orderMapper.convertOrderEntityToResponse(orderEntitySaved);
    }
 
    public OrderResponse createOrderByVehicleId(Long vehicleId, OrderRequest orderRequest, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);

        if (requesterUser.isEmpty()) {
            return null; //usernotfound
        }

        final var userType = requesterUser.get().getUserType();
        if (userType == UserType.SELLER){
            return null; //unhaoutrized
        }
        final VehicleEntity vehicleEntity = vehicleService.getSingleVehicle(vehicleId);
        if(vehicleEntity == null){
            return null; //invalid vehicle id
        }

        if(vehicleEntity.getVehicleStatus() != VehicleStatus.PROMPT_DELIVERY){
            return null; //return the status trough error?
        }

        if(orderRequest.getDeposit() <= 0 || orderRequest.getDeposit() > vehicleEntity.getPrice()){
            return null; //invalid price
        }

        OrderEntity orderEntity = orderMapper.convertOrderRequestByToEntity(orderRequest);
        orderEntity.setOrderStatus(OrderStatus.TO_SEND);
        orderEntity.setPaymentStatus(PaymentStatus.PAID);
        orderEntity.setVehicleEntity(vehicleEntity);
        orderEntity.setUserEntity(requesterUser.get());
        OrderEntity orderEntitySaved = orderRepository.save(orderEntity);
        return orderMapper.convertOrderEntityToResponse(orderEntitySaved);
    }
}
