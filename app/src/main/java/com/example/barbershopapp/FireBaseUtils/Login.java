package com.example.barbershopapp.FireBaseUtils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.barbershopapp.Activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login {

    private static Login instance;
    private final FirebaseAuth mAuth;

    public Login() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static synchronized Login getInstance() {
        if (instance == null) {
            instance = new Login();
        }
        return instance;
    }

    // Client login function
    public void login(String email, String password, Context context) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(context, "Successful login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
