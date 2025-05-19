package com.example.flafla.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.flafla.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Map;

public class ContactActivity extends AppCompatActivity implements OnMapReadyCallback {
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_contact), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(v -> finish());

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        loadData();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.app_name)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    @SuppressLint("SetTextI18n")
    private void loadData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("store").document("contact").get().addOnSuccessListener(documentSnapshot -> {

            Map<String, Object> openingHours = (Map<String, Object>) documentSnapshot.get("opening_hours");
            assert openingHours != null;
            String saturday = (String) openingHours.get("saturday");
            String sunday = (String) openingHours.get("sunday");
            String week = (String) openingHours.get("week");

            String phoneNumberText = documentSnapshot.getString("phone");
            String emailSupportText = documentSnapshot.getString("email_support");
            String addressText = documentSnapshot.getString("address");

            GeoPoint location = documentSnapshot.getGeoPoint("lat_lng");
            assert location != null;
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            latLng = new LatLng(latitude, longitude);

            TextView weekdaysHours = findViewById(R.id.weekdays_hours);
            TextView saturdayHours = findViewById(R.id.saturday_hours);
            TextView sundayHours = findViewById(R.id.sunday_hours);
            TextView phoneNumber = findViewById(R.id.phone_number);
            TextView emailSupport = findViewById(R.id.email_support);
            TextView address = findViewById(R.id.address);

            weekdaysHours.setText("Monday to Friday: " + week);
            saturdayHours.setText("Saturday: " + saturday);
            sundayHours.setText("Sunday: " + sunday);
            phoneNumber.setText(phoneNumberText);
            emailSupport.setText(emailSupportText);
            address.setText(addressText);

            phoneNumber.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumberText));
                startActivity(intent);
            });

            phoneNumber.setPaintFlags(phoneNumber.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            emailSupport.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + emailSupportText));
                startActivity(intent);
            });

            emailSupport.setPaintFlags(emailSupport.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            address.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:0,0?q=" + addressText));
                startActivity(intent);
            });

            address.setPaintFlags(address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        });
    }
}