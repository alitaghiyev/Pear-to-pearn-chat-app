package com.alitaghiyev.alitaghiyevfinal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.alitaghiyev.alitaghiyevfinal.R;
import java.util.HashMap;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = null; //veritabanı nesnelerini oluşturduk
    private FirebaseDatabase firebaseDatabase = null;
    private DatabaseReference databaseReference = null;

    private ImageView imageViewSignupCancel = null;//yine aynı şeklde java içinde gorsel nesneler olsurduk sonra bunlarla xml dosyasını birleştireceğiz
    private TextView textViewSignInAlready = null;
    private CardView cardViewSignup = null;
    private TextInputEditText inputEditTextSignUpFullName, inputEditTextSignUpEmail, inputEditTextSignUpPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        visualObjectIds();
        firebaseDatabase();
        visualObjectClickListeners();
    }

    private void visualObjectIds() {
        imageViewSignupCancel = findViewById(R.id.imageViewSignupCancel);
        textViewSignInAlready = findViewById(R.id.textViewSignInAlready);
        cardViewSignup = findViewById(R.id.cardViewSignUp);
        inputEditTextSignUpFullName = findViewById(R.id.inputEditTextSignUpFullName);
        inputEditTextSignUpEmail = findViewById(R.id.inputEditTextSignUpEmail);
        inputEditTextSignUpPassword = findViewById(R.id.inputEditTextSignUpPassword);
    }

    private void firebaseDatabase() { //yukarıda oluşturduğumuz veritabanı nesnelerini firebase ile bağladık
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("KULLANICILAR");
    }

    private void visualObjectClickListeners() { //sing up ekranında bulunan tıklanabilir gorsel nesnelere tıklandığında ne yapılacağını belirledik
        imageViewSignupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }//geri cık butonu ok işareti <--
        });
        textViewSignInAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signinpage();
            }//zaten hesabım var kısmında giriş yap ekranına gidiş için
        });
        cardViewSignup.setOnClickListener(new View.OnClickListener() {  //kayıt ol card view
            @Override
            public void onClick(View view) { //kayıt ola tıklandığında texviewlerden alınan veriler sırasıyla 3 adet değişkene atandı
                String namelastname = inputEditTextSignUpFullName.getText().toString().trim();
                String email = inputEditTextSignUpEmail.getText().toString().trim();
                String password = inputEditTextSignUpPassword.getText().toString();

                if (namelastname.length() >= 5 && Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length() >= 6) {
                    //burada ad soyad email, şifre uzunluğu gibi kontrolleri yaptık

                    //parametleri yolladık
                    createUser(view, namelastname, email, password);//eyer bilgiler doğruysa sağlanıyorsa yeni kullanıcı oluşturma işlemini gercekleştrdik
                    //ctrl click
                }else {
                    Snackbar.make(view, "Hatalı kayıt işlemi", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createUser(View view, String namelastname, String email, String password) { //kayıt zamanı gelen bilgilere gore firebase işlemlerini yaptık

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                HashMap<String, String> userMap = new HashMap<>();

                userMap.put("adsoyad", namelastname);
                userMap.put("fotograf", "https://i.imgur.com/SA7hZp5_d.webp?maxwidth=760&fidelity=grand");
                userMap.put("email", email);
                userMap.put("parola", password);
                userMap.put("uid", Objects.requireNonNull(authResult.getUser()).getUid());

                databaseReference.push().setValue(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        mainpage();               //firebase işlemleri tamamlandıktan sonra buradan ana sayfaya geçiş metodu çağırıldı
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(view, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(view, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void mainpage() {// ana sayfaya geciş işlemi
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void signinpage() {//visualObjectClickListeners kısmında çağılırıldı
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {//visualObjectClickListeners kısmında çağıırldı
        finish();
    }
}