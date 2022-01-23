package com.alitaghiyev.alitaghiyevfinal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.alitaghiyev.alitaghiyevfinal.R;
import com.alitaghiyev.alitaghiyevfinal.adapters.MessageAdapter;
import com.alitaghiyev.alitaghiyevfinal.models.Message;
import com.alitaghiyev.alitaghiyevfinal.models.User;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import at.markushi.ui.CircleButton;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = null; //veritabanı
    private FirebaseUser firebaseUser = null;
    private FirebaseDatabase firebaseDatabase = null;
    private DatabaseReference databaseReference = null;

    private ConstraintLayout constraintChatBack = null;
    private ImageView imageConstraintProfile = null;
    private EditText editConstraintMessage = null;
    private CircleButton circleChatSend = null;
    private TextView textConstraintMessageTitle = null;
    private RecyclerView recyclerChat = null;

    public static User user = null;
    private MessageAdapter messageAdapter = null;
    private ArrayList<Message> messages = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        visualObjectIds();
        userInformation();
        itemLayout();
        firebaseDatabase();
        setItems();

        constraintChatBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        circleChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mesajı eş zamanlı olarak kullanıcıya gönderir.
                send(view);

            }
        });

    }

    private void visualObjectIds() {
        constraintChatBack = findViewById(R.id.constraintChatBack);
        editConstraintMessage = findViewById(R.id.editConstraintMessage);
        circleChatSend = findViewById(R.id.circleChatSend);
        recyclerChat = findViewById(R.id.recyclerChat);
        imageConstraintProfile = findViewById(R.id.imageConstraintProfile);
        textConstraintMessageTitle = findViewById(R.id.textConstraintMessageTitle);
    }

    private void send(View view) {
        String message = editConstraintMessage.getText().toString().trim();

        if (message.length() != 0) {
            messages = new ArrayList<>();

            @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
            Message messageobj = new Message(message, firebaseUser.getUid(), user.getUid(), timeStamp);

            databaseReference.push().setValue(messageobj).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    messages.clear();
                    setItems();
                    editConstraintMessage.setText("");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(view, "Mesaj gönderilirken hata meydana geldi.", Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void firebaseDatabase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("SOHBETLER");
    }

    private void itemLayout() {
        recyclerChat.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        recyclerChat.setLayoutManager(linearLayoutManager);
    }

    private void setItems() {
        messages = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot d : snapshot.getChildren()) {
                    Message message = d.getValue(Message.class);
                    if (message.getKimden().equals(user.getUid()) && message.getKime().equals(firebaseUser.getUid())) {
                        messages.add(message);
                    }else if (message.getKimden().equals(firebaseUser.getUid()) && message.getKime().equals(user.getUid())) {
                        messages.add(message);
                    }
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void userInformation() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        if (user.getFotograf().length() != 0) {
            Picasso.get().load(user.getFotograf()).into(imageConstraintProfile);
        }

        textConstraintMessageTitle.setText(user.getAdsoyad());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setAdapter() {
        messageAdapter = new MessageAdapter(ChatActivity.this, messages);
        messageAdapter.notifyDataSetChanged();
        recyclerChat.setAdapter(messageAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChatActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}