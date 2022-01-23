package com.alitaghiyev.alitaghiyevfinal.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.alitaghiyev.alitaghiyevfinal.R;
import com.alitaghiyev.alitaghiyevfinal.models.User;

public class UserProfileActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = null;//firebase nesneleri
    private FirebaseUser firebaseUser = null;
    private FirebaseDatabase firebaseDatabase = null;
    private DatabaseReference databaseReference = null;
    private FirebaseStorage firebaseStorage = null;
    private StorageReference storageReference = null;


    //yine aynı şekilde xml ile java sınıfı  arasında bağlantıyı sağlamam için görsel nesneler
    private ImageView imageProfileBack, imageProfileAdd = null;
    private CardView cardProfileUpdate = null;
    private ConstraintLayout constraintProfileUpdate = null;
    private TextInputEditText inputEditTextProfileNameLastname = null;

    private ProgressDialog progressDialogUpdateProfile = null; //resim eklenirken yukleniyor bildirimi

    private boolean controller = false;
    private String userUid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseDatabase();
        visualObjectIds();
        visualObjectClickListeners();
    }

    private void visualObjectIds() {    //java ile xml arasında bağlantı
        imageProfileBack = findViewById(R.id.imageProfileBack);
        imageProfileAdd = findViewById(R.id.imageProfileAdd);
        inputEditTextProfileNameLastname = findViewById(R.id.inputEditTextProfileNameLastname);
        constraintProfileUpdate = findViewById(R.id.constraintProfileUpdate);
        cardProfileUpdate = findViewById(R.id.cardProfileUpdate);
    }

    private void firebaseDatabase() {     //veritabanı başlatma
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("KULLANICILAR");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("FOTOGRAFLAR").child(firebaseUser.getUid());
        myUser();
    }

    private void myUser() {//uygulamanın çökmesini engellemek için profil guncelenirken yapılan sorgu kullanıcı veritabanında varmı bunu kontrol ettik
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    User user = d.getValue(User.class);

                    if (user != null && user.getUid().equals(firebaseUser.getUid())) {
                        controller = true;
                        userUid = d.getKey();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void visualObjectClickListeners() {//tıklama metodları
        imageProfileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            } //geri sol ustteki ikon
        });

        imageProfileAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }  //resim secme
        });

        cardProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (controller) {
                    updateProfileInformation(inputEditTextProfileNameLastname.getText().toString().trim());
                }
            }
        });
    }

    private void choosePhoto() {  // listener içinde cağırıldı resim secmeye tıklandığında resimleri secmemiz için yeni sayfa açar
        if (controller) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Choose a picture"), 1);
        }else {
            Toast.makeText(UserProfileActivity.this, "Fotoğraf eklenirken hata meydana geldi.", Toast.LENGTH_LONG).show();
        }

    }

    private void updateProfileInformation(String namelastname) {
        progressDialogUpdateProfile = ProgressDialog.show(UserProfileActivity.this, "", "Bilgileriniz güncellenirken lütfen bekleyiniz", false);
        progressDialogUpdateProfile.setCancelable(false);

        databaseReference.child(userUid).child("adsoyad").setValue(namelastname).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialogUpdateProfile.dismiss();
                toSplashPage();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialogUpdateProfile.dismiss();
                Toast.makeText(UserProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateProfilePhoto(Uri img) {

        progressDialogUpdateProfile = ProgressDialog.show(UserProfileActivity.this, "", "Bilgileriniz güncellenirken lütfen bekleyiniz", false);
        progressDialogUpdateProfile.setCancelable(false);

        storageReference.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        databaseReference.child(userUid).child("fotograf").setValue(String.valueOf(uri)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialogUpdateProfile.dismiss();
                                toSplashPage();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialogUpdateProfile.dismiss();
                                Toast.makeText(UserProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialogUpdateProfile.dismiss();
                        Toast.makeText(UserProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialogUpdateProfile.dismiss();
                Toast.makeText(UserProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void toSplashPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(UserProfileActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {// resme tıklandığında resmi img urisine atar
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri img = data.getData();
            updateProfilePhoto(img);  //img updateProfilePhoto gonderilir
        }

    }

    @Override
    public void onBackPressed() { //profideyken geri cıka basıldığında mainactivitye gecildi
        super.onBackPressed();
        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}