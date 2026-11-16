package com.example.productservice.service;

import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Integer getProductQuantity(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        return product != null ? product.getQuantity() : null;
    }

    public void decreaseProductQuantity(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null && product.getQuantity() >= quantity) {
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);
        } else {
            throw new IllegalArgumentException("Insufficient quantity available for product ID: " + productId);
        }
        productRepository.save(product);
    }
}
