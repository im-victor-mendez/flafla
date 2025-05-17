package com.example.flafla.models;

import com.google.firebase.Timestamp;

import java.util.List;

public class Cart {
    private List<CartItem> items;
    private Timestamp lastUpdated;

    public Cart() {
    }

    private Cart(Builder builder) {
        this.items = builder.items;
        this.lastUpdated = builder.lastUpdated;
    }

    public static class Builder {
        private List<CartItem> items;
        private Timestamp lastUpdated;

        public Builder setItems(List<CartItem> items) {
            this.items = items;
            return this;
        }

        public Builder setLastUpdated(Timestamp lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        public Cart build() {
            return new Cart(this);
        }
    }

    public List<CartItem> getItems() {
        return items;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }
}
