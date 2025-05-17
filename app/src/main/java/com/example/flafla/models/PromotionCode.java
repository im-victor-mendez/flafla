package com.example.flafla.models;

import com.google.firebase.Timestamp;

public class PromotionCode {
    private String code;
    private int discountPercent;
    private Timestamp validFrom;
    private Timestamp validTo;
    private boolean active;

    public PromotionCode() {
        // Constructor vacío necesario para Firestore
    }

    private PromotionCode(Builder builder) {
        this.code = builder.code;
        this.discountPercent = builder.discountPercent;
        this.validFrom = builder.validFrom;
        this.validTo = builder.validTo;
        this.active = builder.active;
    }

    public static class Builder {
        private String code;
        private int discountPercent;
        private Timestamp validFrom;
        private Timestamp validTo;
        private boolean active;

        public Builder setCode(String code) {
            this.code = code;
            return this;
        }

        public Builder setDiscountPercent(int discountPercent) {
            this.discountPercent = discountPercent;
            return this;
        }

        public Builder setValidFrom(Timestamp validFrom) {
            this.validFrom = validFrom;
            return this;
        }

        public Builder setValidTo(Timestamp validTo) {
            this.validTo = validTo;
            return this;
        }

        public Builder setActive(boolean active) {
            this.active = active;
            return this;
        }

        public PromotionCode build() {
            return new PromotionCode(this);
        }
    }

    public String getCode() {
        return code;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public Timestamp getValidFrom() {
        return validFrom;
    }

    public Timestamp getValidTo() {
        return validTo;
    }

    public boolean isActive() {
        return active;
    }
}
