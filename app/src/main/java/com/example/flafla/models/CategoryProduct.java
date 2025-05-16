package com.example.flafla.models;

import java.util.List;

public class CategoryProduct {
    private String name;
    private List<String> products;
    private String image;

    public CategoryProduct() {
        // Firestore necesita constructor vacío
    }

    public CategoryProduct(String name, List<String> products, String image) {
        this.name = name;
        this.products = products;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public List<String> getProducts() {
        return products;
    }

    public String getImage() {
        return image;
    }
}
