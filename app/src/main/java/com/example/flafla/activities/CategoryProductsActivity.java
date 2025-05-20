package com.example.flafla.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flafla.R;
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
public class CategoryProductsActivity extends BaseActivity {

    public static final String CATEGORY = "CATEGORY";

    private FirebaseFirestore db;
    private ProductAdapter adapter;
    private final List<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_products);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_category_products), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupToolbar();

        db = FirebaseFirestore.getInstance();

        TextView back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.recycler_products);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(this, products, R.layout.item_product);
        recyclerView.setAdapter(adapter);


        String category = getIntent().getStringExtra(CATEGORY);
        fetchCategoryProducts(category);

        TextView indicator = findViewById(R.id.indicator);
        indicator.setText("Shop . " + category);
    }

    /**
     * <h1>Fetch Category Products</h1>
     * <p>
     * Fetches products belonging to the specified category.
     * <p>
     * It queries the categories collection to get the product IDs,
     * then performs batched queries to retrieve the product documents.
     *
     * @param category The category document name (e.g., "flowers" or "plants").
     */
    private void fetchCategoryProducts(String category) {
        db.collection("categories")
                .document(category)
                .get()
                .addOnSuccessListener(this::onSuccess)
                .addOnFailureListener(this::onFailure);
    }

    /**
     * <h1>Chunk List</h1>
     * <p>
     * Splits a list into chunks of size 10 to handle Firestore 'whereIn' query limit.
     *
     * @param list The original list to be chunked.
     * @param <T>  The type of elements in the list.
     * @return A list of sublist, each with up to 10 elements.
     */
    private <T> List<List<T>> chunk(List<T> list) {
        List<List<T>> chunks = new ArrayList<>();

        for (int i = 0; i < list.size(); i += 10) {
            chunks.add(list.subList(i, Math.min(i + 10, list.size())));
        }

        return chunks;
    }

    /**
     * <h1>On Success</h1>
     * <p>
     * Handles a successful fetch of the category document.
     * <p>
     * Retrieves product IDs and loads product details in batches,
     * updating the adapter when all batches are completed.
     *
     * @param doc The Firestore document snapshot for the category.
     */
    private void onSuccess(DocumentSnapshot doc) {
        List<String> ids = (List<String>) doc.get(getString(R.string.products_document));

        if (ids == null || ids.isEmpty()) return;

        List<List<String>> batches = chunk(ids);

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

                        if (completedBatches.incrementAndGet() == batches.size()) {
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(e -> Log.e("CategoryProducts", "Error al cargar productos", e));
        }
    }

    /**
     * <h1>On Failure</h1>
     * <p>
     * Handles failure in fetching the category document.
     * <p>
     * Displays a toast notifying the user of the error.
     *
     * @param e The exception occurred during fetching.
     */
    private void onFailure(Exception e) {
        Toast.makeText(this, "Error al cargar categoría", Toast.LENGTH_SHORT).show();
    }
}
