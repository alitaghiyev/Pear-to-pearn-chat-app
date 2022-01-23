package com.alitaghiyev.alitaghiyevfinal.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.alitaghiyev.alitaghiyevfinal.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = null;
    private FirebaseUser firebaseUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseMethods(); //firebase start
        intentDelayer(); // delay geçikmeyi ayarlamak için

    }

    private void firebaseMethods() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    private void intentDelayer() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firebaseUser != null) { //kullanıcı giriş kontrolu
                    mainpage(); //kullanıcı varsa ana sayfaya giriş yapılır
                }else {
                    signinpage(); //kullanıcı yoksa da giriş yap ekranına yonlendirir
                }
            }
        }, 4000);//4 sn giris ekranı gecikmesi

    }


    private void signinpage() { //giriş yap metodu
        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
        startActivity(intent);//activityler arasında geçişi sağlar splash act den singing activity ekranına
        finish();
    }


    private void mainpage() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);//splash den main ekranına
        finish();
    }



}