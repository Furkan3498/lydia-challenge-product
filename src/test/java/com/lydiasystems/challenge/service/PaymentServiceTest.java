package com.lydiasystems.challenge.service;

import com.lydiasystems.challenge.entity.DTO.BankPaymentRequest;
import com.lydiasystems.challenge.entity.DTO.BankPaymentResponse;
import com.lydiasystems.challenge.entity.Payment;
import com.lydiasystems.challenge.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

public class PaymentServiceTest {

    @Mock
    private BankService bankService;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPay_Success() {
        // Given
        BigDecimal price = new BigDecimal("100.00");
        BankPaymentRequest bankPaymentRequest = new BankPaymentRequest();
        bankPaymentRequest.setPrice(price);

        BankPaymentResponse bankPaymentResponse = new BankPaymentResponse();
        bankPaymentResponse.setResultCode("200");

        // Mock the response of BankService
        when(bankService.pay(any(BankPaymentRequest.class))).thenReturn(bankPaymentResponse);

        // When
        paymentService.pay(price);

        // Then
        verify(bankService, times(1)).pay(any(BankPaymentRequest.class));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    public void testPay_Failure() {
        // Given
        BigDecimal price = new BigDecimal("100.00");
        BankPaymentRequest bankPaymentRequest = new BankPaymentRequest();
        bankPaymentRequest.setPrice(price);

        // Simulate bank service failure (e.g., result code "500")
        BankPaymentResponse bankPaymentResponse = new BankPaymentResponse();
        bankPaymentResponse.setResultCode("500");

        when(bankService.pay(any(BankPaymentRequest.class))).thenReturn(bankPaymentResponse);

        // When
        paymentService.pay(price);

        // Then
        verify(bankService, times(1)).pay(any(BankPaymentRequest.class));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }
}
