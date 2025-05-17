package com.example.flafla.models;

public class User {
    private String id;
    private String name;
    private String email;
    private String phone;
    private Cart cart;

    public User() {
        // Constructor vacío necesario para Firestore
    }

    private User(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.phone = builder.phone;
        this.cart = builder.cart;
    }

    public static class Builder {
        private String id;
        private String name;
        private String email;
        private String phone;
        private Cart cart;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setCart(Cart cart) {
            this.cart = cart;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Cart getCart() {
        return cart;
    }
}
