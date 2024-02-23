package com.develhope.spring.features.orders;

import com.develhope.spring.features.vehicle.VehicleEntity;
import com.develhope.spring.features.vehicle.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    public OrderEntity createOrderFromVehicle(OrderEntity orderEntity, long id) {
        VehicleEntity vehicleEntity = vehicleRepository.findById(id).get();
        orderEntity.setVehicleEntity(vehicleEntity);
        return orderRepository.saveAndFlush(orderEntity);
    }

    public Boolean deleteOrder(long id) {
        orderRepository.deleteById(id);
        return !orderRepository.existsById(id);
    }

    public OrderEntity updateOrder(long id, OrderEntity orderEntity) {
        if (orderRepository.existsById(id)) {
            orderEntity.setId(id);
            return orderRepository.saveAndFlush(orderEntity);
        } else {
            return null;
        }
    }

    public OrderStatus getOrderStatusFromId(long id) {
        if (orderRepository.existsById(id)) {
            return orderRepository.findById(id).get().getOrderStatus();
        } else {
            return null;
        }
    }

    public OrderEntity updateOrderStatusFromId(long id, String status) {
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
}
