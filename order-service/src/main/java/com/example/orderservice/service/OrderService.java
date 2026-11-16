package com.example.orderservice.service;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.dto.req.OrderCreateReq;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    @Transactional
    public void createOrder(OrderCreateReq req) {
        Integer availableQuantity = inventoryClient.getProductQuantity(req.getProductId());

        if (availableQuantity == null || availableQuantity < req.getQuantity()) {
            throw new IllegalArgumentException("Insufficient quantity available for product ID: " + req.getProductId());
        }

        // Decrease the product quantity in the inventory
        inventoryClient.decreaseProductQuantity(req.getProductId(), req.getQuantity());

        // Save the order to the database
        Order order = new Order();
        order.setProductId(req.getProductId());
        order.setQuantity(req.getQuantity());
        orderRepository.save(order);
    }
}
