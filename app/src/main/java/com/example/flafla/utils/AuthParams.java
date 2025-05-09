package com.example.flafla.utils;

import android.widget.EditText;

public class AuthParams {
    public String email;
    public String password;
    public EditText emailField;
    public EditText passwordField;

    public AuthParams(String email, String password, EditText emailField, EditText passwordField) {
        this.email = email;
        this.password = password;
        this.emailField = emailField;
        this.passwordField = passwordField;
    }
}
