package com.alitaghiyev.alitaghiyevfinal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;
import com.alitaghiyev.alitaghiyevfinal.R;
import com.alitaghiyev.alitaghiyevfinal.adapters.ContactAdapter;
import com.alitaghiyev.alitaghiyevfinal.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import at.markushi.ui.CircleButton;



public class MainActivity extends AppCompatActivity {

    //veritabanı
    private FirebaseDatabase firebaseDatabase = null;
    private FirebaseAuth firebaseAuth = null; //giriş işlemi için
    private FirebaseUser firebaseUser = null;
    private DatabaseReference databaseReference = null;
    //gorsel nesneler
    private ImageView imageMainProfile, imageMainMore = null;
    private EditText editMainSearch = null;
    private ImageView imageMainMenu = null;
    private ConstraintLayout constraintMainNavigation, constraintMainProfile, constraintMainExit = null;
    private ImageView imageMainBack = null;
    private CircleButton circleShowFriends = null;
    private RecyclerView recyclerMain = null;
    private TextView text18, text21 = null;

    private String password = "";
    private String uid = "";

    private ContactAdapter contactAdapter = null;//RecyclerView adapter
    // recyclerviev  dinamik listeler . RecyclerView, büyük veri kümelerini verimli bir şekilde görüntülemeyi kolaylaştırır.
    private ArrayList<User> users = null; //veritabanındakı kullanıcıları almak için array

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        visualObjectIds();
        firebaseDatabase();
        itemLayout();
        userInformation();
        setContacts();
        visualObjectClickListeners();

        imageMainMore.setOnClickListener(new View.OnClickListener() { //setOnClickListener tıklandığında ne yapılacağını belirler
            @Override
            public void onClick(View view) {
                showAboutAppDialog();
            }
        });

        imageMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerMain.setVisibility(View.INVISIBLE);
                constraintMainNavigation.setVisibility(View.VISIBLE);
            }
        });

        imageMainBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerMain.setVisibility(View.VISIBLE);
                constraintMainNavigation.setVisibility(View.INVISIBLE);
            }
        });

        constraintMainProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerMain.setVisibility(View.VISIBLE);
                constraintMainNavigation.setVisibility(View.INVISIBLE);

                toProfilePage();

            }
        });

        editMainSearch.addTextChangedListener(new TextWatcher() { //kullanıcı tarafından girilen textlerin anlık olarak değişimini algılıyoruz arama yapmak için

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterList(editable.toString()); //filter list kullanıcı filtreleme
            }
        });

        constraintMainExit.setOnClickListener(new View.OnClickListener() { //cıkıs yapma işlemi uygulamadan ve firebase den cıkış işlemi yapıldı
            @Override
            public void onClick(View view) {

                if (firebaseUser != null) {
                    firebaseAuth.signOut();
                    Intent intent = new Intent(MainActivity.this, SplashActivity.class);//cıkısyapa tıklandıktan sonra ilk ekran olan splash
                    //ekranına yonlendirme yaptık zaten oradan tekrar giris ekranına yonlendirme yapılıyor
                    startActivity(intent);
                    finish();
                }else {
                    Snackbar.make(view, "Çıkış yapılamadı", Snackbar.LENGTH_LONG).show(); //uyarı
                }
            }
        });

    }

    private void visualObjectIds() { // xml dosyasındakı gorsel nesneler ile javadakı nesneleri bir biri ile ilişkilendirdik
        imageMainProfile = findViewById(R.id.imageMainProfile);
        imageMainMenu = findViewById(R.id.imageMainMenu);
        imageMainMore = findViewById(R.id.imageMainMore);
        imageMainBack = findViewById(R.id.imageMainBack);
        recyclerMain = findViewById(R.id.recyclerMain);
        constraintMainNavigation = findViewById(R.id.constraintMainNavigation);
        constraintMainProfile = findViewById(R.id.constraintMainProfile);
        constraintMainExit = findViewById(R.id.constraintMainExit);
        editMainSearch = findViewById(R.id.editMainSearch);
        text18 = findViewById(R.id.text18);
        text21 = findViewById(R.id.text21);
        circleShowFriends = findViewById(R.id.circleShowFriends);
    }

    private void firebaseDatabase() { //uygulama calıstığında veritabanını başlatdık sonra diğer işlemlere geçilicek
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("KULLANICILAR");
    }

    private void itemLayout() { //itemlerin dikey olarak kullanıcıya gösterilmesi vertical
        recyclerMain.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerMain.setLayoutManager(linearLayoutManager);
    }

    private void showAboutAppDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setMessage("Kullanıcıların birbirleriyle (peer-to-peer) mesajlaşarak iletişime geçtiği ve kendi profillerini oluşturabildiği kâr amacı gütmeyen bir uygulamadır.");
        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    private void userInformation() {//kullanıcının bilgilerini açılır menude gosterilmesi için cekildi. isim mail
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    User user = d.getValue(User.class); //veritabanından kullanıcının bilgilerini çekilmesi

                    if (user != null && user.getUid().equals(firebaseUser.getUid())) { //kullanıcının veritababnındakı id si kontrol edildi
                        uid = d.getKey();
                        password = user.getParola();
                        setUserInfo(user.getFotograf(), user.getAdsoyad(), user.getEmail());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUserInfo(String photo, String fullname, String email) { //kullanıcı bilgilerinin ekrana uygun kısımlara yazdırıldı
        if (Patterns.WEB_URL.matcher(photo).matches()) {
            Picasso.get().load(photo).into(imageMainProfile);
        }
        text18.setText(fullname); //text 18 e isim soy isim
        text21.setText(email); // text 21 e mail adresi yazdırıldı
    }

    private void setContacts() {
        users = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot d : snapshot.getChildren()) {
                    User user = d.getValue(User.class);
                    if (user != null  && !user.getEmail().equals(firebaseUser.getEmail())) {
                        users.add(user);
                    }
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void visualObjectClickListeners() {
        circleShowFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FriendshipActivity.class);// activityler arası gecisi ayarlar
                startActivity(intent);
                finish();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")

    private void filterList(String text) { //arama kısmına yazılan inputların filtrelenmesi
        ArrayList<User> userFilterArrayList = new ArrayList<>();

        for (User user : users
        ) {
            if (user.getAdsoyad().toLowerCase().replaceAll(" ", "").contains(text.toLowerCase().replaceAll(" ", ""))) {
                userFilterArrayList.add(user);
            }
        }
        contactAdapter.filterUser(userFilterArrayList);
    }


    private void setAdapter() { //item kontrol
        contactAdapter = new ContactAdapter(MainActivity.this, users);
        contactAdapter.notifyDataSetChanged();
        recyclerMain.setAdapter(contactAdapter); //contactAdapter recyclerMain e aktarıldı
    }



    private void toProfilePage() {
        Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
        startActivity(intent);//ana ekrandan profile geciş
        finish();
    }
}