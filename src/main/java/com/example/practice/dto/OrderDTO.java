package com.example.practice.dto;

public class OrderDTO {
    private Long id;
    private int amount;
    private Long productId;
    private Long customerId;

    public OrderDTO() {
    }

    public OrderDTO(Long id, int amount, Long productId, Long customerId) {
        this.id = id;
        this.amount = amount;
        this.productId = productId;
        this.customerId = customerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
