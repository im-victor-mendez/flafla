package com.example.flafla.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flafla.R;
import com.example.flafla.models.FaqCategory;

import java.util.List;

/**
 * <h1>Faq Category Adapter</h1>
 * <p>
 * Adapter for displaying FAQ categories, each containing a list of questions.
 * <p>
 * This adapter manages a nested RecyclerView for showing questions per category.
 */
public class FaqCategoryAdapter extends RecyclerView.Adapter<FaqCategoryAdapter.CategoryViewHolder> {

    private final List<FaqCategory> faqCategories;

    /**
     * Constructor to initialize the adapter with a list of FAQ categories.
     *
     * @param faqCategories List of FaqCategory objects.
     */
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
        holder.categoryTitle.setText(category.getCategory());

        FaqQuestionAdapter questionAdapter = new FaqQuestionAdapter(category.getQuestions());
        holder.questionsRecycler.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.questionsRecycler.setAdapter(questionAdapter);
    }

    @Override
    public int getItemCount() {
        return faqCategories.size();
    }

    /**
     * <h1>Category ViewHolder</h1>
     * ViewHolder for displaying the category title and a list of associated questions.
     */
    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;
        RecyclerView questionsRecycler;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.faq_category);
            questionsRecycler = itemView.findViewById(R.id.faq_questions_recycler);
        }
    }
}
