package com.example.flafla.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.flafla.R;
import com.example.flafla.utils.AuthCallback;
import com.example.flafla.utils.AuthManager;
import com.example.flafla.utils.AuthParams;

/**
 * <h1>Auth Activity</h1>
 * <p>
 * Esta actividad maneja el inicio de sesión, la creación de cuenta y el inicio de sesión anónimo
 * mediante Firebase Auth.
 * </p>
 * Contiene los campos de entrada para el email y la contraseña, así como
 * botones para acceder a las funcionalidades de inicio de sesión, registro y acceso como invitado.
 */
public class AuthActivity extends AppCompatActivity {

    private AuthManager authManager;
    private EditText emailEditText;
    private EditText passwordEditText;

    /**
     * <h1>On Create</h1>
     * <p>
     * Método llamado cuando se crea la actividad.
     * </p>
     * Inicializa los campos y botones, y configura los oyentes de click para
     * cada uno de los botones (Iniciar sesión, Crear cuenta, Iniciar como invitado).
     *
     * @param savedInstanceState El estado guardado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);

        // Configura los márgenes de la actividad para no sobreponerse con los bordes del sistema.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_auth), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa el AuthManager.
        authManager = new AuthManager();

        // Encuentra las vistas para el email y la contraseña.
        emailEditText = findViewById(R.id.email_auth);
        passwordEditText = findViewById(R.id.password_auth);

        // Encuentra los botones y asigna los oyentes de clic.
        Button loginButton = findViewById(R.id.login_auth);
        Button createButton = findViewById(R.id.create_auth);
        Button guestButton = findViewById(R.id.guest_auth);
        Button findPasswordButton = findViewById(R.id.find_password_auth);
        Button findIdButton = findViewById(R.id.find_id_auth);


        loginButton.setOnClickListener(v -> login());
        createButton.setOnClickListener(v -> createAccount());
        guestButton.setOnClickListener(v -> signInAnonymously());

        findPasswordButton.setOnClickListener(v -> resetPassword());
        findIdButton.setOnClickListener(v -> recoverEmail());
    }

    /**
     * <h1>Login</h1>
     * <p>
     * Método que maneja el inicio de sesión de un usuario.
     * </p>
     * Valida los datos de entrada (email y contraseña), y llama a `AuthManager`
     * para intentar iniciar sesión con Firebase.
     * <p>
     * Si la autenticación es exitosa, navega a la actividad de inicio (HomeActivity).
     */
    private void login() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Crea el objeto AuthParams con los parámetros necesarios.
        AuthParams authParams = new AuthParams(email, password, emailEditText, passwordEditText);

        // Llama a AuthManager para intentar iniciar sesión.
        authManager.login(authParams, new AuthCallback() {
            @Override
            public void onSuccess(String message) {
                // Si la autenticación es exitosa, muestra un mensaje y navega a la actividad principal.
                Toast.makeText(AuthActivity.this, message, Toast.LENGTH_SHORT).show();
                goToHomeActivity();
            }

            @Override
            public void onError(Exception exception) {
                // Si hay un error, muestra un mensaje de error.
                String errorMessage = "Error: " + exception.getMessage();
                Toast.makeText(AuthActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * <h1>Create Account</h1>
     * <p>
     * Método que maneja la creación de una nueva cuenta.
     * </p>
     * Valida los datos de entrada (email y contraseña), y llama a `AuthManager`
     * para intentar crear la cuenta en Firebase.
     * <p>
     * Si la creación de cuenta es exitosa, navega a la actividad de inicio (HomeActivity).
     */
    private void createAccount() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Crea el objeto AuthParams con los parámetros necesarios.
        AuthParams authParams = new AuthParams(email, password, emailEditText, passwordEditText);

        // Llama a AuthManager para intentar crear la cuenta.
        authManager.createAccount(authParams, new AuthCallback() {
            @Override
            public void onSuccess(String message) {
                // Si la cuenta se crea exitosamente, muestra un mensaje y navega a la actividad principal.
                Toast.makeText(AuthActivity.this, message, Toast.LENGTH_SHORT).show();
                goToHomeActivity();
            }

            @Override
            public void onError(Exception exception) {
                // Si hay un error, muestra un mensaje de error.
                String errorMessage = "Error: " + exception.getMessage();
                Toast.makeText(AuthActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * <h1>Sign In Anonymously</h1>
     * <p>
     * Método que maneja el inicio de sesión anónimo. Llama a `AuthManager` para intentar iniciar
     * sesión de manera anónima con Firebase.
     * </p>
     * Si es exitoso, navega a la actividad de inicio (HomeActivity).
     */
    private void signInAnonymously() {
        // Llama a AuthManager para intentar iniciar sesión anónimamente.
        authManager.signInAnonymously(new AuthCallback() {
            @Override
            public void onSuccess(String message) {
                // Si la autenticación es exitosa, muestra un mensaje y navega a la actividad principal.
                Toast.makeText(AuthActivity.this, message, Toast.LENGTH_SHORT).show();
                goToHomeActivity();
            }

            @Override
            public void onError(Exception exception) {
                // Si hay un error, muestra un mensaje de error.
                String errorMessage = "Error: " + exception.getMessage();
                Toast.makeText(AuthActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "Enter your email to recover your password.", Toast.LENGTH_SHORT).show();
            return;
        }

        authManager.resetPassword(email, new AuthCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(AuthActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(AuthActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void recoverEmail() {
        Toast.makeText(this, "If this email is registered, you will receive information to recover your account.", Toast.LENGTH_LONG).show();
        resetPassword();
    }


    /**
     * <h1>Go To Home Activity</h1>
     * <p>
     * Método que navega a la actividad de inicio (HomeActivity)
     * después de una autenticación exitosa.
     * </p>
     * Finaliza la actividad actual.
     */
    private void goToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
