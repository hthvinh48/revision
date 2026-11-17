package com.example.orderservice.client;

import com.example.orderservice.dto.res.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    @GetMapping("/api/inventory/{productId}")
    ProductResponse getProduct(@PathVariable Long productId);

    @PostMapping("/api/inventory/{productId}/decrease")
    void decrease(@PathVariable Long productId, @RequestBody Integer quantity);
}
