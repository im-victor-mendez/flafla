package com.example.flafla.models;

import com.example.flafla.enums.PaymentType;

public class Payment {
    private String id;
    private PaymentType type;
    private String cardNumber;
    private String cardHolder;
    private String expiryDate;

    // Constructor vacío necesario para Firestore
    public Payment() {
    }

    private Payment(Builder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.cardNumber = builder.cardNumber;
        this.cardHolder = builder.cardHolder;
        this.expiryDate = builder.expiryDate;
    }

    public static class Builder {
        private String id;
        private PaymentType type;
        private String cardNumber;
        private String cardHolder;
        private String expiryDate;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setType(PaymentType type) {
            this.type = type;
            return this;
        }

        public Builder setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
            return this;
        }

        public Builder setCardHolder(String cardHolder) {
            this.cardHolder = cardHolder;
            return this;
        }

        public Builder setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }

    public String getId() {
        return id;
    }

    public PaymentType getType() {
        return type;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}
