package com.example.flafla.models;

import com.google.firebase.Timestamp;

public class ProductPromotion {

    public enum Type {
        DISCOUNT, BULK
    }

    private String product_id;
    private Type type;
    // Solo para DISCOUNT
    private Integer discount_percent;
    // Solo para BULK (ej: "2x1", "3x2")
    private String bulk_type;
    private String label;
    private String description;
    private Timestamp valid_from;
    private Timestamp validTo;

    private ProductPromotion() {
    }

    public static class Builder {
        private final ProductPromotion instance;

        public Builder() {
            instance = new ProductPromotion();
        }

        public Builder setProductId(String productId) {
            instance.product_id = productId;
            return this;
        }

        public Builder setType(Type type) {
            instance.type = type;
            return this;
        }

        public Builder setDiscountPercent(Integer discountPercent) {
            instance.discount_percent = discountPercent;
            return this;
        }

        public Builder setBulkType(String bulkType) {
            instance.bulk_type = bulkType;
            return this;
        }

        public Builder setLabel(String label) {
            instance.label = label;
            return this;
        }

        public Builder setDescription(String description) {
            instance.description = description;
            return this;
        }

        public Builder setValidFrom(Timestamp validFrom) {
            instance.valid_from = validFrom;
            return this;
        }

        public Builder setValidTo(Timestamp validTo) {
            instance.validTo = validTo;
            return this;
        }

        public ProductPromotion build() {
            return instance;
        }
    }

    // Getters (Firebase necesita getters públicos)
    public String getProduct_id() {
        return product_id;
    }

    public Type getType() {
        return type;
    }

    public Integer getDiscount_percent() {
        return discount_percent;
    }

    public String getBulk_type() {
        return bulk_type;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getValid_from() {
        return valid_from;
    }

    public Timestamp getValidTo() {
        return validTo;
    }
}
