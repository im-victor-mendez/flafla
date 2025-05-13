package com.example.flafla.activities;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flafla.R;
import com.example.flafla.adapters.ProductAdapter;
import com.example.flafla.models.Product;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Search Activity</h1>
 * <p>
 * Permite al usuario buscar productos por nombre o categoría.
 * <p>
 * Los resultados se muestran en una lista con imagen, nombre y precio.
 */
public class SearchActivity extends AppCompatActivity {

    // Referencias a la interfaz de usuario
    private EditText editSearch;

    // Adaptador y lista de productos filtrados
    private ProductAdapter adapter;
    private final List<Product> filteredProducts = new ArrayList<>();

    private CollectionReference productsRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_search), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa elementos de la interfaz
        editSearch = findViewById(R.id.edit_search);
        ImageButton btnSearch = findViewById(R.id.search);
        RecyclerView recyclerResults = findViewById(R.id.recycler_products);
        ImageButton closeButton = findViewById(R.id.button_close);


        // Configura el RecyclerView
        recyclerResults.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, filteredProducts, R.layout.item_product_search);
        recyclerResults.setAdapter(adapter);

        // Inicializa Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        productsRef = db.collection("products");

        // Acción al hacer click en el botón de búsqueda
        btnSearch.setOnClickListener(v -> performSearch());

        // Acción al hacer click en el botón de cerrar
        closeButton.setOnClickListener(v -> finish());

        // Acción al presionar "Enter" en el teclado virtual
        editSearch.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false; // Flag to indicate if the event is consumed

            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                performSearch();
                handled = true; // Consume the event
            } else if (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                // Check for null event before accessing its methods
                performSearch();
                handled = true; // Consume the event
            }

            return handled; // Return true if the event was handled, false otherwise
        });
    }

    /**
     * <h1>Perform Search</h1>
     * <p>
     * Ejecuta la búsqueda consultando Firestore y filtrando productos por nombre o categoría.
     */
    private void performSearch() {
        String query = editSearch.getText().toString().toLowerCase().trim();

        // Verifica que se haya ingresado texto
        if (TextUtils.isEmpty(query)) {
            Toast.makeText(this, "Enter a search term", Toast.LENGTH_SHORT).show();
            return;
        }

        filteredProducts.clear(); // Limpia resultados anteriores

        // Obtiene todos los productos desde la base de datos
        productsRef.get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Product product = doc.toObject(Product.class);

                        String name = product.getName().toLowerCase();
                        String type = product.getCategory().name().toLowerCase();

                        // Verifica si el nombre o categoría contiene el texto buscado
                        if (name.contains(query) || type.contains(query)) {
                            filteredProducts.add(product);
                        }
                    }
                    adapter.notifyDataSetChanged(); // Actualiza la lista en pantalla
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error searching for products, please try again", Toast.LENGTH_SHORT).show());
    }

    /**
     * <h1>Dispatch Touch Event</h1>
     * <p>
     * Este metodo sobrescribe el comportamiento de toque de pantalla en la actividad.
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
}
