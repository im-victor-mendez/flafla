package com.example.flafla.models;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Faq Category</h1>
 * <p>
 * Modelo que representa una categoría de preguntas frecuentes (FAQ) en Firestore.
 * Cada documento en la colección "faq" corresponde a una categoría con su lista de preguntas y respuestas.
 * </p>
 */
public class FaqCategory {

    private final String category;
    private final List<FaqQuestion> questions;

    /**
     * Constructor vacío necesario para la deserialización de Firestore.
     */
    public FaqCategory() {
        this.category = "";
        this.questions = new ArrayList<>();
    }

    /**
     * Constructor principal a través de patrón Builder.
     */
    protected FaqCategory(Builder<?> builder) {
        this.category = builder.category;
        this.questions = builder.questions;
    }

    public static class Builder<T extends Builder<T>> {
        private String category;
        private List<FaqQuestion> questions = new ArrayList<>();

        public T setCategory(String category) {
            this.category = category;
            return self();
        }

        public T setQuestions(List<FaqQuestion> questions) {
            this.questions = questions;
            return self();
        }

        public T addQuestion(FaqQuestion question) {
            this.questions.add(question);
            return self();
        }

        protected T self() {
            return (T) this;
        }

        public FaqCategory build() {
            return new FaqCategory(this);
        }
    }

    public String getCategory() {
        return category;
    }

    public List<FaqQuestion> getQuestions() {
        return questions;
    }

    @NonNull
    @Override
    public String toString() {
        return "FaqCategory{" +
                "category='" + category + '\'' +
                ", questions=" + questions +
                '}';
    }
}
