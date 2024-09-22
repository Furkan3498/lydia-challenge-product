package com.lydiasystems.challenge.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentServiceClientsTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentServiceClients paymentServiceClients;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCall_Success() throws Exception {
        BigDecimal price = new BigDecimal("100.00");

        CompletableFuture<String> result = paymentServiceClients.call(price);

        verify(paymentService, times(1)).pay(price);
        assertEquals("success", result.get());
    }


    @Test
    void testCall_Failure() throws Exception {
        BigDecimal price = new BigDecimal("100.00");

        doThrow(new RuntimeException("Payment processing failed"))
                .when(paymentService).pay(price);

        CompletableFuture<String> result = paymentServiceClients.call(price);

        ExecutionException executionException = assertThrows(ExecutionException.class, result::get);
        Throwable cause = executionException.getCause();
        assertTrue(cause instanceof RuntimeException);
        assertEquals("Payment processing failed", cause.getMessage());

        verify(paymentService, times(1)).pay(price);
    }



}
