package com.example.flafla.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.flafla.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * <h1>Profile Activity</h1>
 * <p>
 * Actividad que muestra la información del perfil del usuario autenticado.
 * <p>
 * Permite al usuario cerrar sesión.
 */
public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencias a los elementos de la interfaz
        TextView nameTV = findViewById(R.id.name);
        ImageView profileImage = findViewById(R.id.profile_image);
        Button logout = findViewById(R.id.logout);

        TextView orders = findViewById(R.id.orders);
        orders.setOnClickListener(v -> startActivity(new Intent(this, OrdersActivity.class)));

        // Inicializa Firebase Auth
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        assert currentUser != null;

        // Obtiene la información básica del usuario
        String name = currentUser.getDisplayName();
        String email = currentUser.getEmail();
        String phone = currentUser.getPhoneNumber();

        // Muestra la información disponible del usuario (nombre, email o teléfono)
        if (name != null) nameTV.setText(name);
        else if (email != null) nameTV.setText(email);
        else if (phone != null) nameTV.setText(phone);
        else nameTV.setText("Guest");

        // Obtiene la URL de la imagen de perfil del usuario (si existe)
        Uri photoUri = currentUser.getPhotoUrl();
        if (photoUri != null) {
            Glide.with(this).load(photoUri).into(profileImage);
        }

        // Acción del botón de logout
        logout.setOnClickListener(this::onClick);
    }

    /**
     * <h1>On Click</h1>
     * <p>
     * Método que se ejecuta al hacer clic en el botón de logout.
     * <p>
     * Cierra la sesión de Firebase y navega a la pantalla de autenticación.
     */
    private void onClick(View v) {
        auth.signOut();

        Toast.makeText(this, "Session closed", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }
}
