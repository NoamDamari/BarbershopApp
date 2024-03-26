package com.example.barbershopapp;

import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barbershopapp.Activities.LoginActivity;
import com.example.barbershopapp.Activities.MainActivity;
import com.example.barbershopapp.Activities.RegisterActivity;
import com.example.barbershopapp.Activities.SetAppointmentActivity;
import com.example.barbershopapp.Adapters.HoursAdapter;
import com.example.barbershopapp.Models.Appointment;
import com.example.barbershopapp.Models.Client;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;

public class FirebaseManager {

    private static FirebaseManager instance;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public FirebaseManager() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static synchronized FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }

    public void register(Client client , Context context) {

        String name = client.getUsername();
        String email = client.getEmail();
        String password = client.getPassword();
        String phone = client.getPhone();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()){
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(context, "Registration Succeeded", Toast.LENGTH_SHORT).show();
                                FirebaseManager.getInstance().saveClientDetailsOnDatabase(client , mAuth.getUid());
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

    public void saveClientDetailsOnDatabase(Client client , String uid){
        assert uid != null : "User ID cannot be null";
        DatabaseReference clientsRef = mDatabase.child("Clients").child(uid);
        clientsRef.setValue(client);
    }

    public void login(String email , String password , Context context) {

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(context, "Login Succeeded", Toast.LENGTH_SHORT).show();
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

    public void managerLogin(@NonNull String email , String password , String managerId , Context context) {

        if (email.isEmpty() || password.isEmpty() || managerId.isEmpty()) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseManager.getInstance().checkManagerCredentials(email, password, managerId, new ManagerCredentialsListener() {
                                    @Override
                                    public void onManagerCredentialsMatch() {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(context, "Login Succeeded", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context, MainActivity.class);
                                        context.startActivity(intent);
                                    }
                                    @Override
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

    public interface ManagerCredentialsListener {
        void onManagerCredentialsMatch();
        void onManagerCredentialsMismatch(String errorMessage);
    }

    public void checkManagerCredentials(String email, String password, String managerId, ManagerCredentialsListener listener) {
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

    public void getCurrentUserDetails(onUserDetailsFetchedListener listener) {

        String uid = mAuth.getUid();
        mDatabase.child("Clients").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String name = snapshot.child("username").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String password = snapshot.child("password").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    Client client = new Client(name , email , password , phone);
                    listener.onUserDetailsFetched(client);
                }
                else {
                    listener.onUserDetailsFetched(null);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onUserDetailsFetched(null);
            }
        });
    }

    public interface onUserDetailsFetchedListener {
        void onUserDetailsFetched(Client client);
    }

    public void getAvailableHours(String date , ArrayList<String> availableHours, Context context , onAvailableHoursFetchedListener listener) {
        mDatabase.child("Appointments").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isExist = snapshot.exists();
                if(isExist) {
                    for(int hour = 10; hour <= 21; hour++)
                    {
                        if(!snapshot.hasChild(String.valueOf(hour) + ":00"))
                        {
                            availableHours.add(String.valueOf(hour) + ":00");
                        }
                    }
                }
                else
                {
                    // Add all hours as available to hours list (all hours available)
                    for (int hour = 10; hour <= 21; hour++){
                        availableHours.add(String.valueOf(hour) + ":00");
                    }
                }
                listener.onAvailableHoursFetched(availableHours);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onAvailableHoursFetched(null);
                Toast.makeText(context, "Data base access failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public interface onAvailableHoursFetchedListener {
        void onAvailableHoursFetched(ArrayList<String> availableHours);
    }

    public void setAppointment(Appointment appointment, Context context) {

        String uid = mAuth.getUid();

        mDatabase.child("Clients").child(uid).child("Appointments").
                child(appointment.getDate()).child(appointment.getTime()).setValue(appointment);
        mDatabase.child("Appointments").child(appointment.getDate()).child(appointment.getTime()).
                setValue(appointment);

        Toast.makeText(context ,"Appointment Set SUCCESS", Toast.LENGTH_SHORT).show();
    }
}
