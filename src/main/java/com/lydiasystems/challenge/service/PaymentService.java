package com.lydiasystems.challenge.service;

import com.lydiasystems.challenge.entity.DTO.BankPaymentRequest;
import com.lydiasystems.challenge.entity.DTO.BankPaymentResponse;
import com.lydiasystems.challenge.entity.Payment;
import com.lydiasystems.challenge.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@Transactional
public class PaymentService {

    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final BankService bankService;
    private final PaymentRepository paymentRepository;

    public PaymentService(BankService bankService, PaymentRepository paymentRepository) {
        this.bankService = bankService;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public void pay(BigDecimal price) {
        BankPaymentRequest request = new BankPaymentRequest();
        request.setPrice(price);
        BankPaymentResponse response = bankService.pay(request);

        Payment payment = new Payment();
        payment.setBankResponse(response.getResultCode());
        payment.setPrice(price);
        paymentRepository.save(payment);
        logger.info("Payment saved successfully!");
    }
}
