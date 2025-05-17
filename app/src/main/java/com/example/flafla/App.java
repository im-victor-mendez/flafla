package com.example.flafla;

import android.app.Application;

import com.example.flafla.utils.dev.SeederManager;
import com.google.firebase.BuildConfig;
import com.google.firebase.FirebaseApp;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        if (BuildConfig.DEBUG) {
            SeederManager.runOnce(this);
        }

    }
}
