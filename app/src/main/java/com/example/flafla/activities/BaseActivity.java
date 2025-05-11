package com.example.flafla.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.flafla.R;

public abstract class BaseActivity extends AppCompatActivity {
    protected ImageButton menuBtn, searchBtn, cartBtn, profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        if (toolbar == null) return;

        setSupportActionBar(toolbar);

        menuBtn = findViewById(R.id.menu_toolbar_button);
        searchBtn = findViewById(R.id.search_toolbar_button);
        cartBtn = findViewById(R.id.cart_toolbar_button);
        profileBtn = findViewById(R.id.account_toolbar_button);

        menuBtn.setOnClickListener(v -> {
            Log.d("BaseActivity", "Menu button clicked");
            // TODO: Implementar la actividad del menú
//            Intent intent = new Intent(this, MenuActivity.class);
//            startActivity(intent);
        });

        searchBtn.setOnClickListener(v -> {
            Log.d("BaseActivity", "Search button clicked");
            // TODO: Implementar la actividad de búsqueda
//            Intent intent = new Intent(this, SearchActivity.class);
//            startActivity(intent);
        });

        cartBtn.setOnClickListener(v -> {
            Log.d("BaseActivity", "Cart button clicked");
            // TODO: Implementar la actividad del carrito
//            Intent intent = new Intent(this, CartActivity.class);
//            startActivity(intent);
        });

        profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });
    }
}
