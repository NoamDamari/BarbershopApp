package com.example.barbershopapp.FireBaseUtils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.barbershopapp.Activities.MainActivity;
import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.Models.Client;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration {
    private static Registration instance;
    private final FirebaseAuth mAuth;

    public Registration() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static synchronized Registration getInstance() {
        if (instance == null) {
            instance = new Registration();
        }
        return instance;
    }

    // Register the client and call to saveClientDetailsOnDatabase function
    public void register(Client client, Context context) {

        String email = client.getEmail();
        String password = client.getPassword();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(context, "Successful Registration", Toast.LENGTH_SHORT).show();
                            Client clientWithUid = new Client(client.getUsername(), client.getEmail(), client.getPassword(), client.getPhone(), mAuth.getUid());
                            FirebaseManager.getInstance().saveClientDetailsOnDatabase(clientWithUid);
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
