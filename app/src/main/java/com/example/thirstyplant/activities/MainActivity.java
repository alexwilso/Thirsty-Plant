package com.example.thirstyplant.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thirstyplant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
/**
 * The MainActivity program implements an application that
 * simply displays "Hello World!" to the standard output.
 *
 * @author  Alex Wilson
 */
public class MainActivity extends AppCompatActivity {
    private Button createAccount;
    private EditText emailBox, passwordBox;
    private Button loginButton;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // References to controls on layout
        firebaseAuth = FirebaseAuth.getInstance();
        emailBox = findViewById(R.id.emailLogin);
        passwordBox = findViewById(R.id.passwordLogin);
        loginButton = findViewById(R.id.logOutButton);
        // Calls login method when log in button is pressed
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
        createAccount = findViewById(R.id.createAccountLogin);
        // Calls toCreateAccount method when Create Button is pressed
        createAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                toCreateAccount();
            }
        });
    }

/**
 * Uses FireBaseAuth to verify to log user in if all fields are met.
 */
    private void logIn(){
        String emailText = emailBox.getText().toString();
        String passwordText = passwordBox.getText().toString();
        if (emailText.isEmpty() && passwordText.isEmpty()){
            Toast.makeText(MainActivity.this, "Please enter an email and a password", Toast.LENGTH_LONG).show();
        }
        else if (emailText.isEmpty()){
            emailBox.setError("Please enter an email");
            emailBox.requestFocus();
        }
        else if (passwordText.isEmpty()){
            passwordBox.setError("Please enter a password");
            passwordBox.requestFocus();
        }
        else if (!(emailText.isEmpty() && passwordText.isEmpty())){
            firebaseAuth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Login failed, Please try again", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent toHome = new Intent(MainActivity.this, Home.class);
                        startActivity(toHome);
                    }
                }
            });
        }
        else{
            Toast.makeText(MainActivity.this, "Error occurred, Please try again", Toast.LENGTH_LONG).show();
        }
    }

/**
 * Takes user to create account activity
 */
    private void toCreateAccount(){
        Intent intent = new Intent(this, CreateUser.class);
        startActivity(intent);
    }
}