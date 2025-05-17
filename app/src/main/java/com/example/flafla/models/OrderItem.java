package com.example.flafla.models;

public class OrderItem {
    private String productId;
    private int quantity;
    private double price;

    // Necesario para Firestore
    public OrderItem() {
    }

    private OrderItem(Builder builder) {
        this.productId = builder.productId;
        this.quantity = builder.quantity;
        this.price = builder.price;
    }

    public static class Builder {
        private String productId;
        private int quantity;
        private double price;

        public Builder setProductId(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder setPrice(double price) {
            this.price = price;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }

    // Getters
    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
