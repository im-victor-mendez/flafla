package com.example.flafla.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flafla.R;
import com.example.flafla.adapters.FaqCategoryAdapter;
import com.example.flafla.models.FaqCategory;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Faq Activity</h1>
 * <p>
 * Esta actividad muestra una lista de categorías de preguntas frecuentes (FAQ),
 * cada una con sus respectivas preguntas y respuestas.
 * </p>
 */
public class FaqActivity extends AppCompatActivity {

    private FaqCategoryAdapter adapter;
    private final List<FaqCategory> faqCategories = new ArrayList<>();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_faq);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_faq), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.back_button).setOnClickListener(v -> finish());

        RecyclerView faqRecycler = findViewById(R.id.faqRecycler);
        faqRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FaqCategoryAdapter(faqCategories);
        faqRecycler.setAdapter(adapter);

        fetchFaqData();
    }

    /**
     * Obtiene las categorías de preguntas frecuentes desde Firestore y las muestra.
     */
    private void fetchFaqData() {
        db.collection("faq")
                .get(Source.DEFAULT) // puedes usar Source.CACHE para offline primero
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    faqCategories.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        FaqCategory category = doc.toObject(FaqCategory.class);
                        if (category != null) {
                            faqCategories.add(category);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("FaqActivity", "Error al obtener FAQs", e);
                    Toast.makeText(this, "No se pudo cargar la información", Toast.LENGTH_SHORT).show();
                });
    }
}
