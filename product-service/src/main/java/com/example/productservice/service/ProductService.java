package com.example.productservice.service;

import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm"));
    }

    @Transactional
    public void decreaseProductQuantity(Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "quantity không hợp lệ");
        }

        Product product = getProductById(productId);
        if (product.getQuantity() < quantity) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Không đủ hàng trong kho");
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }
}
