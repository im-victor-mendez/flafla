package com.example.flafla.models;

import java.util.Date;

public class Review {
    public static class Builder {
        private int authorId;
        private Date createdAt;
        private int entityId;
        private String content;
        private int rating;

        public Builder setAuthorId(int authorId) {
            this.authorId = authorId;
            return this;
        }

        public Builder setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder setEntityId(int entityId) {
            this.entityId = entityId;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setRating(int rating) {
            if (rating < 0 || rating >= 5) {
                throw new IllegalArgumentException("Rating must be between 0 and 5.");
            }
            this.rating = rating;
            return this;
        }

        public Review build() {
            return new Review(this);
        }
    }

    private final int authorId;
    private final Date createdAt;
    private final int entityId;
    private final String content;

    private final int rating;

    private Review(Builder builder) {
        this.authorId = builder.authorId;
        this.createdAt = builder.createdAt;
        this.entityId = builder.entityId;
        this.content = builder.content;
        this.rating = builder.rating;
    }

    @Override
    public String toString() {
        return "Review [authorId=" + authorId + ", createdAt=" + createdAt + ", entityId=" + entityId + ", content="
                + content + ", rating=" + rating + "]";
    }

    public int getAuthorId() {
        return authorId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public int getEntityId() {
        return entityId;
    }

    public String getContent() {
        return content;
    }

    public int getRating() {
        return rating;
    }
}
