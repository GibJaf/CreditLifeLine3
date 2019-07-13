package com.example.creditlifeline3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    EditText usernameEditText , passwordEditText;
    private FirebaseAuth mAuth;
    ProgressBar signUpProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Log.d(TAG, "onCreate: started");


        Initialize();

        // SIGN UP USER
        findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });
    }

    private void Initialize(){
        Log.d(TAG, "Initialize: started");
        mAuth = FirebaseAuth.getInstance();
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpProgressbar = findViewById(R.id.signUpProgressBar);
    }

    private void RegisterUser(){
        Log.d(TAG, "RegisterUser: started");

        final String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if(username.isEmpty()) {
            usernameEditText.setError("Email/Username is required");
            usernameEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            usernameEditText.setError("Please enter valid email address");
            usernameEditText.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }
        if(password.length() < 6){
            passwordEditText.setError(" Minimum password length is 6");
            passwordEditText.requestFocus();
            return;
        }

        signUpProgressbar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(username,password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // Successful registration
                            finish(); //kill SignUpActivity and go to MainActivity , so that if back is pressed , app is closed
                            signUpProgressbar.setVisibility(View.GONE);
                            Log.d(TAG, " User registration for "+username+" => successful");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignUpActivity.this,user.getEmail()+" registered successfully :)",Toast.LENGTH_SHORT).show();
                            Intent goToMainActivityIntent = new Intent(SignUpActivity.this,MainActivity.class);
                            goToMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // not so important I guess cuz previous activities are being finish() anyways .
                            startActivity(goToMainActivityIntent);
                        } else {
                            // Failed registration
                            signUpProgressbar.setVisibility(View.GONE);
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, user.getEmail()+" registration status => "+task.getException());
                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                                Toast.makeText(SignUpActivity.this, user.getEmail()+" is already registered ",Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(SignUpActivity.this, "Unsuccessful registration => "+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
