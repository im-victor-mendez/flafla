package com.example.flafla.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flafla.R;
import com.example.flafla.activities.ProductDetailActivity;
import com.example.flafla.models.Product;

import java.util.List;

/**
 * <h1>Product Adapter</h1>
 * <p>
 * Adapter para mostrar una lista de productos en un RecyclerView.
 * <p>
 * Cada producto muestra su imagen, nombre y precio, y permite navegar a su detalle al ser presionado.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final List<Product> productList;
    private final Context context;
    private final int itemLayoutRes; // Nuevo: recurso de layout

    /**
     * Constructor del adapter.
     *
     * @param context       Contexto de la aplicación o actividad.
     * @param productList   Lista de productos a mostrar.
     * @param itemLayoutRes Recurso XML del layout a usar para cada item.
     */
    public ProductAdapter(Context context, List<Product> productList, @LayoutRes int itemLayoutRes) {
        this.context = context;
        this.productList = productList;
        this.itemLayoutRes = itemLayoutRes;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(itemLayoutRes, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.name.setText(product.getName());
        String priceFormatted = String.format("$%.2f", product.getPrice());
        holder.price.setText(priceFormatted);

        String image = product.getImages().get(0);
        Glide.with(context).load(image).into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT, product.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, price;

        public ProductViewHolder(@NonNull View v) {
            super(v);
            image = v.findViewById(R.id.image_product);
            name = v.findViewById(R.id.text_product_name);
            price = v.findViewById(R.id.text_product_price);
        }
    }
}
