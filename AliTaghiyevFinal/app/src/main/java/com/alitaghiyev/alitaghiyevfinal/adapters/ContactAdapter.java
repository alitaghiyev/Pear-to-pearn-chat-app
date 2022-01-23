package com.alitaghiyev.alitaghiyevfinal.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.alitaghiyev.alitaghiyevfinal.activities.ChatActivity;
import com.alitaghiyev.alitaghiyevfinal.R;
import com.alitaghiyev.alitaghiyevfinal.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.CardViewDesignThingsHolder> {
    private Context context;
    private ArrayList<User> userArrayList = new ArrayList<>(); //veritabanındakı kullanıcıların listesi
    //bu listedeki elemanlar veriler cardcontact.xml dosyasındakı tasarıma uygun olarak ekranda gosterilir

    private FirebaseAuth firebaseAuth = null;
    private FirebaseUser firebaseUser = null;
    private FirebaseDatabase firebaseDatabase = null;
    private DatabaseReference databaseReference = null;

    public ContactAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public CardViewDesignThingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardcontact, parent, false);
        return new CardViewDesignThingsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewDesignThingsHolder holder, int position) {
        User user = userArrayList.get(position);

        holder.cvContactNameLastname.setText(user.getAdsoyad());

        holder.clCvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMessagePage(user);
            }
        });

        holder.cvContactAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAddFriend(user);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    private void toMessagePage(User user) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("user", user);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static class CardViewDesignThingsHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout clCvContact = null;
        private ImageView cvContactAddFriend = null;
        private TextView cvContactNameLastname = null;

        public CardViewDesignThingsHolder(@NonNull View itemView) {
            super(itemView);
            clCvContact = itemView.findViewById(R.id.clCvContact);
            cvContactAddFriend = itemView.findViewById(R.id.cvContactAddFriend);
            cvContactNameLastname = itemView.findViewById(R.id.cvContactNameLastname);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterUser(ArrayList<User> userFilterArrayList) {
        userArrayList = userFilterArrayList;
        notifyDataSetChanged();
    }

    private void sendAddFriend(User user) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("FRIENDSHIP");

        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("from", firebaseUser.getEmail());
        userMap.put("to", user.getEmail());
        userMap.put("controller", "false");

        databaseReference.push().setValue(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Arkadaşlık isteği başarıyla gönderildi", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
