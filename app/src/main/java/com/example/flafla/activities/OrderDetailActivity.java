package com.example.flafla.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flafla.R;
import com.example.flafla.adapters.OrderItemAdapter;
import com.example.flafla.models.Order;
import com.example.flafla.models.OrderItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class OrderDetailActivity extends BaseActivity {
    public static final String ORDER = "ORDER";
    private TextView id, subtotal, shipping, total;
    private RecyclerView orderItems;
    private Order currentOrder;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_order_detail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupToolbar();

        TextView back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        id = findViewById(R.id.order_id);
        subtotal = findViewById(R.id.tvSubtotal);
        shipping = findViewById(R.id.tvShipping);
        total = findViewById(R.id.tvTotal);

        TextView owner = findViewById(R.id.owner);
        owner.setText("Hi, " + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());

        orderItems = findViewById(R.id.rvOrderItems);
        orderItems.setLayoutManager(new LinearLayoutManager(this));

        TextView tvFeedback = findViewById(R.id.feedback);
        tvFeedback.setText(Html.fromHtml(
                "Tell us about your shopping experience and receive <b>15% discount</b> on your next purchase!"
        ));

        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String orderId = getIntent().getStringExtra(ORDER);

        loadOrderDetails(userId, orderId);
    }

    private void loadOrderDetails(String userId, String orderId) {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("orders")
                .document(orderId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    currentOrder = snapshot.toObject(Order.class);
                    displayOrderDetails();
                });
    }

    @SuppressLint("SetTextI18n")
    private void displayOrderDetails() {
        if (currentOrder == null) return;

        TextView status = findViewById(R.id.status);
        switch (currentOrder.getStatus()) {
            case PENDING:
                status.setText(getString(R.string.pending_instructions));
                break;
            case SHIPPED:
                status.setText(getString(R.string.shipped_instructions));
                break;
            case DELIVERED:
                status.setText(getString(R.string.delivered_instructions));
                break;
        }

        double subtotal = 0.0;
        double shipping = currentOrder.getShipping();

        String start = currentOrder.getId().substring(0, 3);
        String end = currentOrder.getId().substring(currentOrder.getId().length() - 3);
        String id = start + end;
        this.id.setText(id.toUpperCase());

        for (OrderItem item : currentOrder.getItems()) {
            subtotal += item.getPrice() * item.getQuantity();
        }

        this.subtotal.setText("$" + subtotal);
        this.shipping.setText("$" + shipping);
        total.setText("$" + (subtotal + shipping));

        OrderItemAdapter adapter = new OrderItemAdapter(currentOrder.getItems());
        orderItems.setAdapter(adapter);
    }

}