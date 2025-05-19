package com.example.flafla.models;

import com.example.flafla.enums.OrderStatus;
import com.google.firebase.Timestamp;

import java.util.List;

public class Order {
    private String id;
    private List<OrderItem> items;
    private Timestamp date;
    private OrderStatus status;
    private String giftMessage;
    private double shipping;

    // Necesario para Firestore
    public Order() {
    }

    private Order(Builder builder) {
        this.id = builder.id;
        this.items = builder.items;
        this.date = builder.date;
        this.status = builder.status;
        this.giftMessage = builder.giftMessage;
        this.shipping = builder.shipping;
    }

    public static class Builder {
        private String id;
        private List<OrderItem> items;
        private Timestamp date;
        private OrderStatus status;
        private String giftMessage;
        private double shipping;

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
            this.status = OrderStatus.valueOf(status.name());
            return this;
        }

        public Builder setGiftMessage(String giftMessage) {
            this.giftMessage = giftMessage;
            return this;
        }

        public Builder setShipping(double shipping) {
            this.shipping = shipping;
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

    public OrderStatus getStatus() {
        return status;
    }

    public String getGiftMessage() {
        return giftMessage;
    }

    public double getShipping() {
        return shipping;
    }
}
