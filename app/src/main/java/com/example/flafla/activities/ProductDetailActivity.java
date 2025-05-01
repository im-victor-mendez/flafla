package com.example.flafla.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flafla.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private FirebaseFirestore db;

    private LinearLayout specifications, termsConditions;
    private ImageView productImage;
    private TextView productName, productPrice, productDescription;
    private Button addToBagButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        db = FirebaseFirestore.getInstance();

        // Obtener el ID del producto pasado a través de un Intent
        String productId = getIntent().getStringExtra(getString(R.string.product_id));

        // Cargar producto desde Firestore
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
     * Método que maneja la respuesta exitosa de la consulta a Firestore.
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
     * Método que maneja el fallo en la consulta a Firestore.
     * <p>
     * Puedes agregar una lógica de manejo de errores aquí, como mostrar un mensaje de error.
     *
     * @param exception La excepción que ocurrió al intentar recuperar el producto.
     */
    private void onFailure(Exception exception) {
        // Aquí podrías agregar una acción en caso de error, como mostrar un mensaje al usuario.
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
        // Obtener los datos del producto desde el documento de Firestore
        String nameDocument = getString(R.string.name_document_snapshot);
        String name = document.getString(nameDocument);

        String priceDocument = getString(R.string.price_document_snapshot);
        Double price = document.getDouble(priceDocument);

        String descriptionDocument = getString(R.string.description_document_snapshot);
        String description = document.getString(descriptionDocument);

        // Obtener las imágenes del producto (pueden ser varias imágenes)
        String imagesDocument = getString(R.string.images_document_snapshot);
        List<String> images;

        // Obtener especificaciones y términos del producto
        String specificationsDocument = getString(R.string.specifications_document_snapshot);
        Map<String, String> specsMap = (Map<String, String>) document.get(specificationsDocument);

        String termsAndConditionsDocument = getString(R.string.terms_and_conditions_document_snapshot);
        Map<String, String> termsMap = (Map<String, String>) document.get(termsAndConditionsDocument);

        // Establecer los valores en los TextViews correspondientes
        productName.setText(name);
        productPrice.setText(String.format("$%.2f", price));
        productDescription.setText(description);

        if (document.get(imagesDocument) != null)
            images = (List<String>) document.get(imagesDocument);

        else images = new ArrayList<>();

        // Mostrar las especificaciones y términos en sus respectivos contenedores
        displayKeyValues(specifications, specsMap);
        displayKeyValues(termsConditions, termsMap);
    }

    /**
     * <h1>Display Key Values</h1>
     * <p>
     * Este método se encarga de agregar un TextView dinámicamente para cada par clave-valor
     * en el mapa proporcionado, y los agrega a un contenedor LinearLayout.
     *
     * @param container El LinearLayout donde se agregarán los TextViews.
     * @param map       El mapa que contiene las claves y valores que se desean mostrar.
     */
    private void displayKeyValues(LinearLayout container, Map<String, String> map) {
        // Verificar si el mapa es null o está vacío, en cuyo caso no se hace nada
        if (map == null || map.isEmpty()) return;

        // Iterar sobre el mapa para agregar un TextView por cada par key-value
        for (Map.Entry<String, String> entry : map.entrySet()) {

            // Crear un nuevo TextView para mostrar cada clave-valor
            TextView textView = new TextView(this);

            // Establecer el texto del TextView con el formato adecuado ("key: value")
            String text = String.format("%s: %s", entry.getKey(), entry.getValue());
            textView.setText(text);

            // Personalización del TextView (opcional)
            textView.setTextSize(16); // Tamaño de la fuente
            textView.setPadding(0, 10, 0, 10); // Espaciado entre los TextViews

            // Agregar el nuevo TextView al contenedor LinearLayout
            container.addView(textView);
        }
    }
}
