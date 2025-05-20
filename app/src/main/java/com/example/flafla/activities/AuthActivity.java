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
import com.example.flafla.models.User;
import com.example.flafla.utils.AuthCallback;
import com.example.flafla.utils.AuthManager;
import com.example.flafla.utils.AuthParams;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthActivity extends AppCompatActivity {
    private AuthManager authManager;
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_auth), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authManager = new AuthManager();

        // If the user is already authenticated, go directly to the main activity.
        if (authManager.isUserSignedIn()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        emailEditText = findViewById(R.id.email_auth);
        passwordEditText = findViewById(R.id.password_auth);

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
     * This method overrides the screen touch behavior in the activity.
     * It detects whether the user taps outside of a text field (<code>EditText</code>) and,
     * if so, hides the keyboard and clears the focus from the field.
     * </p>
     *
     * <p>
     * Useful for improving the user experience in forms
     * by avoiding unnecessary keyboard visibility.
     * </p>
     *
     * @param ev Touch event detected in the activity.
     * @return true if the event was handled correctly, or the default behavior
     * from <code>super.dispatchTouchEvent(ev)</code> otherwise.
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
     * Method that handles user login.
     * </p>
     * Validates input data (email and password), and calls `AuthManager`
     * to attempt logging in with Firebase.
     * <p>
     * If authentication is successful, navigates to the main activity (HomeActivity).
     */
    private void login() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        AuthParams authParams = new AuthParams(email, password, emailEditText, passwordEditText);

        authManager.login(authParams, new AuthCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(AuthActivity.this, message, Toast.LENGTH_SHORT).show();
                goToHomeActivity();
            }

            @Override
            public void onError(Exception exception) {
                String errorMessage = "Error: " + exception.getMessage();
                Toast.makeText(AuthActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * <h1>Create Account</h1>
     * <p>
     * Method that handles account creation.
     * </p>
     * Validates input data (email and password), and calls `AuthManager`
     * to attempt creating the account with Firebase.
     * <p>
     * If account creation is successful, navigates to the main activity (HomeActivity).
     */
    private void createAccount() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        AuthParams authParams = new AuthParams(email, password, emailEditText, passwordEditText);

        authManager.createAccount(authParams, new AuthCallback() {
            @Override
            public void onSuccess(String message) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                User user = new User.Builder().setEmail(email).setId(authManager.getCurrentUserId()).build();
                db.collection("users").document(user.getId()).set(user);

                Toast.makeText(AuthActivity.this, message, Toast.LENGTH_SHORT).show();
                goToHomeActivity();
            }

            @Override
            public void onError(Exception exception) {
                String errorMessage = "Error: " + exception.getMessage();
                Toast.makeText(AuthActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * <h1>Sign In Anonymously</h1>
     * <p>
     * Method that handles anonymous login. Calls `AuthManager` to attempt
     * signing in anonymously using Firebase.
     * </p>
     * If successful, navigates to the main activity (HomeActivity).
     */
    private void signInAnonymously() {
        authManager.signInAnonymously(new AuthCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(AuthActivity.this, message, Toast.LENGTH_SHORT).show();
                goToHomeActivity();
            }

            @Override
            public void onError(Exception exception) {
                String errorMessage = "Error: " + exception.getMessage();
                Toast.makeText(AuthActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * <h1>Reset Password</h1>
     * <p>
     * Method that requests a password reset link to be sent to the provided email.
     * </p>
     * If the email field is empty, displays a message asking for one.
     * If a valid email is provided, calls `AuthManager` to send the recovery email.
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
     * Method that simulates recovering the email address for an account,
     * reusing the password reset logic.
     * </p>
     * Displays a message informing the user that they will receive an email if it exists.
     * If the email field is empty, a warning is shown.
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
     * Method that navigates to the main activity (HomeActivity)
     * after a successful authentication.
     * </p>
     * Finishes the current activity.
     */
    private void goToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * <h1>Email Listener</h1>
     * <p>
     * Listens for the keyboard action button event when typing
     * in the email field. If the "Next" key is pressed,
     * focus moves to the password field.
     * </p>
     *
     * @param v        TextView that triggered the event
     * @param actionId Keyboard action code (e.g., IME_ACTION_NEXT)
     * @param event    Key event (can be null)
     * @return true if the event was handled, false otherwise
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
     * Listens for the keyboard action button event when typing
     * in the password field. If the "Done" key is pressed,
     * the keyboard is hidden.
     * </p>
     *
     * @param v        TextView that triggered the event
     * @param actionId Keyboard action code (e.g., IME_ACTION_DONE)
     * @param event    Key event (can be null)
     * @return true if the event was handled, false otherwise
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