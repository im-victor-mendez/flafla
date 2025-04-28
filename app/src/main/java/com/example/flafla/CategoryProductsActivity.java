package com.example.flafla;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flafla.adapters.ProductAdapter;
import com.example.flafla.models.Product;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <h1>Category Products Activity</h1>
 * <p>
 * Activity que muestra un grid de productos según la categoría que
 * reciba en el Intent (flowers o plants).
 * <p>
 * 1. Lee el extra "category" del Intent.
 * <p>
 * 2. Consulta /categories/{category} y extrae la lista de productIds.
 * <p>
 * 3. Con esos IDs hace un solo get a /products con whereIn.
 * <p>
 * 4. Despliega los productos en un RecyclerView de 2 columnas.
 */
public class CategoryProductsActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "CATEGORY";

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_products);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(this, products);
        recyclerView.setAdapter(adapter);


        String category = getIntent().getStringExtra(EXTRA_CATEGORY);
        fetchCategoryProducts(category);
    }

    /**
     * <h1>Fetch Category Products</h1>
     * <p>
     * 1. Consulta el documento categories/{category}.
     * <p>
     * 2. Extrae la lista de IDs de productos.
     * <p>
     * 3. Lanza una query whereIn a products con esos IDs.
     *
     * @param category El nombre del documento en /categories (flowers o plants).
     */
    private void fetchCategoryProducts(String category) {
        db.collection("categories")
                .document(category)
                .get()
                .addOnSuccessListener(this::onSuccess)
                .addOnFailureListener(this::onFailure);
    }

    /**
     * <h1>Chunk</h1>
     * <p>
     * Divide una lista en trozos de tamaño maxSize.
     */
    private <T> List<List<T>> chunk(List<T> list, int maxSize) {
        List<List<T>> chunks = new ArrayList<>();

        for (int i = 0; i < list.size(); i += maxSize) {
            chunks.add(list.subList(i, Math.min(i + maxSize, list.size())));
        }

        return chunks;
    }

    /**
     * <h1>On Success</h1>
     */
    private void onSuccess(DocumentSnapshot doc) {
        List<String> ids = (List<String>) doc.get(getString(R.string.products_document));

        if (ids == null || ids.isEmpty()) return;


        // Hacer batched get de productos (Firestore permite hasta 10 en whereIn)
        // Si son > 10, fragmenta en lotes de 10
        List<List<String>> batches = chunk(ids, 10);

        AtomicInteger completedBatches = new AtomicInteger(0);

        for (List<String> batch : batches) {
            db.collection(getString(R.string.products_collection))
                    .whereIn(FieldPath.documentId(), batch)
                    .get()
                    .addOnSuccessListener(qsnap -> {
                        for (DocumentSnapshot psnap : qsnap.getDocuments()) {
                            Product p = psnap.toObject(Product.class);
                            products.add(p);
                        }

                        // Incrementar contador de batches completados
                        if (completedBatches.incrementAndGet() == batches.size()) {
                            // Si todos los batches ya terminaron, actualizar el adapter
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("CategoryProducts", "Error al cargar productos", e);
                    });
        }
    }

    private void onFailure(Exception e) {
        Toast.makeText(this, "Error al cargar categoría", Toast.LENGTH_SHORT).show();
    }
}
