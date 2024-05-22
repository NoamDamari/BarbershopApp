package com.example.barbershopapp.FireBaseUtils;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManagerValidator {

    private static ManagerValidator instance;
    private final DatabaseReference mDatabase;

    public ManagerValidator() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static synchronized ManagerValidator getInstance() {
        if (instance == null) {
            instance = new ManagerValidator();
        }
        return instance;
    }

    public void validateManagerCredentials(String email, String password, String managerId, ManagerValidator.ManagerCredentialsListener listener) {
        mDatabase.child("Managers").child(managerId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String managerEmail = snapshot.child("email").getValue(String.class);
                            String managerPassword = snapshot.child("password").getValue(String.class);

                            assert managerEmail != null;
                            assert managerPassword != null;
                            if (managerEmail.equals(email) && managerPassword.equals(password)) {
                                // Manager email and password match the provided credentials
                                listener.onManagerCredentialsMatch();
                            } else {
                                // Manager email or password do not match the provided credentials
                                listener.onManagerCredentialsMismatch("Manager credentials do not match");
                            }
                        } else {
                            // Manager ID does not exist in the Managers node
                            listener.onManagerCredentialsMismatch("Invalid manager ID");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                        listener.onManagerCredentialsMismatch("Database Error");
                    }
                });
    }

    public interface ManagerCredentialsListener {
        void onManagerCredentialsMatch();
        void onManagerCredentialsMismatch(String errorMessage);
    }
}
