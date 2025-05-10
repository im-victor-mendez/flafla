package com.example.flafla.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flafla.R;
import com.example.flafla.models.Article;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

/**
 * <h1>Home Article Adapter</h1>
 * <p>
 * Adaptador para el RecyclerView que muestra una lista de artículos en la pantalla de inicio.
 * <p>
 * Cada artículo se muestra con su título, una etiqueta (tag) y una imagen.
 */
public class HomeArticleAdapter extends RecyclerView.Adapter<HomeArticleAdapter.ArticleViewHolder> {
    private final List<Article> articles;
    private final Context context;

    /**
     * Constructor del adaptador.
     *
     * @param articles Lista de artículos que serán mostrados en el RecyclerView.
     * @param context  Contexto de la aplicación para acceder a recursos y cargar imágenes.
     */
    public HomeArticleAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    /**
     * Crea una nueva vista de artículo para mostrar en el RecyclerView.
     *
     * @param parent   Vista del RecyclerView en la que se insertará el nuevo item.
     * @param viewType Tipo de vista, aunque no se utiliza en este caso.
     * @return Un objeto ArticleViewHolder que contiene la vista inflada.
     */
    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del artículo
        View view = LayoutInflater.from(context).inflate(R.layout.item_article_home, parent, false);
        return new ArticleViewHolder(view);
    }

    /**
     * Vincula los datos del artículo con la vista correspondiente.
     *
     * @param holder   El ViewHolder que contiene la vista del artículo.
     * @param position La posición del artículo en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);

        // Asigna el título del artículo al TextView
        holder.title.setText(article.getTitle());

        // Limpia el contenedor de tags antes de agregar nuevos
        holder.tagsContainer.removeAllViews();

        // Si el artículo tiene tags, añadirlos al contenedor
        if (article.getTags() != null && !article.getTags().isEmpty()) {
            for (String tag : article.getTags()) {
                // Crear un nuevo TextView para cada tag
                TextView tagView = new TextView(context);
                tagView.setText(tag);
                tagView.setTextColor(ContextCompat.getColor(context, R.color.brown));
                tagView.setBackground(ContextCompat.getDrawable(context, R.drawable.tag)); // Drawable para el fondo del tag
                tagView.setTextSize(12);
                tagView.setPadding(24, 8, 24, 8); // Padding Horizontal y Vertical
                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 16, 16); // Márgenes entre los tags
                tagView.setLayoutParams(params);

                // Añadir el TextView al contenedor de tags
                holder.tagsContainer.addView(tagView);
            }
        }

        // Usar Glide para cargar la imagen del artículo de forma eficiente
        Glide.with(context)
                .load(article.getImage())
                .into(holder.image);

        // TODO: Implementar la navegación al hacer click en un artículo
        holder.itemView.setOnClickListener(v -> {
            // Intent intent = new Intent(context, ArticleDetailActivity.class);
            // intent.putExtra("article_id", article.getId());
            // context.startActivity(intent);
        });
    }

    /**
     * Retorna el número de artículos en la lista.
     *
     * @return El tamaño de la lista de artículos.
     */
    @Override
    public int getItemCount() {
        return articles.size();
    }

    /**
     * <h1>Article View Holder</h1>
     * <p>
     * ViewHolder que mantiene las vistas de cada artículo para optimizar el reciclaje de vistas.
     */
    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        ImageView image; // Imagen del artículo
        TextView title;  // Título del artículo
        FlexboxLayout tagsContainer; // Contenedor para los tags dinámicos

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            tagsContainer = itemView.findViewById(R.id.article_tags_container);
        }
    }
}
