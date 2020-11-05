package com.jvr.im;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {

    // Duración en milisegundos que se mostrará el splash
    private final int DURACION_SPLASH = 1500;
   // private String key = "dark";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
       final FirebaseAuth auth = FirebaseAuth.getInstance();
       final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable(){
            public void run(){
                obtenerinfo(auth, databaseReference);

            }
        }, DURACION_SPLASH);
    }

    private void obtenerinfo(FirebaseAuth auth, DatabaseReference databaseReference){
        if(auth.getCurrentUser()!=null){
            Intent main = new Intent(Splash.this, MainActivity.class);
            startActivity(main);
            finish();
        }else{
            Intent login = new Intent(Splash.this, loginscreen.class);
            startActivity(login);
            finish();
        }
    }
}