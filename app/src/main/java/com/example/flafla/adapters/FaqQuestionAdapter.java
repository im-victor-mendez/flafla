package com.example.flafla.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flafla.R;
import com.example.flafla.models.FaqQuestion;

import java.util.List;

/**
 * <h1>Faq Question Adapter</h1>
 * <p>
 * Adapter for displaying a list of questions and answers inside each FAQ category.
 * <p>
 * Used within {@link FaqCategoryAdapter} to populate inner RecyclerViews.
 */
public class FaqQuestionAdapter extends RecyclerView.Adapter<FaqQuestionAdapter.QuestionViewHolder> {

    private final List<FaqQuestion> questions;

    /**
     * Constructor to initialize the adapter with a list of questions.
     *
     * @param questions List of FaqQuestion objects to display.
     */
    public FaqQuestionAdapter(List<FaqQuestion> questions) {
        this.questions = questions;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_faq_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        FaqQuestion question = questions.get(position);
        holder.question.setText(question.getQuestion());
        holder.answer.setText(question.getAnswer());
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    /**
     * <h1>Question ViewHolder</h1>
     * ViewHolder for displaying a single FAQ question and its answer.
     */
    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView question, answer;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.faq_question);
            answer = itemView.findViewById(R.id.faq_answer);
        }
    }
}
