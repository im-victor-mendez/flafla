package com.example.flafla.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flafla.R;
import com.example.flafla.adapters.ArticleAdapter;
import com.example.flafla.models.Article;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BlogActivity extends BaseActivity {
    private ArticleAdapter adapter;
    private final List<Article> articleList = new ArrayList<>();
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_blog);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_blog), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupToolbar();

        findViewById(R.id.back).setOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.recycler_blog);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArticleAdapter(articleList, this);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadBlogArticles();
    }

    private void loadBlogArticles() {
        db.collection("articles").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    articleList.clear();
                    adapter.notifyDataSetChanged();

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        addArticleFromSnapshot(doc);
                    }
                })
                .addOnFailureListener(e -> Log.e("BlogActivity", "Failed to fetch articles", e));
    }

    private void addArticleFromSnapshot(@NonNull DocumentSnapshot doc) {
        Article article = doc.toObject(Article.class);
        if (article != null) {
            articleList.add(article);
            Log.d("BlogActivity", "Added article: " + article.getTitle());
            Log.d("BlogActivity", "Article list size: " + articleList.size());
            adapter.notifyItemInserted(articleList.size() - 1);
        }
    }

}