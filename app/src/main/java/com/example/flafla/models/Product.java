package com.example.flafla.models;

import androidx.annotation.NonNull;

import com.example.flafla.enums.ProductType;

import java.util.List;
import java.util.Map;

/**
 * <h1>Product</h1>
 * Modelo de datos para un producto en la colección "products" de Firestore.
 * <p>
 * Incluye atributos como nombre, descripción, precio, imágenes, especificaciones,
 * términos y condiciones, y tipo de categoría (plant o flower).
 * <p>
 * Compatible con Firestore gracias al constructor vacío y los métodos getter/setter.
 * También incluye un patrón Builder para facilitar la creación de objetos de forma fluida.
 */
public class Product {

    // Atributos del producto
    private String id;
    private String name;
    private String description;
    private double price;
    private List<String> images;
    private Map<String, String> specifications;
    private Map<String, String> termsAndConditions;
    private ProductType category;

    /**
     * Constructor vacío requerido por Firestore para la deserialización.
     */
    public Product() {
        // Obligatorio para Firestore
    }

    /**
     * Constructor privado utilizado por el Builder.
     */
    private Product(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.price = builder.price;
        this.images = builder.images;
        this.specifications = builder.specifications;
        this.termsAndConditions = builder.termsAndConditions;
        this.category = builder.category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Map<String, String> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Map<String, String> specifications) {
        this.specifications = specifications;
    }

    public Map<String, String> getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(Map<String, String> termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public ProductType getCategory() {
        return category;
    }

    public void setCategory(ProductType category) {
        this.category = category;
    }

    /**
     * Builder para la creación de objetos Product de forma fluida.
     */
    public static class Builder {
        private String id;
        private String name;
        private String description;
        private double price;
        private List<String> images;
        private Map<String, String> specifications;
        private Map<String, String> termsAndConditions;
        private ProductType category;

        public Builder() {
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setPrice(double price) {
            this.price = price;
            return this;
        }

        public Builder setImages(List<String> images) {
            this.images = images;
            return this;
        }

        public Builder setSpecifications(Map<String, String> specifications) {
            this.specifications = specifications;
            return this;
        }

        public Builder setTermsAndConditions(Map<String, String> termsAndConditions) {
            this.termsAndConditions = termsAndConditions;
            return this;
        }

        public Builder setCategory(ProductType category) {
            this.category = category;
            return this;
        }

        /**
         * Construye una instancia de Product usando los valores asignados.
         *
         * @return un nuevo objeto Product.
         */
        public Product build() {
            return new Product(this);
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "ProductEntity [id=" + id + ", name=" + name + ", description=" + description +
                ", price=" + price + ", images=" + images + ", specifications=" + specifications +
                ", termsAndConditions=" + termsAndConditions + ", category=" + category + "]";
    }
}
