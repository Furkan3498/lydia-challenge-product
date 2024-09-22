package com.lydiasystems.challenge.service;


import com.lydiasystems.challenge.entity.Product;
import com.lydiasystems.challenge.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class PurchaseService {
    private final ProductRepository productRepository;

    private final PaymentService paymentService;

    public PurchaseService(ProductRepository productRepository, PaymentService paymentService) {
        this.productRepository = productRepository;
        this.paymentService = paymentService;
    }

    @Transactional
    public void purchaseProduct(Long productId, int quantity, BigDecimal price) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product couldn't found with id : " + productId));

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Not enough stock available");
        }

        paymentService.pay(price);

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

    public void simulateMultipleCustomers(Long productId, int quantity, BigDecimal price, int customerCount) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < customerCount; i++) {
            executorService.submit(() -> {
                try {
                    purchaseProduct(productId, quantity, price);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
