package com.example.flafla.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

        // Si el usuario ya está autenticado, navega directamente a la actividad principal.
        if (authManager.isUserSignedIn()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        // Encuentra las vistas para el email y la contraseña.
        emailEditText = findViewById(R.id.email_auth);
        passwordEditText = findViewById(R.id.password_auth);

        // Encuentra los botones y asigna los oyentes de click.
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

        emailEditText.setOnEditorActionListener(this::emailListener);
        passwordEditText.setOnEditorActionListener(this::passwordListener);
    }

    /**
     * <h1>Dispatch Touch Event</h1>
     * <p>
     * Este método sobrescribe el comportamiento de toque de pantalla en la actividad.
     * Detecta si el usuario toca fuera de un campo de texto (<code>EditText</code>) y,
     * si es así, oculta el teclado y elimina el foco del campo.
     * </p>
     *
     * <p>
     * Es útil para mejorar la experiencia del usuario en formularios,
     * ya que evita que el teclado se quede abierto innecesariamente.
     * </p>
     *
     * @param ev Evento táctil detectado en la actividad.
     * @return true si el evento fue manejado correctamente, o el comportamiento predeterminado
     * de <code>super.dispatchTouchEvent(ev)</code> en otros casos.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
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

    /**
     * <h1>Reset Password</h1>
     * <p>
     * Método que solicita un enlace de restablecimiento de contraseña al correo proporcionado.
     * </p>
     * Si el campo de correo está vacío, muestra un mensaje pidiendo que se ingrese uno.
     * Si se proporciona un correo válido, llama al `AuthManager` para enviar el correo de recuperación.
     */
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

    /**
     * <h1>Recover Email</h1>
     * <p>
     * Método que simula la recuperación del correo electrónico de una cuenta,
     * reutilizando la lógica de restablecimiento de contraseña.
     * </p>
     * Muestra un mensaje informando al usuario que recibirá un correo si el email existe.
     * Si el campo de email está vacío, se muestra una advertencia.
     */
    private void recoverEmail() {
        if (emailEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter your email to recover your password.", Toast.LENGTH_SHORT).show();
            return;
        }

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

    /**
     * <h1>Email Listener</h1>
     * <p>
     * Escucha el evento del botón de acción del teclado cuando se está
     * escribiendo en el campo de correo electrónico. Si se presiona la tecla
     * "Next", el foco se mueve al campo de contraseña.
     * </p>
     *
     * @param v        TextView que dispara el evento
     * @param actionId Código de acción del teclado (por ejemplo, IME_ACTION_NEXT)
     * @param event    Evento de teclado (puede ser null)
     * @return true si el evento fue manejado, false en caso contrario
     */
    private boolean emailListener(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            passwordEditText.requestFocus();
            return true;
        }
        return false;
    }

    /**
     * <h1>Password Listener</h1>
     * <p>
     * Escucha el evento del botón de acción del teclado cuando se está
     * escribiendo en el campo de contraseña. Si se presiona la tecla
     * "Done", el teclado se oculta.
     * </p>
     *
     * @param v        TextView que dispara el evento
     * @param actionId Código de acción del teclado (por ejemplo, IME_ACTION_DONE)
     * @param event    Evento de teclado (puede ser null)
     * @return true si el evento fue manejado, false en caso contrario
     */
    private boolean passwordListener(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            // Ocultar el teclado
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(passwordEditText.getWindowToken(), 0);
            }
            return true;
        }
        return false;
    }
}