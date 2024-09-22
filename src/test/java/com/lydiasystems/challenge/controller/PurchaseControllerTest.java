package com.lydiasystems.challenge.controller;

import com.lydiasystems.challenge.entity.DTO.PurchaseRequest;
import com.lydiasystems.challenge.service.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PurchaseControllerTest {

    @Mock
    private PurchaseService purchaseService;

    @InjectMocks
    private PurchaseController purchaseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPurchaseProduct_Success() {
        // Given
        PurchaseRequest request = new PurchaseRequest();
        request.setProductId(1L);
        request.setQuantity(2);
        request.setPrice(new BigDecimal(100));

        // When
        ResponseEntity<Void> response = purchaseController.purchaseProduct(request);

        // Then
        verify(purchaseService, times(1)).purchaseProduct(request.getProductId(), request.getQuantity(), request.getPrice());
        assertEquals(200, response.getStatusCodeValue());
    }
}
