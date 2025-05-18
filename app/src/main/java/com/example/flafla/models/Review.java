package com.example.flafla.models;

import java.util.Date;

public class Review {
    public static class Builder {
        private String author_id;
        private Date created_at;
        private String content;
        private int rating;

        public Builder setAuthor_id(String author_id) {
            this.author_id = author_id;
            return this;
        }

        public Builder setCreated_at(Date created_at) {
            this.created_at = created_at;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setRating(int rating) {
            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Rating must be between 0 and 5.");
            }
            this.rating = rating;
            return this;
        }

        public Review build() {
            return new Review(this);
        }
    }

    private String author_id;
    private Date created_at;
    private String content;

    private int rating;

    public Review() {
        // Requerido por Firestore
    }

    private Review(Builder builder) {
        this.author_id = builder.author_id;
        this.created_at = builder.created_at;
        this.content = builder.content;
        this.rating = builder.rating;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public String getContent() {
        return content;
    }

    public int getRating() {
        return rating;
    }
}
