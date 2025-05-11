package com.example.flafla.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flafla.R;
import com.example.flafla.adapters.HomeArticleAdapter;
import com.example.flafla.models.Article;
import com.example.flafla.models.HomePageContent;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Home Activity</h1>
 * <p>
 * Muestra una lista de artículos destacados (home) usando un RecyclerView.
 * <p>
 * Los artículos a mostrar se definen en el documento "homepage" de la colección "home",
 * el cual contiene una lista de IDs que se usan para recuperar los artículos desde la colección "articles".
 */
public class HomeActivity extends BaseActivity {

    private HomeArticleAdapter adapter;
    private final List<Article> articleList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        View rootView = findViewById(R.id.activity_home);
        View banner = findViewById(R.id.banner);
        View toolbar = findViewById(R.id.toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Establece el padding para evitar que el contenido se meta bajo las barras
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            // Asegura que el cálculo del alto del toolbar se haga después de que fue medido
            banner.post(() -> {
                int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
                int toolbarHeight = toolbar.getHeight();
                int availableHeight = screenHeight - systemBars.top - systemBars.bottom - toolbarHeight;

                ViewGroup.LayoutParams params = banner.getLayoutParams();
                params.height = availableHeight;
                banner.setLayoutParams(params);
            });

            return insets;
        });

        // Configurar el toolbar
        setupToolbar();

        // Inicializar el RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HomeArticleAdapter(articleList, this);
        recyclerView.setAdapter(adapter);

        // Inicializar instancia de Firestore
        db = FirebaseFirestore.getInstance();

        // Cargar artículos destacados desde la base de datos
        loadHomeArticles();
    }

    /**
     * <h1>Load Home Articles</h1>
     * <p>
     * Obtiene el documento "homepage" de la colección "home",
     * el cual contiene los IDs de los artículos que se deben mostrar en la pantalla principal.
     */
    private void loadHomeArticles() {
        db.collection("home").document("homepage").get()
                .addOnSuccessListener(this::onSuccess)
                .addOnFailureListener(e -> Log.e("HomeActivity", "Failed to fetch homepage data", e));
    }


    /**
     * <h1>On Success</h1>
     * <p>
     * Callback exitoso al obtener el documento "homepage".
     * <p>
     * Extrae los IDs de artículos y los consulta individualmente.
     *
     * @param documentSnapshot Documento "homepage" de la colección "home"
     */
    private void onSuccess(DocumentSnapshot documentSnapshot) {
        // Extraer IDs de artículos y consultar individualmente
        if (documentSnapshot.exists()) {
            HomePageContent data = documentSnapshot.toObject(HomePageContent.class);

            assert data != null;

            // Obtener la lista de IDs de artículos
            List<String> ids = data.getFeatured_articles();

            ((TextView) findViewById(R.id.banner_title)).setText(data.getBanner_title());
            Glide.with(this).load(data.getBanner_image()).into((ImageView) findViewById(R.id.banner_image));


            // Iterar sobre cada ID y consultar el artículo correspondiente
            for (String id : ids) {
                // Consultar el artículo individualmente
                db.collection("articles").document(id).get()
                        .addOnSuccessListener(this::addArticleFromSnapshot)
                        .addOnFailureListener(e -> Log.e("HomeActivity", "Error loading article " + id, e));
            }
        }
    }

    /**
     * <h1>Add Article From Snapshot</h1>
     * <p>
     * Convierte un documento Firestore en un objeto Article y lo agrega al RecyclerView.
     *
     * @param doc Documento Firestore del artículo
     */
    private void addArticleFromSnapshot(@NonNull DocumentSnapshot doc) {
        Article article = doc.toObject(Article.class);
        if (article != null) {
            articleList.add(article);
            adapter.notifyItemInserted(articleList.size() - 1);
        }
    }
}
