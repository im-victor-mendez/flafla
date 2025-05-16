package com.example.flafla.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flafla.R;
import com.example.flafla.models.FaqCategory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Adapter para mostrar categorías de FAQ expandibles que revelan preguntas al hacer clic.
 */
public class FaqCategoryAdapter extends RecyclerView.Adapter<FaqCategoryAdapter.CategoryViewHolder> {

    private final List<FaqCategory> faqCategories;
    private final Set<Integer> expandedPositions = new HashSet<>(); // Control de expansión

    public FaqCategoryAdapter(List<FaqCategory> faqCategories) {
        this.faqCategories = faqCategories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_faq_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        FaqCategory category = faqCategories.get(position);
        boolean isExpanded = expandedPositions.contains(position);

        holder.categoryTitle.setText(category.getCategory());

        FaqQuestionAdapter questionAdapter = new FaqQuestionAdapter(category.getQuestions());
        holder.questionsRecycler.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.questionsRecycler.setAdapter(questionAdapter);

        // Agrega el divisor entre preguntas
        if (holder.questionsRecycler.getItemDecorationCount() == 0) {
            DividerItemDecoration questionDivider = new DividerItemDecoration(holder.itemView.getContext(), DividerItemDecoration.VERTICAL);
            holder.questionsRecycler.addItemDecoration(questionDivider);
        }


        // Mostrar u ocultar preguntas
        holder.questionsRecycler.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.toggleIcon.setImageResource(isExpanded ? R.drawable.expand_less : R.drawable.expand_more);

        // Toggle al hacer clic en el header
        holder.headerLayout.setOnClickListener(v -> {
            if (expandedPositions.contains(position)) {
                expandedPositions.remove(position);
            } else {
                expandedPositions.add(position);
            }
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return faqCategories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;
        RecyclerView questionsRecycler;
        ImageView toggleIcon;
        View headerLayout;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.faq_category);
            questionsRecycler = itemView.findViewById(R.id.faq_questions_recycler);
            toggleIcon = itemView.findViewById(R.id.faq_toggle_icon);
            headerLayout = itemView.findViewById(R.id.header);
        }
    }
}
