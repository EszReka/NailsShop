package com.example.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    FirebaseAuth uAuth;
    private static final int SECRET_KEY = 99;
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private SharedPreferences preferences;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();
        uAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.nameText);
        password = findViewById(R.id.passwordText);

        preferences = getSharedPreferences(PREF_KEY,MODE_PRIVATE);
    }

    public void login(View view) {
        uAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                Intent intent = new Intent(getApplicationContext(), MainPage.class);
                intent.putExtra("SECRET_KEY_REG", 100);
                startActivity(intent);
            }else{
                Toast.makeText(MainActivity.this, "A felhasználó beléptetése sikertelen volt." + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                Log.e(task.getException().getMessage()," ERROR");
            }
            }
        });


    }

    public void register(View view) {
        Intent intent = new Intent(this, Register.class);
        intent.putExtra("SECRET_KEY", 99);
        startActivity(intent);
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
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email",email.getText().toString());
        editor.putString("password",password.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}