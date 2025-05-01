package com.example.flafla.models;

import java.util.List;

/**
 * <h1>Home Page Content</h1>
 * <p>
 * Modelo de datos para representar el contenido del documento "homepage"
 * de la colección "home" en Firestore.
 */
public class HomePageContent {

    private String promo_text;
    private List<String> featured_articles;
    private String banner_title;
    private String banner_image;

    /**
     * Constructor vacío requerido por Firestore para deserialización automática.
     */
    public HomePageContent() {
    }

    public String getPromo_text() {
        return promo_text;
    }

    public void setPromo_text(String promo_text) {
        this.promo_text = promo_text;
    }

    public List<String> getFeatured_articles() {
        return featured_articles;
    }

    public void setFeatured_articles(List<String> featured_articles) {
        this.featured_articles = featured_articles;
    }

    public String getBanner_title() {
        return banner_title;
    }

    public void setBanner_title(String banner_title) {
        this.banner_title = banner_title;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }
}
