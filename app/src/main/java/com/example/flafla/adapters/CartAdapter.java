package com.example.flafla.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flafla.R;
import com.example.flafla.database.CartDatabaseHelper;
import com.example.flafla.models.CartItem;
import com.example.flafla.models.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private CartDatabaseHelper dbHelper;
    private Runnable refreshCallback;

    public CartAdapter(List<CartItem> cartItems, CartDatabaseHelper dbHelper, Runnable refreshCallback) {
        this.cartItems = cartItems;
        this.dbHelper = dbHelper;
        this.refreshCallback = refreshCallback;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.quantity.setText(String.valueOf(item.getQuantity()));

        FirebaseFirestore.getInstance().collection("products").document(item.getProductId()).get().addOnSuccessListener(documentSnapshot -> {
            Product product = documentSnapshot.toObject(Product.class);

            holder.name.setText(product.getName());
            holder.price.setText(String.format("%.2f", product.getPrice()));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                Glide.with(holder.itemView.getContext()).load(product.getImages().getFirst()).into(holder.image);
            }
        });

        holder.add.setOnClickListener(v -> {
            dbHelper.addItem(item.getProductId());
            holder.quantity.setText(String.valueOf(dbHelper.getQuantityForProduct(item.getProductId())));
        });

        holder.subtract.setOnClickListener(v -> {
            dbHelper.removeItem(item.getProductId());
            holder.quantity.setText(String.valueOf(dbHelper.getQuantityForProduct(item.getProductId())));
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, quantity, price, add, subtract;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            add = itemView.findViewById(R.id.add);
            subtract = itemView.findViewById(R.id.subtract);
        }
    }
}
