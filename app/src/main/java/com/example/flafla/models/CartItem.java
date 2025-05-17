package com.example.flafla.models;

public class CartItem {
    private String productId;
    private int quantity;

    public CartItem() {
    }

    private CartItem(Builder builder) {
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

        public CartItem build() {
            return new CartItem(this);
        }
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
