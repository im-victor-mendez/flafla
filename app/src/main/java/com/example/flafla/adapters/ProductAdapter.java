package com.example.flafla.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    private List<Product> productList;
    private Context context;

    /**
     * Constructor del adapter.
     *
     * @param context     Contexto de la aplicación o actividad.
     * @param productList Lista de productos a mostrar.
     */
    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    /**
     * <h1>On Create View Holder</h1>
     * <p>
     * Crea nuevas instancias de ProductViewHolder inflando el layout del item.
     *
     * @param parent   ViewGroup padre donde se añadirá el nuevo View.
     * @param viewType Tipo de vista (no usado aquí ya que es un solo tipo de item).
     * @return Una nueva instancia de ProductViewHolder.
     */
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    /**
     * <h1>On Bind View Holder</h1>
     * <p>
     * Vincula los datos de un producto al ViewHolder.
     *
     * @param holder   ViewHolder que debe actualizarse.
     * @param position Posición del producto en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.name.setText(product.getName());
        String priceFormatted = String.format("$%.2f", product.getPrice());
        holder.price.setText(priceFormatted);

        // TODO: Cargar imagen usando Glide
        // Glide.with(context).load(product.getImages().get(0)).into(holder.image);

        // Maneja el click en un producto para abrir su detalle
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra(context.getString(R.string.product_id), product.getId());
            context.startActivity(intent);
        });
    }

    /**
     * Retorna el número total de productos en la lista.
     *
     * @return Número de productos.
     */
    @Override
    public int getItemCount() {
        return productList.size();
    }

    /**
     * <h1>Product View Holder</h1>
     * <p>
     * ViewHolder que representa cada item del RecyclerView.
     */
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, price;

        /**
         * Constructor del ViewHolder. Inicializa las vistas de cada producto.
         *
         * @param v Vista del item_product.xml inflada.
         */
        public ProductViewHolder(@NonNull View v) {
            super(v);
            image = v.findViewById(R.id.image_product);
            name = v.findViewById(R.id.text_product_name);
            price = v.findViewById(R.id.text_product_price);
        }
    }
}
