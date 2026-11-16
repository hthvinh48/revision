package com.example.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    @GetMapping("/api/inventory/{productId}/quantity")
    Integer getProductQuantity(@PathVariable Long productId);

    @PostMapping("/api/inventory/{productId}/decrease")
    void decreaseProductQuantity(@PathVariable Long productId, Integer quantity);
}
