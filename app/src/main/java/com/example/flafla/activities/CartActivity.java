package com.example.flafla.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flafla.R;
import com.example.flafla.adapters.CartAdapter;
import com.example.flafla.database.CartDatabaseHelper;
import com.example.flafla.models.CartItem;

import java.util.List;

public class CartActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private CartDatabaseHelper dbHelper;
    private Button btnClearCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_cart), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupToolbar();

        recyclerView = findViewById(R.id.cartRecyclerView);
        btnClearCart = findViewById(R.id.btnClearCart);
        dbHelper = new CartDatabaseHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadCartItems();

        btnClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.clearCart();
                loadCartItems();
            }
        });
    }


    private void loadCartItems() {
        List<CartItem> items = dbHelper.getAllItems();
        adapter = new CartAdapter(items, dbHelper, this::loadCartItems);
        recyclerView.setAdapter(adapter);
    }
}