package com.jvr.im;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class loginscreen extends AppCompatActivity {

    private FirebaseAuth auth;
    private static final int GOOGLE_SIGN = 123;
    protected GoogleSignInClient googleSignInClient;
    private CallbackManager mCallbackManager;
    private static final String TAG = "login";
    private FirebaseUser prevUser, currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .requestProfile()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
        setContentView(R.layout.activity_loginscreen);
        TextView btnlogin = findViewById(R.id.btnlogin);
        TextView btnsingin = findViewById(R.id.btnsingin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogLogin();
            }
        });
        btnsingin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogRegister();
            }
        });
    }

    public void showAlertDialogLogin() {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.login, null);
        builder.setView(customLayout);
        final EditText etemail = customLayout.findViewById(R.id.etemail);
        final EditText etpass = customLayout.findViewById(R.id.etpassword);
        CardView btnlog = customLayout.findViewById(R.id.btnlog);
        CardView btnloingoogle = customLayout.findViewById(R.id.btnlogingoogle);


        // create and show the alert dialog
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etemail.getText().toString(), password = etpass.getText().toString();
                if(!email.equals("")&&!password.equals("")){
                    loginUserEmail(email,password,dialog);
                }else{
                  //  Toast.makeText(getApplicationContext(), getString(R.string.str_checkdata), Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnloingoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInGoogle();
            }
        });
    }

    private void showAlertDialogRegister() {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.singin, null);
        builder.setView(customLayout);
        final EditText etemailreg = customLayout.findViewById(R.id.etemailreg);
        final EditText etpassreg = customLayout.findViewById(R.id.etpasswordreg);
        final EditText etname = customLayout.findViewById(R.id.etname);
        final EditText etpassregconfirm = customLayout.findViewById(R.id.etpasswordregconfirm);

        //blur();
        CardView btnreg = customLayout.findViewById(R.id.btnreg);
        CardView btnreggoogle = customLayout.findViewById(R.id.btnreggoogle);


        // create and show the alert dialog
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etname.getText().toString(), email = etemailreg.getText().toString(), password = etpassreg.getText().toString(), passconfirm = etpassregconfirm.getText().toString();
                if(!name.isEmpty()&&!email.isEmpty()&&!password.isEmpty()&&!passconfirm.isEmpty()){
                    if(password.length() >= 6){
                        if(password.equals(passconfirm)){
                            registerUserEmail(email,password, name,dialog);
                        }else{

                            Toast.makeText(loginscreen.this, getString(R.string.str_errorpass), Toast.LENGTH_SHORT).show();
                        }
                    }else{

                        Toast.makeText(loginscreen.this, getString(R.string.str_errorcaracteres), Toast.LENGTH_SHORT).show();
                    }
                }else{

                   Toast.makeText(loginscreen.this, getString(R.string.str_errordatos), Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnreggoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInGoogle();
            }
        });


    }

    //Metodo para el login sando un correo
    private void loginUserEmail(String email, String password, final Dialog dialog){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent main = new Intent(loginscreen.this, MainActivity.class);
                    dialog.dismiss();
                    startActivity(main);
                    finish();
                }else{
                  //  Toast.makeText(getApplicationContext(), getString(R.string.str_checkdata), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //metodo para registrarlo atraves de correo
    private void registerUserEmail(final String email, String pass, final String name, final Dialog dialog){
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    dialog.dismiss();
                    Intent main = new Intent(loginscreen.this, register.class);
                    startActivity(main);
                    finish();

                }else{
                  //  Toast.makeText(loginscreen.this, getString(R.string.str_errorreg), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void SignInGoogle(){
        Intent SignIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(SignIntent,GOOGLE_SIGN);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_SIGN){
            Task<GoogleSignInAccount> task = GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null ) firebaseAuthWithGoogle(account);

            }catch (ApiException e){


                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider
                .getCredential(account.getIdToken(),null);
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent i = new Intent(loginscreen.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(loginscreen.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




}