package com.example.thirstyplant.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thirstyplant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateUser extends AppCompatActivity {
    private TextView toLogin;
    private EditText emailBox, passwordBox;
    private Button registerButton;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        firebaseAuth =firebaseAuth.getInstance();
        // References to controls on layout
        emailBox = findViewById(R.id.emailRegister);
        passwordBox = findViewById(R.id.passwordRegister);
        registerButton = findViewById(R.id.createAccountRegister);

        // Calls create Account method when button is pressed
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

        // Calls toLoginScreen method, when text is pressed
        toLogin = findViewById(R.id.toLogin);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLoginScreen();
            }
        });
    }

/**
 * Creates Account using firebaseAuth if all fields are met
 */
    private void createAccount(){
        String emailText = emailBox.getText().toString();
        String passwordText = passwordBox.getText().toString();
        if (emailText.isEmpty() && passwordText.isEmpty()){
            Toast.makeText(CreateUser.this, "Please enter an email and a password", Toast.LENGTH_LONG).show();
        }
        else if (emailText.isEmpty()){
            emailBox.setError("Please enter an email");
            emailBox.requestFocus();
        }
        else if (passwordText.isEmpty()){
            emailBox.setError("Please enter a password");
            passwordBox.requestFocus();
        }
        else if (!(emailText.isEmpty() && passwordText.isEmpty())){
            firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(CreateUser.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(CreateUser.this, "Failed to create account, Please try again", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent toHome = new Intent(CreateUser.this, Home.class);
                        startActivity(toHome);
                    }
                }
            });
        }
        else {
            Toast.makeText(CreateUser.this, "Error occurred, Please try again", Toast.LENGTH_LONG).show();
        }
    }

/**
 * Changes to login screen
 */

    private void toLoginScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}