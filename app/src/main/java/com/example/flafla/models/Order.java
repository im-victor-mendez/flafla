package com.example.flafla.models;

import com.example.flafla.enums.OrderStatus;
import com.google.firebase.Timestamp;

import java.util.List;

public class Order {
    private String id;
    private List<OrderItem> items;
    private Timestamp date;
    private String status;
    private String giftMessage;

    // Necesario para Firestore
    public Order() {
    }

    private Order(Builder builder) {
        this.id = builder.id;
        this.items = builder.items;
        this.date = builder.date;
        this.status = builder.status;
        this.giftMessage = builder.giftMessage;
    }

    public static class Builder {
        private String id;
        private List<OrderItem> items;
        private Timestamp date;
        private String status;
        private String giftMessage;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setItems(List<OrderItem> items) {
            this.items = items;
            return this;
        }

        public Builder setDate(Timestamp date) {
            this.date = date;
            return this;
        }

        public Builder setStatus(OrderStatus status) {
            this.status = status.name();
            return this;
        }

        public Builder setGiftMessage(String giftMessage) {
            this.giftMessage = giftMessage;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }

    public String getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getGiftMessage() {
        return giftMessage;
    }
}
