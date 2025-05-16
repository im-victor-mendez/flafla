package com.example.flafla.models;

import androidx.annotation.NonNull;

/**
 * <h1>Faq Question</h1>
 * <p>
 * Representa una sola pregunta y su respuesta en una categoría de FAQ.
 */
public class FaqQuestion {
    private String question;
    private String answer;

    /**
     * Constructor vacío necesario para Firestore.
     */
    public FaqQuestion() {
        // Firestore requires a no-arg constructor
    }

    public FaqQuestion(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @NonNull
    @Override
    public String toString() {
        return "FaqQuestion{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}