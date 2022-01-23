package com.alitaghiyev.alitaghiyevfinal.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alitaghiyev.alitaghiyevfinal.R;
import com.alitaghiyev.alitaghiyevfinal.models.Friend;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.CardViewDesignThingsHolder>{
    private Context context;
    private ArrayList<Friend> friendArrayList = new ArrayList<>();

    private FirebaseAuth firebaseAuth = null;
    private FirebaseUser firebaseUser = null;
    private FirebaseDatabase firebaseDatabase = null;
    private DatabaseReference databaseReference = null;

    public FriendAdapter(Context context, ArrayList<Friend> friendArrayList) {
        this.context = context;
        this.friendArrayList = friendArrayList;
    }

    @NonNull
    @Override
    public CardViewDesignThingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardfriend, parent, false);
        return new CardViewDesignThingsHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull CardViewDesignThingsHolder holder, int position) {
        Friend friend = friendArrayList.get(position);

        if (friend.getController().equals("true")) {
            holder.ivAddFriend.setImageDrawable(context.getDrawable(R.drawable.check));
        }

        holder.tvAddFriend.setText(friend.getFrom());

        holder.clAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.ivAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (friend.getController().equals("false")) {
                    initializeFirebase(friend);
                }else {
                    Toast.makeText(context, friend.getFrom()+" kişisi arkadaşınız olarak ekli", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return friendArrayList.size();
    }

    public static class CardViewDesignThingsHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout clAddFriend = null;
        private TextView tvAddFriend = null;
        private ImageView ivAddFriend = null;

        public CardViewDesignThingsHolder(@NonNull View itemView) {
            super(itemView);
            clAddFriend = itemView.findViewById(R.id.clAddFriend);
            tvAddFriend = itemView.findViewById(R.id.tvAddFriend);
            ivAddFriend = itemView.findViewById(R.id.ivAddFriend);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initializeFirebase(Friend friend) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("FRIENDSHIP");

        databaseReference.child(friend.getKey()).child("controller").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(Void unused) {
                notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}
