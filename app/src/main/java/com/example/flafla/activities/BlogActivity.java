package com.example.flafla.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

        db = FirebaseFirestore.getInstance();

        TextView back = findViewById(R.id.back);
        RecyclerView recyclerView = findViewById(R.id.recycler_blog);

        adapter = new ArticleAdapter(articleList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        back.setOnClickListener(v -> finish());

        loadBlogArticles();
    }

    /**
     * <h1>Load Blog Articles</h1>
     * <p>
     * Loads all blog articles from the Firestore "articles" collection.
     * <p>
     * Clears the current list and repopulates it with the fetched data.
     */
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

    /**
     * <h1>Add Article From Snapshot</h1>
     * <p>
     * Converts a Firestore DocumentSnapshot to an Article object
     * and adds it to the article list if valid.
     * <p>
     * Also notifies the adapter to update the UI.
     *
     * @param doc The DocumentSnapshot representing a blog article.
     */
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