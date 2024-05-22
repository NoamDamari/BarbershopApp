package com.example.barbershopapp.FireBaseUtils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.barbershopapp.Activities.ManagerMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ManagerLogin {
    private static ManagerLogin instance;
    private final FirebaseAuth mAuth;

    public ManagerLogin() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static synchronized ManagerLogin getInstance() {
        if (instance == null) {
            instance = new ManagerLogin();
        }
        return instance;
    }

    // Manager login function and call to checkManagerCredentials
    public void managerLogin(@NonNull String email, String password, String managerId, Context context) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Validate manager id
                            ManagerValidator.getInstance().validateManagerCredentials(email, password, managerId, new ManagerValidator.ManagerCredentialsListener() {
                                public void onManagerCredentialsMatch() {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(context, "Login Succeeded", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, ManagerMainActivity.class);
                                    intent.putExtra("manager id", managerId);
                                    context.startActivity(intent);
                                }

                                public void onManagerCredentialsMismatch(String errorMessage) {
                                    // If manager credentials don't match, show error message
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
