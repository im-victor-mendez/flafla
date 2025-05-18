package com.example.flafla.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.flafla.R;
import com.example.flafla.utils.AuthManager;

public abstract class BaseActivity extends AppCompatActivity {
    protected ImageButton menuBtn, searchBtn, cartBtn, profileBtn;
    protected TextView title;

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
        title = findViewById(R.id.title_toolbar_text);
        cartBtn = findViewById(R.id.cart_toolbar_button);
        profileBtn = findViewById(R.id.account_toolbar_button);

        menuBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        });

        searchBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });

        title.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        });

        cartBtn.setOnClickListener(v -> {
            if (new AuthManager().isSignedAsGuest()) {
                Toast.makeText(this, "You must be logged in to access the cart", Toast.LENGTH_SHORT).show();
                return;
            }

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
