package com.lydiasystems.challenge.service;

import com.lydiasystems.challenge.entity.Product;
import com.lydiasystems.challenge.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

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
    public void testAddProduct() {
        // Given
        when(productRepository.save(mockProduct)).thenReturn(mockProduct);

        // When
        Product createdProduct = productService.addProduct(mockProduct);

        // Then
        assertEquals(mockProduct.getName(), createdProduct.getName());
        verify(productRepository, times(1)).save(mockProduct);
    }

    @Test
    public void testUpdateProduct_Success() {
        // Given
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(new BigDecimal("20.00"));
        updatedProduct.setStock(200);

        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
        when(productRepository.save(mockProduct)).thenReturn(mockProduct);

        // When
        Product result = productService.updateProduct(1L, updatedProduct);

        // Then
        assertEquals("Updated Product", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(new BigDecimal("20.00"), result.getPrice());
        assertEquals(200, result.getStock());
        verify(productRepository, times(1)).save(mockProduct);
    }

    @Test
    public void testUpdateProduct_NotFound() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When and Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(1L, mockProduct);
        });

        assertEquals("Couldn't find product id : 1", exception.getMessage());
    }

    @Test
    public void testDeleteProduct() {
        // When
        productService.deleteProduct(1L);

        // Then
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testListProducts() {
        // Given
        List<Product> products = new ArrayList<>();
        products.add(mockProduct);

        when(productRepository.findAllByOrderByIdAsc()).thenReturn(products);

        // When
        List<Product> result = productService.listProducts();

        // Then
        assertEquals(1, result.size());
        assertEquals(mockProduct.getName(), result.get(0).getName());
        verify(productRepository, times(1)).findAllByOrderByIdAsc();
    }
}
