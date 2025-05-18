package com.example.flafla.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.flafla.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_menu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Cerrar menú al presionar botón
        ImageButton btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> finish());

        // Manejo de clics del menú
        // TODO: Replace with navigation feature
        findViewById(R.id.menu_about).setOnClickListener(v -> showToast("ABOUT"));
        findViewById(R.id.menu_shop).setOnClickListener(v -> {
            startActivity(new Intent(this, ShopActivity.class));
            finish();
        });
        findViewById(R.id.menu_people).setOnClickListener(v -> showToast("PEOPLE"));
        findViewById(R.id.menu_blog).setOnClickListener(v -> {
            startActivity(new Intent(this, BlogActivity.class));
            finish();
        });
        findViewById(R.id.menu_contact).setOnClickListener(v -> {
            startActivity(new Intent(this, ContactActivity.class));
            finish();
        });
        findViewById(R.id.menu_faq).setOnClickListener(v -> {
            startActivity(new Intent(this, FaqActivity.class));
            finish();
        });
        findViewById(R.id.menu_catalogue).setOnClickListener(v -> showToast("CATALOGUE"));
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg + " clicked", Toast.LENGTH_SHORT).show();
    }
}
