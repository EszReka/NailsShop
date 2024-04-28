package com.example.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private static final String LOG_TAG = Register.class.getName();
    private static final int SECRET_KEY_REG = 100;
    private FirebaseFirestore firestore;
    private FirebaseAuth uAuth;
    EditText email;
    EditText userName;
    EditText password;
    EditText passwordConf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bundle bundle = getIntent().getExtras();
        int secret_key = bundle.getInt("SECRET_KEY",0);

        firestore = FirebaseFirestore.getInstance();

        if(secret_key != 99){
            finish();
        }

        userName = findViewById(R.id.usernameReg);
        email = findViewById(R.id.emailReg);
        password = findViewById(R.id.passwordReg);
        passwordConf = findViewById(R.id.passwordConfirm);

        uAuth = FirebaseAuth.getInstance();

    }

    public void sendRegistry(View view) {
        if(password.equals(passwordConf)){
            //show error
        }
        uAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(), MainPage.class);
                    intent.putExtra("SECRET_KEY_REG", 100);
                    startActivity(intent);
                } else {
                    Toast.makeText(Register.this, "A felhasználó készítése sikertelen volt." + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(task.getException().getMessage()," ERROR");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}