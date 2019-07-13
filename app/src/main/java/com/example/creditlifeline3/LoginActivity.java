package com.example.creditlifeline3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    EditText usernameEditText , passwordEditText;
    Button loginButton , goToSignUpButton;
    ProgressBar loginProgressBar;
    private FirebaseAuth mAuth;
    String username , password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: started");

        Initialize();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

      goToSignUpButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              finish();
              startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
          }
      });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: started");
        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
    }

    private void Initialize(){
        Log.d(TAG, "Initialize: started");

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        goToSignUpButton = findViewById(R.id.goToSignUpButton);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        mAuth = FirebaseAuth.getInstance();
    }

    private void loginUser(){
        Log.d(TAG, "loginUser: started");


    }
}
