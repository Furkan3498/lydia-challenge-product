package com.lydiasystems.challenge.entity.DTO;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class PurchaseRequest {
    private Long productId;
    private int quantity;
    private BigDecimal price;
}
