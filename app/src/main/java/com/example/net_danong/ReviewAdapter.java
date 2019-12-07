package com.example.net_danong;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ReviewAdapter extends FirestoreAdapter<ReviewAdapter.ViewHolder> {

    public ReviewAdapter(Query query) {
        super(query);
    }
    static  View view;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_review, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position).toObject(Review.class));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView idView;
        TextView contents;

        MaterialRatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_userProfile);
            idView = itemView.findViewById(R.id.txt_userId);
            contents = itemView.findViewById(R.id.txt_Contents);
            ratingBar = itemView.findViewById(R.id.rating_pdtRating);
        }

        public void bind(Review review) {
/*            DocumentReference userDocRef = FirebaseFirestore.getInstance().collection("users").document(review.getUserUId());
            userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User user = document.toObject(User.class);
                            Glide.with(view.getContext())
                                    .load(review.getRvPhoto())
                                    .into(imageView);
                            idView.setText(user.getEmail().substring(0,user.getEmail().lastIndexOf("@")));
                        }
                    }
                }
            });*/
            Glide.with(view.getContext())
                    .load(review.getRvPhoto())
                    .into(imageView);
            idView.setText(review.getUserName());
            contents.setText(review.getText());
            ratingBar.setRating((float) review.getRating());

        }
    }

}
