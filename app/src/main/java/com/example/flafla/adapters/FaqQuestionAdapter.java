package com.example.flafla.adapters;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flafla.R;
import com.example.flafla.models.FaqQuestion;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Adapter para mostrar preguntas expandibles que revelan su respuesta.
 */
public class FaqQuestionAdapter extends RecyclerView.Adapter<FaqQuestionAdapter.QuestionViewHolder> {

    private final List<FaqQuestion> questions;
    private final Set<Integer> expandedPositions = new HashSet<>();

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

        boolean isExpanded = expandedPositions.contains(position);

        // Cambia visibilidad con animación
        TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView);
        holder.answer.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        // Cambia el ícono de la flecha
        holder.arrow.setImageResource(isExpanded ? R.drawable.expand_more : R.drawable.expand_less);

        holder.itemView.setOnClickListener(v -> {
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
        return questions.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView question, answer;
        ImageView arrow;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.faq_question);
            answer = itemView.findViewById(R.id.faq_answer);
            arrow = itemView.findViewById(R.id.faq_arrow);
        }
    }

}
