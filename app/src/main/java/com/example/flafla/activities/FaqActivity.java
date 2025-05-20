package com.example.flafla.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
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
        faqRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new FaqCategoryAdapter(faqCategories);
        faqRecycler.setAdapter(adapter);

        fetchFaqData();
    }

    /**
     * <h1>Fetch FAQ Data</h1>
     * <p>
     * Fetches FAQ categories from Firestore and updates the adapter.
     * <p>
     * Uses Source.DEFAULT but can be switched to Source.CACHE for offline-first behavior.
     */
    private void fetchFaqData() {
        db.collection("faq")
                .get(Source.DEFAULT)
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
                    Log.e("FaqActivity", "Failed to fetch FAQ categories", e);
                    Toast.makeText(this, "Failed to load information", Toast.LENGTH_SHORT).show();
                });
    }
}
