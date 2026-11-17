package com.example.orderservice.service;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.dto.req.OrderCreateRequest;
import com.example.orderservice.dto.res.ProductResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public Order createOrder(OrderCreateRequest req) {
        if (req.getProductId() == null || req.getQuantity() == null || req.getQuantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "productId/quantity không hợp lệ");
        }

        ProductResponse product;
        try {
            product = inventoryClient.getProduct(req.getProductId());
        } catch (FeignException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm");
        } catch (FeignException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Inventory service không khả dụng");
        }

        if (product.getQuantity() < req.getQuantity()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Không đủ hàng trong kho");
        }

        Order order = new Order();
        order.setProductId(req.getProductId());
        order.setQuantity(req.getQuantity());
        order.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(req.getQuantity())));
        order.setStatus("PENDING");
        order = orderRepository.save(order);

        try {
            inventoryClient.decrease(req.getProductId(), req.getQuantity());
            order.setStatus("COMPLETED");
        } catch (FeignException e) {
            order.setStatus("CANCELLED");
            orderRepository.save(order);
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Đặt hàng thất bại, đơn #" + order.getId() + " đã bị hủy"
            );
        }
        orderRepository.save(order);
        return order;
    }
}
