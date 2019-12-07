package com.example.net_danong;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PeopleFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static List<ChatUserModel> userModels;
    private Context context;

    public PeopleFragmentRecyclerViewAdapter(Context context) {
        this.context = context;
        userModels = new ArrayList<>();
        final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("chatUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userModels.clear();

                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                    ChatUserModel userModel = snapshot.getValue(ChatUserModel.class);
                    if(userModel.uid.equals(myUid)){
                        continue;
                    }
                    userModels.add(userModel);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend,parent,false);


        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        Glide.with
                (holder.itemView.getContext())
                .load(userModels.get(position).profileImageUrl)
                .apply(new RequestOptions().circleCrop())
                .into(((CustomViewHolder)holder).imageView);
        ((CustomViewHolder)holder).textView.setText(userModels.get(position).userName);


        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), MessageActivity.class);
            intent.putExtra("destinationUid",userModels.get(position).uid);
            ActivityOptions activityOptions = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromright,R.anim.toleft);
                context.startActivity(intent,activityOptions.toBundle());
            }

        });
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.frienditem_imageview);
            textView = view.findViewById(R.id.frienditem_textview);
        }
    }
}