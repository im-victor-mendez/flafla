package com.example.flafla.models;

public class AvailableItem {
    private String productId;
    private int quantity;

    public AvailableItem() {
        // Constructor vacío necesario para Firestore
    }

    private AvailableItem(Builder builder) {
        this.productId = builder.productId;
        this.quantity = builder.quantity;
    }

    public static class Builder {
        private String productId;
        private int quantity;

        public Builder setProductId(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public AvailableItem build() {
            return new AvailableItem(this);
        }
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
