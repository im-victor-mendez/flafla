package com.example.flafla.models;

import androidx.annotation.NonNull;

import com.example.flafla.enums.ProductType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {
    public static class Builder<T extends Builder<T>> {
        private String id;
        private String name;
        private String description;
        private double price;
        private List<String> images;
        private Map<String, String> specifications;
        private Map<String, String> termsAndConditions;
        private ProductType productType;

        public T setId(String id) {
            this.id = id;
            return self();
        }

        public T setName(String name) {
            this.name = name;
            return self();
        }

        public T setDescription(String description) {
            this.description = description;
            return self();
        }

        public T setPrice(double price) {
            this.price = price;
            return self();
        }

        public T setImages(List<String> images) {
            this.images = images;
            return self();
        }

        public T setSpecifications(Map<String, String> specifications) {
            this.specifications = specifications;
            return self();
        }

        public T setTermsAndConditions(Map<String, String> termsAndConditions) {
            this.termsAndConditions = termsAndConditions;
            return self();
        }

        public T setCategory(ProductType productType) {
            this.productType = productType;
            return self();
        }

        protected T self() {
            return (T) this;
        }
    }

    private final String id;
    private final String name;
    private final String description;
    private final double price;
    private final List<String> images;
    private final Map<String, String> specifications;
    private final Map<String, String> termsAndConditions;

    private final ProductType productType;

    protected Product(Builder<?> builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.price = builder.price;
        this.images = builder.images;
        this.specifications = builder.specifications;
        this.termsAndConditions = builder.termsAndConditions;
        this.productType = builder.productType;
    }

    public Product() {
        this.id = "";
        this.name = "";
        this.description = "";
        this.price = 0.0;
        this.images = new ArrayList<>();
        this.specifications = new HashMap<>();
        this.termsAndConditions = new HashMap<>();
        this.productType = ProductType.FLOWER; // o un valor por defecto
    }


    @NonNull
    @Override
    public String toString() {
        return "ProductEntity [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price
                + ", images=" + images + ", specifications=" + specifications + ", termsAndConditions="
                + termsAndConditions + ", category=" + productType + "]";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getImages() {
        return images;
    }

    public Map<String, String> getSpecifications() {
        return specifications;
    }

    public Map<String, String> getTermsAndConditions() {
        return termsAndConditions;
    }

    public ProductType getCategory() {
        return productType;
    }
}
