package com.example.flafla.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.flafla.R;
import com.example.flafla.models.Article;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import io.noties.markwon.Markwon;
import io.noties.markwon.image.coil.CoilImagesPlugin;

public class ArticleActivity extends AppCompatActivity {
    public static final String EXTRA_ARTICLE = "ARTICLE";
    private FirebaseFirestore db;
    private Article article;
    TextView markdownView, title, date, author;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_article);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_article), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        setupToolbar();

        db = FirebaseFirestore.getInstance();


        String articleId = getIntent().getStringExtra(EXTRA_ARTICLE);

        fetchArticleProduct(articleId);

        markdownView = findViewById(R.id.article_content);
        title = findViewById(R.id.article_title);
        date = findViewById(R.id.article_date);
        author = findViewById(R.id.article_author);
        image = findViewById(R.id.article_image);
    }

    private void fetchArticleProduct(String article) {
        db.collection(getString(R.string.DOC_ARTICLES))
                .document(article)
                .get()
                .addOnSuccessListener(this::onSuccess)
                .addOnFailureListener(this::onFailure);
    }

    /**
     * <h1>On Success</h1>
     */
    private void onSuccess(DocumentSnapshot doc) {
        if (doc.exists()) {
            article = doc.toObject(Article.class);

            Markwon markwon = Markwon.builder(this)
                    .usePlugin(CoilImagesPlugin.create(this))
                    .build();

            title.setText(article.getTitle());
            date.setText(article.getDate().toString());
            author.setText(article.getAuthor());
            Glide.with(this)
                    .load(article.getImage())
                    .into(image);

            markwon.setMarkdown(markdownView, article.getContent());
        } else {
            Toast.makeText(this, "Artículo no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void onFailure(Exception e) {
        Toast.makeText(this, "Error al cargar article", Toast.LENGTH_SHORT).show();
    }
}