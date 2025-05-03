package com.example.flafla.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.flafla.R;

public class MenuFragment extends Fragment {

    public MenuFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // TODO: Add elements to fragment menu
        setupMenuItem(view, R.id.menu_about, "About");
        setupMenuItem(view, R.id.menu_shop, "Shop");
        setupMenuItem(view, R.id.menu_people, "People");
        setupMenuItem(view, R.id.menu_blog, "Blog");
        setupMenuItem(view, R.id.menu_contact, "Contact");
        setupMenuItem(view, R.id.menu_faq, "FAQ");
        setupMenuItem(view, R.id.menu_catalogue, "Catalogue");

        return view;
    }

    private void setupMenuItem(View view, int id, String section) {
        TextView item = view.findViewById(id);
        item.setOnClickListener(v -> {
            // TODO: Implement navigation to the respective section
        });
    }
}
