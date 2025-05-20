package com.example.flafla.models;

import com.google.firebase.Timestamp;

import java.util.List;

/**
 * <h1>Article</h1>
 * <p>
 * Modelo de datos para un artículo de la colección "articles" en Firestore.
 */
public class Article {
    private String id;
    private String title;
    private String image;
    private List<String> tags;
    private String content;
    private Timestamp date;
    private String author;

    /**
     * Constructor vacío necesario para Firestore deserialización.
     */
    public Article() {
        // Firestore necesita un constructor vacío
    }

    /**
     * Constructor para crear un objeto Article manualmente.
     */
    public Article(String id, String title, String image, List<String> tags, String content, Timestamp date, String author) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.tags = tags;
        this.content = content;
        this.date = date;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
