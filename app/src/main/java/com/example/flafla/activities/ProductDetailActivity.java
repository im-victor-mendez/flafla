package com.example.flafla.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.flafla.R;
import com.example.flafla.adapters.ImageCarouselAdapter;
import com.example.flafla.adapters.ReviewAdapter;
import com.example.flafla.models.Review;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <h1>Product Detail Activity</h1>
 * <p>
 * Activity para mostrar los detalles de un producto específico.
 * <p>
 * Carga la información del producto desde Firestore y la muestra en la interfaz de usuario.
 */
public class ProductDetailActivity extends BaseActivity {
    public static final String EXTRA_PRODUCT = "PRODUCT_ID";
    private FirebaseFirestore db;

    private LinearLayout specifications, termsConditions;
    private TextView indicator;
    private TextView productName;
    private TextView productPrice;
    private TextView productDescription;

    private ViewPager2 viewPager;
    private ImageButton toggleSpecsBtn, toggleTermsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_product_detail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupToolbar();

        db = FirebaseFirestore.getInstance();

        TextView back = findViewById(R.id.back_button);

        back.setOnClickListener(v -> finish());

        indicator = findViewById(R.id.product_indicator);
        viewPager = findViewById(R.id.product_images);
        productName = findViewById(R.id.text_product_name);
        productPrice = findViewById(R.id.text_product_price);
        productDescription = findViewById(R.id.text_product_description);

        specifications = findViewById(R.id.layout_specifications);
        termsConditions = findViewById(R.id.layout_terms);
        ConstraintLayout specsLayout = findViewById(R.id.specifications_layout);
        ConstraintLayout termsLayout = findViewById(R.id.terms_layout);
        toggleSpecsBtn = findViewById(R.id.toggle_specifications_button);
        toggleTermsBtn = findViewById(R.id.toggle_terms_button);

        specsLayout.setOnClickListener(v -> toggleVisibility(specifications, toggleSpecsBtn));
        termsLayout.setOnClickListener(v -> toggleVisibility(termsConditions, toggleTermsBtn));

        toggleSpecsBtn.setOnClickListener(v -> toggleVisibility(specifications, toggleSpecsBtn));
        toggleTermsBtn.setOnClickListener(v -> toggleVisibility(termsConditions, toggleTermsBtn));


        String productId = getIntent().getStringExtra(EXTRA_PRODUCT);
        loadProduct(productId);
    }

    /**
     * <h1>Load Product</h1>
     * <p>
     * Carga la información del producto desde Firestore usando el ID proporcionado.
     *
     * @param productId El ID del producto que se quiere cargar.
     */
    private void loadProduct(String productId) {
        String collection = getString(R.string.products_collection);

        // Realizamos la consulta en Firestore para obtener el documento del producto
        db.collection(collection).document(productId)
                .get()
                .addOnSuccessListener(this::onSuccess)  // Si la consulta es exitosa, manejar el resultado
                .addOnFailureListener(this::onFailure); // Si ocurre un error, manejar el fallo
    }

    /**
     * <h1>On Success</h1>
     * <p>
     * Metodo que maneja la respuesta exitosa de la consulta a Firestore.
     * Si el documento existe, se procede a mostrar la información.
     *
     * @param documentSnapshot El documento recuperado de Firestore.
     */
    private void onSuccess(DocumentSnapshot documentSnapshot) {
        // Verificar si el documento existe
        if (documentSnapshot.exists()) showProduct(documentSnapshot);
    }

    /**
     * <h1>On Failure</h1>
     * <p>
     * Metodo que maneja el fallo en la consulta a Firestore.
     * <p>
     *
     * @param exception La excepción que ocurrió al intentar recuperar el producto.
     */
    private void onFailure(Exception exception) {
    }

    /**
     * <h1>Show Product</h1>
     * <p>
     * Muestra la información del producto en la UI.
     * <p>
     * Esta función obtiene la información del documento de Firestore y la establece en los elementos UI correspondientes.
     *
     * @param document El documento de Firestore que contiene los datos del producto.
     */
    private void showProduct(@NonNull DocumentSnapshot document) {
        String category = document.getString("category");
        String name = document.getString("name");
        Double price = document.getDouble("price");
        String description = document.getString("description");
        List<String> images = (List<String>) document.get("images");
        Map<String, String> specsMap = (Map<String, String>) document.get("specifications");
        Map<String, String> termsMap = (Map<String, String>) document.get("termsAndConditions");

        indicator.setText("Shop, ".toUpperCase() + category);
        productName.setText(name);
        productPrice.setText(String.format("$%.0f", price));
        productDescription.setText(description);

        RecyclerView recyclerView = findViewById(R.id.review_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<Review> reviewList = new ArrayList<>();
        ReviewAdapter reviewAdapter = new ReviewAdapter(reviewList);
        recyclerView.setAdapter(reviewAdapter);
        document.getReference()
                .collection("reviews")
                .orderBy("created_at", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Review review = doc.toObject(Review.class);
                        reviewList.add(review);
                    }

                    if (reviewList.isEmpty()) {
                        findViewById(R.id.review_title).setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        findViewById(R.id.review_divider).setVisibility(View.GONE);
                    }

                    reviewAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar reseñas", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error", e);
                });


        DotsIndicator dotsIndicator = findViewById(R.id.dots_indicator);

        // Carrusel de imágenes
        ImageCarouselAdapter adapter = new ImageCarouselAdapter(this, images);
        viewPager.setAdapter(adapter);

        dotsIndicator.setViewPager2(viewPager);

        displayKeyValues(specifications, specsMap);
        displayKeyValues(termsConditions, termsMap);
    }

    /**
     * <h1>Display Key Values</h1>
     * <p>
     * Este metodo se encarga de agregar un TextView dinámicamente para cada par clave-valor
     * en el mapa proporcionado, y los agrega a un contenedor LinearLayout.
     *
     * @param container El LinearLayout donde se agregarán los TextViews.
     * @param map       El mapa que contiene las claves y valores que se desean mostrar.
     */
    private void displayKeyValues(LinearLayout container, Map<String, String> map) {
        container.removeAllViews();
        if (map == null || map.isEmpty()) return;

        for (Map.Entry<String, String> entry : map.entrySet()) {
            // Crear un LinearLayout horizontal
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            row.setPadding(8, 8, 8, 8);

            // Crear TextView para la clave (key)
            TextView keyView = new TextView(this);
            keyView.setText(entry.getKey() + ": ");
            keyView.setTextColor(getColor(R.color.brown));
            keyView.setTextSize(14);
            keyView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            // Crear TextView para el valor (value)
            TextView valueView = new TextView(this);
            valueView.setText(entry.getValue());
            valueView.setTextColor(getColor(R.color.brown_toned));
            valueView.setTextSize(14);
            valueView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            // Agregar ambos TextView al row
            row.addView(keyView);
            row.addView(valueView);

            // Agregar el row al contenedor principal
            container.addView(row);
        }
    }


    private void toggleVisibility(LinearLayout layout, ImageButton button) {
        if (layout.getVisibility() == View.VISIBLE) {
            layout.setVisibility(View.GONE);
            button.setImageResource(R.drawable.add);
        } else {
            layout.setVisibility(View.VISIBLE);
            button.setImageResource(R.drawable.remove);
        }
    }

}
