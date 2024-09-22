package com.lydiasystems.challenge.controller;

import com.lydiasystems.challenge.entity.Product;
import com.lydiasystems.challenge.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProduct_Success() {
        // Given
        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(new BigDecimal(100));
        when(productService.addProduct(product)).thenReturn(product);

        // When
        ResponseEntity<Product> response = productController.addProduct(product);

        // Then
        verify(productService, times(1)).addProduct(product);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Sample Product", Objects.requireNonNull(response.getBody()).getName());
    }

    @Test
    void testUpdateProduct_Success() {
        // Given
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(new BigDecimal(150));
        when(productService.updateProduct(1L, updatedProduct)).thenReturn(updatedProduct);

        // When
        ResponseEntity<Product> response = productController.updateProduct(1L, updatedProduct);

        // Then
        verify(productService, times(1)).updateProduct(1L, updatedProduct);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Product", Objects.requireNonNull(response.getBody()).getName());
    }

    @Test
    void testDeleteProduct_Success() {
        // When
        ResponseEntity<Void> response = productController.deleteProduct(1L);

        // Then
        verify(productService, times(1)).deleteProduct(1L);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testListProducts_Success() {
        // Given
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setPrice(new BigDecimal(100));

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setPrice(new BigDecimal(200));

        List<Product> productList = Arrays.asList(product1, product2);
        when(productService.listProducts()).thenReturn(productList);

        // When
        ResponseEntity<List<Product>> response = productController.listProducts();

        // Then
        verify(productService, times(1)).listProducts();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertEquals("Product 1", response.getBody().get(0).getName());
        assertEquals("Product 2", response.getBody().get(1).getName());
    }
}
