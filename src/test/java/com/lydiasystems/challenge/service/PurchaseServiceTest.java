package com.lydiasystems.challenge.service;

import com.lydiasystems.challenge.entity.Product;
import com.lydiasystems.challenge.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PurchaseServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PurchaseService purchaseService;

    private Product mockProduct;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Product 1");
        mockProduct.setDescription("Sample product description");
        mockProduct.setPrice(new BigDecimal("10.00"));
        mockProduct.setStock(100);
    }

    @Test
    public void testPurchaseProduct_Success() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        // When
        purchaseService.purchaseProduct(1L, 10, new BigDecimal("100.00"));

        // Then
        verify(paymentService, times(1)).pay(new BigDecimal("100.00"));
        verify(productRepository, times(1)).save(mockProduct);
        assertEquals(90, mockProduct.getStock());
    }

    @Test
    public void testPurchaseProduct_NotEnoughStock() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> {
            purchaseService.purchaseProduct(1L, 200, new BigDecimal("100.00"));
        });

        verify(paymentService, never()).pay(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    public void testPurchaseProduct_ProductNotFound() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> {
            purchaseService.purchaseProduct(1L, 10, new BigDecimal("100.00"));
        });

        verify(paymentService, never()).pay(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    public void testSimulateMultipleCustomers() throws InterruptedException {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        // When
        purchaseService.simulateMultipleCustomers(1L, 1, new BigDecimal("10.00"), 5);

        // Verify the concurrent execution has finished before assertions
        Thread.sleep(500); // Add small sleep if needed, or use await mechanism.

        // Simulate the execution of multiple customers
        verify(productRepository, times(5)).save(mockProduct);
        verify(paymentService, times(5)).pay(new BigDecimal("10.00"));
    }

}
