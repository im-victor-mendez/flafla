package com.example.flafla.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
    private ImageButton btnSearch;
    private RecyclerView recyclerResults;

    // Adaptador y lista de productos filtrados
    private ProductAdapter adapter;
    private final List<Product> filteredProducts = new ArrayList<>();

    // Firestore
    private FirebaseFirestore db;
    private CollectionReference productsRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // TODO: Implementar elementos en el layout
        // Inicializa elementos de la interfaz
//        editSearch = findViewById(R.id.edit_search);
//        btnSearch = findViewById(R.id.btn_search);
//        recyclerResults = findViewById(R.id.recycler_products);

        // Configura el RecyclerView
        recyclerResults.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, filteredProducts);
        recyclerResults.setAdapter(adapter);

        // Inicializa Firestore
        db = FirebaseFirestore.getInstance();
        productsRef = db.collection("products");

        // Acción al hacer clic en el botón de búsqueda
        btnSearch.setOnClickListener(v -> performSearch());

        // Acción al presionar "Enter" en el teclado virtual
        editSearch.setOnEditorActionListener((v, actionId, event) -> {
            boolean editorIsSearchOrDone = actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE;
            boolean keyEvent = event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN;
            boolean eventIsNotNull = event != null && keyEvent;

            if (editorIsSearchOrDone || eventIsNotNull) {
                performSearch();
                return true;
            }

            return false;
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
            Toast.makeText(this, "Ingresa un término de búsqueda", Toast.LENGTH_SHORT).show();
            return;
        }

        filteredProducts.clear(); // Limpia resultados anteriores

        // Obtiene todos los productos desde la base de datos
        productsRef.get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Product product = doc.toObject(Product.class);

                        String name = product.getName().toLowerCase();
                        String category = product.getCategory().name().toLowerCase();

                        // Verifica si el nombre o categoría contiene el texto buscado
                        if (name.contains(query) || category.contains(query)) {
                            filteredProducts.add(product);
                        }
                    }
                    adapter.notifyDataSetChanged(); // Actualiza la lista en pantalla
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al buscar productos", Toast.LENGTH_SHORT).show();
                });
    }
}
