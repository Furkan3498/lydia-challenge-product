package com.lydiasystems.challenge.service;

import com.lydiasystems.challenge.entity.DTO.BankPaymentRequest;
import com.lydiasystems.challenge.entity.DTO.BankPaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BankServiceTest {

    private BankService bankService;

    @BeforeEach
    void setUp() {
        bankService = new BankService();
    }

    @Test
    void testPay_Success() throws InterruptedException {
        // Given
        BankPaymentRequest request = new BankPaymentRequest();
        request.setPrice(new BigDecimal(100));

        // When
        BankPaymentResponse response = bankService.pay(request);

        // Then
        assertNotNull(response);
        assertEquals("200", response.getResultCode());
    }

    @Test
    void testPay_SleepInterrupted() {
        BankPaymentRequest request = new BankPaymentRequest();
        request.setPrice(new BigDecimal(100));

        Thread.currentThread().interrupt();

        BankPaymentResponse response = bankService.pay(request);

        assertNull(response);

        assertTrue(Thread.interrupted());
    }


}
