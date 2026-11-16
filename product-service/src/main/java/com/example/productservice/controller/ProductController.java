package com.example.productservice.controller;

import com.example.productservice.entity.Product;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @GetMapping("/{productId}/quantity")
    public Integer getProductQuantity(@PathVariable Long productId) {
        return productService.getProductQuantity(productId);
    }

    @PostMapping("/{productId}/decrease")
    public void decreaseProductQuantity(@PathVariable Long productId, @RequestBody Integer quantity) {
        productService.decreaseProductQuantity(productId, quantity);
    }
}
