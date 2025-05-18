package com.example.flafla.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flafla.R;
import com.example.flafla.models.Review;
import com.example.flafla.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView author, rating, content, date;
        ImageView avatar;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            author = itemView.findViewById(R.id.author);
            rating = itemView.findViewById(R.id.rating);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
        }

        @SuppressLint("DefaultLocale")
        public void bind(Review review) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users").document(review.getAuthor_id()).get().addOnSuccessListener(documentSnapshot -> {
                User user = documentSnapshot.toObject(User.class);

                if (user == null) return;

                Log.d("ReviewAdapter", "User: " + user.getName());

                author.setText(user.getName());
                Glide.with(avatar.getContext()).load(R.drawable.person).circleCrop().into(avatar);
                rating.setText(String.format("%d/5", review.getRating()));
                content.setText(review.getContent());
                date.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(review.getCreated_at()));
            });
        }
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(reviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
