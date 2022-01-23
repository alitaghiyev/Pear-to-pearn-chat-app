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
import com.alitaghiyev.alitaghiyevfinal.R;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity { //javada oluturduğumuz nesneler
    private FirebaseAuth firebaseAuth = null;

    private ImageView imageViewSignInCancel = null;
    private TextView textViewSignInHaveAccount = null;
    private TextInputEditText inputEditTextSignInEmail, inputEditTextSignInPassword = null;
    private CardView cardViewSignIn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        visualObjectIds();//java ile xml dosyasındakı gorsel nesnelerin birleştirilmesi
        firebaseDatabase();
        visualObjectClickListeners();
    }

    private void visualObjectIds() { //java ile xml dosyasındakı gorsel nesnelerin birleştirdik bağladık
        imageViewSignInCancel = findViewById(R.id.imageViewSignInCancel);
        textViewSignInHaveAccount = findViewById(R.id.textViewSignInHaveAccount);
        cardViewSignIn = findViewById(R.id.cardViewSignIn);
        inputEditTextSignInEmail = findViewById(R.id.inputEditTextSignInEmail);
        inputEditTextSignInPassword = findViewById(R.id.inputEditTextSignInPassword);
    }

    private void firebaseDatabase() {
        firebaseAuth = FirebaseAuth.getInstance();
    } //veritabanını başlatdık


    private void visualObjectClickListeners() {//ekrandakı tıklanabilir nesnelere tıklandığında ne yapılacağını belittiğimiz metod
        imageViewSignInCancel.setOnClickListener(new View.OnClickListener() { //
            @Override
            public void onClick(View view) {
                onBackPressed();
            } //cıkıs yapma
        });



        textViewSignInHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signuppage();
            }     //kayıtola tıklandında singup ekranına yonlendime metodunu calıstırdık
        });



        cardViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEditTextSignInEmail.getText().toString().trim();
                String password = inputEditTextSignInPassword.getText().toString();

                if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length() >= 6) { //bilgiler doldurulduktan sonra giris yap butonuna tıklandığında
                    //gerekli input alanlarının edit textlerin kontrolunu yaptık mail adresi doğrumu şifrenin boyutu ne kadar gibi gibi
                    userController(view, email, password);
                }else {//eyer bilgiler yanlışsa uyarı bildirimi gosterdik
                    Snackbar.make(view, "Hatalı Giriş ", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void mainpage() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void userController(View view, String email, String password) { //kullanıcı uygulama giriş yaptıktan sonra bilgilerinin tutulamsı
        //yani uygulama kapatılsa bile tekrar girdiğimiz zaman bilgiler kayb olmuyor sadece cıkış yaptığında tekrar griş gerekli oluyor
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                mainpage();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(view, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void signuppage() { //yukarıda singup ekranına gecis için cağırılan metod
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class); //nereden nereye singin den singup ekranına gecis
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    } //listener içinde onBackPressed tıklandığında uygulama kapatıldı
}