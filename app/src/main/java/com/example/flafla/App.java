package com.example.flafla;

import android.app.Application;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class App extends Application {
    private FirebaseFirestore db;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        db = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore getFirestore() {
        return db;
    }
}
