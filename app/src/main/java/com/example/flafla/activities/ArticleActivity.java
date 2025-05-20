package com.example.flafla.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.flafla.R;
import com.example.flafla.models.Article;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

import io.noties.markwon.Markwon;
import io.noties.markwon.image.coil.CoilImagesPlugin;

public class ArticleActivity extends BaseActivity {
    public static final String EXTRA_ARTICLE = "ARTICLE";
    private FirebaseFirestore db;

    TextView markdownView, title, date, author;
    ImageView image;
    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_article);

        // Handle window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_article), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupToolbar();

        db = FirebaseFirestore.getInstance();

        // Get the article ID from the Intent extras
        String articleId = getIntent().getStringExtra(EXTRA_ARTICLE);

        // Fetch the article data from Firestore
        fetchArticleProduct(articleId);

        // Initialize UI components
        back = findViewById(R.id.back_button);
        title = findViewById(R.id.article_title);
        date = findViewById(R.id.article_date);
        author = findViewById(R.id.article_author);
        image = findViewById(R.id.article_image);
        markdownView = findViewById(R.id.article_content);

        // Set back button click behavior
        back.setOnClickListener(v -> finish());
    }

    /**
     * <h1>Fetch Article Product</h1>
     * <p>
     * Fetches the article from Firestore using its ID.
     *
     * @param articleId The ID of the article document to retrieve.
     */
    private void fetchArticleProduct(String articleId) {
        db.collection(getString(R.string.DOC_ARTICLES))
                .document(articleId)
                .get()
                .addOnSuccessListener(this::onSuccess)
                .addOnFailureListener(this::onFailure);
    }

    /**
     * <h1>On Success</h1>
     * <p>
     * Called when the article document is successfully retrieved.
     *
     * @param doc The Firestore document snapshot of the article.
     */
    private void onSuccess(DocumentSnapshot doc) {
        if (doc.exists()) {
            Article article = doc.toObject(Article.class);
            assert article != null;

            // Initialize Markwon for Markdown rendering, with image support via Coil
            Markwon markwon = Markwon.builder(this)
                    .usePlugin(CoilImagesPlugin.create(this))
                    .build();

            // Format the article date
            String dateFormatted = SimpleDateFormat.getDateInstance().format(article.getDate().toDate());

            // Bind data to views
            title.setText(article.getTitle());
            date.setText(dateFormatted);
            author.setText(article.getAuthor());

            // Load article image using Glide
            Glide.with(this)
                    .load(article.getImage())
                    .into(image);

            // Render Markdown content into the TextView
            markwon.setMarkdown(markdownView, article.getContent());
        } else {
            Toast.makeText(this, "Article not found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * <h1>On Failure</h1>
     * <p>
     * Called when there is an error retrieving the article.
     *
     * @param e The exception that was thrown.
     */
    private void onFailure(Exception e) {
        Toast.makeText(this, "Failed to load article", Toast.LENGTH_SHORT).show();
    }
}
