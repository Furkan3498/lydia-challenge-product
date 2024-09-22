package com.lydiasystems.challenge.controller;

import com.lydiasystems.challenge.entity.DTO.PurchaseRequest;
import com.lydiasystems.challenge.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public ResponseEntity<Void> purchaseProduct(@RequestBody PurchaseRequest request) {
        purchaseService.purchaseProduct(request.getProductId(), request.getQuantity(), request.getPrice());
        return ResponseEntity.ok().build();
    }
}
