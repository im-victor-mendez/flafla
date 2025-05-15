package com.example.flafla.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.flafla.R;
import com.example.flafla.adapters.ImageCarouselAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
public class ProductDetailActivity extends AppCompatActivity {
    public static final String EXTRA_PRODUCT = "PRODUCT_ID";
    private FirebaseFirestore db;

    private LinearLayout specifications, termsConditions;
    private ImageView productImage;
    private TextView productName, productPrice, productDescription;
    private Button addToBagButton;

    private ViewPager2 viewPager;
    private Button btnToggleSpecs, btnToggleTerms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        db = FirebaseFirestore.getInstance();

        viewPager = findViewById(R.id.viewPager);
        productName = findViewById(R.id.text_product_name);
        productPrice = findViewById(R.id.text_product_price);
        productDescription = findViewById(R.id.text_product_description);
        addToBagButton = findViewById(R.id.btn_add_to_bag);

        specifications = findViewById(R.id.layout_specifications);
        termsConditions = findViewById(R.id.layout_terms);
        btnToggleSpecs = findViewById(R.id.btn_toggle_specifications);
        btnToggleTerms = findViewById(R.id.btn_toggle_terms);

        btnToggleSpecs.setOnClickListener(v -> toggleVisibility(specifications, btnToggleSpecs, "especificaciones"));
        btnToggleTerms.setOnClickListener(v -> toggleVisibility(termsConditions, btnToggleTerms, "términos y condiciones"));


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
     * Puedes agregar una lógica de manejo de errores aquí, como mostrar un mensaje de error.
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
        String name = document.getString("name");
        Double price = document.getDouble("price");
        String description = document.getString("description");
        List<String> images = (List<String>) document.get("images");
        Map<String, String> specsMap = (Map<String, String>) document.get("specifications");
        Map<String, String> termsMap = (Map<String, String>) document.get("termsAndConditions");

        productName.setText(name);
        productPrice.setText(String.format("$%.2f", price));
        productDescription.setText(description);

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
        container.removeAllViews(); // Limpiar antes de llenar
        if (map == null || map.isEmpty()) return;

        for (Map.Entry<String, String> entry : map.entrySet()) {
            TextView textView = new TextView(this);
            textView.setText(entry.getKey() + ": " + entry.getValue());
            textView.setTextSize(14);
            textView.setPadding(8, 8, 8, 8);
            container.addView(textView);
        }
    }

    private void toggleVisibility(LinearLayout layout, Button button, String label) {
        if (layout.getVisibility() == View.VISIBLE) {
            layout.setVisibility(View.GONE);
            button.setText("Mostrar " + label);
        } else {
            layout.setVisibility(View.VISIBLE);
            button.setText("Ocultar " + label);
        }
    }

}
