package com.example.flafla.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthManager {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public boolean validateFields(AuthParams params) {
        boolean valid = true;

        if (params.email.isEmpty()) {
            params.emailField.setError("Email is required");
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(params.email).matches()) {
            params.emailField.setError("Invalid email format");
            valid = false;
        }

        if (params.password.isEmpty()) {
            params.passwordField.setError("Password is required");
            valid = false;
        }

        return valid;
    }

    public void login(AuthParams params, AuthCallback callback) {
        if (!validateFields(params)) return;

        auth.signInWithEmailAndPassword(params.email, params.password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        callback.onSuccess("Authenticated");
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    public void createAccount(AuthParams params, AuthCallback callback) {
        if (!validateFields(params)) return;

        auth.createUserWithEmailAndPassword(params.email, params.password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess("Account created successfully");
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    public void signInAnonymously(AuthCallback callback) {
        auth.signInAnonymously()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess("Signed in as guest");
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    public void resetPassword(String email, AuthCallback callback) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess("A password reset email has been sent.");
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    public boolean isUserSignedIn() {
        return auth.getCurrentUser() != null;
    }
}
