package com.example.barbershopapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.barbershopapp.Activities.AuthActivity;
import com.example.barbershopapp.Activities.MainActivity;
import com.example.barbershopapp.Activities.ManagerMainActivity;
import com.example.barbershopapp.Models.Appointment;
import com.example.barbershopapp.Models.Client;
import com.example.barbershopapp.Models.Manager;
import com.example.barbershopapp.Models.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

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

    // Register the client and call to saveClientDetailsOnDatabase function
    public void register(Client client , Context context) {

        String email = client.getEmail();
        String password = client.getPassword();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(context, "Successful Registration", Toast.LENGTH_SHORT).show();
                            Client clientWithUid = new Client(client.getUsername(), client.getEmail(), client.getPassword(), client.getPhone() , mAuth.getUid());
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

    // Store client details on database
    public void saveClientDetailsOnDatabase(Client client){
        assert client.getUid() != null : "User ID cannot be null";
        DatabaseReference clientsRef = mDatabase.child("Clients").child(client.getUid());
        clientsRef.setValue(client);
    }

    // Client login function
    public void login(String email , String password , Context context) {

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

    // Manager login function and call to checkManagerCredentials
    public void managerLogin(@NonNull String email , String password , String managerId , Context context) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Validate manager id
                            FirebaseManager.getInstance().validateManagerCredentials(email, password, managerId, new ManagerCredentialsListener() {
                                @Override
                                public void onManagerCredentialsMatch() {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(context, "Login Succeeded", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, ManagerMainActivity.class);
                                    intent.putExtra("manager id" , managerId);
                                    context.startActivity(intent);
                                }
                                @Override
                                public void onManagerCredentialsMismatch(String errorMessage) {
                                    // If manager credentials don't match, show error message
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public interface ManagerCredentialsListener {
        void onManagerCredentialsMatch();
        void onManagerCredentialsMismatch(String errorMessage);
    }

    // Validate that the manager ID exists and matches the provided user email and password.
    public void validateManagerCredentials(String email, String password, String managerId, ManagerCredentialsListener listener) {
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

    // Fetching data of the currently logged-in user(client)
    public void fetchCurrentUserDetails(onUserDetailsFetchedListener listener) {

        String uid = mAuth.getUid();
        mDatabase.child("Clients").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Client client = snapshot.getValue(Client.class);
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

    // Fetch available appointment hours at the barbershop
    public void fetchAvailableHours(String date , ArrayList<String> availableHours, Context context , onAvailableHoursFetchedListener listener) {
        mDatabase.child("Appointments").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isExist = snapshot.exists();
                // If the date exists in the database, there are appointments scheduled for that date
                if(isExist) {
                    for(int hour = 10; hour <= 21; hour++)
                    {
                        // If the hour node exists in the database, there is an appointment scheduled for that hour
                        if(!snapshot.hasChild(String.valueOf(hour) + ":00"))
                        {
                            // Add only available hours
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

    // Set an Appointment at the barbershop
    public void setAppointment(Appointment appointment, Context context) {

        String uid = appointment.getClient().getUid();

        // Set appointment on client record
        mDatabase.child("Clients").child(uid).child("Appointments").
                child(appointment.getDate()).child(appointment.getTime()).setValue(appointment);

        // Set appointment on Appointments collection
        mDatabase.child("Appointments").child(appointment.getDate()).child(appointment.getTime()).
                setValue(appointment);

        Toast.makeText(context ,"Appointment Set SUCCESS", Toast.LENGTH_SHORT).show();
    }

    public void fetchClosestAppointmentForClient(onClosestAppointmentDetailsFetchedListener listener) {
        String uid = mAuth.getUid();
        DatabaseReference ref = mDatabase.child("Clients").child(uid).child("Appointments");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                    String date = dateSnapshot.getKey();

                    if (isFutureTime((date + " 23:59"))) {
                        for (DataSnapshot hourSnapshot : dateSnapshot.getChildren()) {
                            String hour = hourSnapshot.getKey();
                            if (isFutureTime(date + " " + hour)) {
                                String service = hourSnapshot.child("serviceType").getValue(String.class);
                                Appointment closestAppointment = new Appointment(date, hour, service, null);
                                listener.onClosestAppointmentDetailsFetched(closestAppointment);
                                return;
                            }
                        }
                    }
                }
                listener.onClosestAppointmentDetailsFetched(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Closest Appointment error", "Closest Appointment error");
                listener.onClosestAppointmentDetailsFetched(null);
            }
        });
    }

    public interface onClosestAppointmentDetailsFetchedListener {
        void onClosestAppointmentDetailsFetched(Appointment appointment);
    }

    public boolean isFutureTime(String dateAndTime) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
            Date selectedDate = dateFormat.parse(dateAndTime);

            Date currentDate = new Date();

            if (selectedDate.after(currentDate)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Fetching Upcoming appointments for clients and managers
    public void fetchAppointments(Context context , ArrayList<Appointment> appointments , boolean isManager , onAppointmentsFetchedListener listener) {

        // If the function called by a client, it will retrieve only their appointments from the database.
        if (!isManager) {
            String uid = mAuth.getUid();
            DatabaseReference clientAppointmentsRef = mDatabase.child("Clients")
                    .child(uid)
                    .child("Appointments");
            clientAppointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dateSnapshot : snapshot.getChildren()){
                        String date = dateSnapshot.getKey();

                        if (isFutureTime((date + " 23:59"))) {
                            for (DataSnapshot timeSnapshot : dateSnapshot.getChildren()) {
                                String time = timeSnapshot.getKey();
                                if (isFutureTime(date + " " + time)) {
                                    appointments.add(timeSnapshot.getValue(Appointment.class));
                                }
                            }
                        }
                    }
                    listener.onAppointmentsFetched(appointments);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Data base access failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        // If the function called by a manager , fetch all Upcoming appointments at the barbershop
        else {
            mDatabase.child("Appointments").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dateSnapshot : snapshot.getChildren()){
                        String date = dateSnapshot.getKey();

                        if (isFutureTime((date + " 23:59"))) {
                            for (DataSnapshot timeSnapshot : dateSnapshot.getChildren()) {
                                String time = timeSnapshot.getKey();
                                if (isFutureTime(date + " " + time)) {
                                    appointments.add(timeSnapshot.getValue(Appointment.class));
                                }
                            }
                        }
                    }
                    listener.onAppointmentsFetched(appointments);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Data base access failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public interface onAppointmentsFetchedListener {
        void onAppointmentsFetched(ArrayList<Appointment> appointments);
    }

    public void cancelAppointment(Appointment appointment) {
        DatabaseReference appointmentsRef = mDatabase.child("Appointments");

        String date = appointment.getDate();
        String time = appointment.getTime();
        String uid = appointment.getClient().getUid();

        // Delete appointment from appointments collection
        appointmentsRef.child(date).child(time).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("FirebaseManager", "Appointment deleted successfully from client appointments.");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirebaseManager", "Failed to delete appointment from client appointments.", e);
                    }
                });

        // Delete appointment from client record
        DatabaseReference clientAppointmentsRef = mDatabase.child("Clients").child(uid).child("Appointments");
        clientAppointmentsRef.child(date).child(time).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("FirebaseManager", "Appointment deleted successfully from client appointments.");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirebaseManager", "Failed to delete appointment from client appointments.", e);
                    }
                });
    }

    // Fetch barbershop services list
    public void fetchServicesList(Context context , ArrayList<Service> servicesList , onServicesFetchedListener listener) {

        DatabaseReference servicesRef = mDatabase.child("Services");
        servicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot serviceSnapshot : snapshot.getChildren()) {
                    String serviceName = serviceSnapshot.getKey();
                    String servicePrice = serviceSnapshot.getValue(String.class);
                    Service service = new Service(serviceName , servicePrice);
                    servicesList.add(service);
                }
                listener.onServicesFetched(servicesList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Data base access failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public interface onServicesFetchedListener {
        void onServicesFetched(ArrayList<Service> services);
    }

    // Sign out function
    public void signOut(Context context) {
        FirebaseManager.getInstance().mAuth.signOut();
        Intent intent = new Intent(context, AuthActivity.class);
        context.startActivity(intent);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    // Fetch manager details from database
    public void fetchManagerDetails(String managerId , onManagerDetailsFetchedListener listener) {

        mDatabase.child("Managers").child(managerId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            String name = snapshot.child("username").getValue(String.class);
                            String email = snapshot.child("email").getValue(String.class);
                            String password = snapshot.child("password").getValue(String.class);
                            String phone = snapshot.child("phone").getValue(String.class);
                            Manager manager = new Manager(name , email , password , managerId);
                            listener.onManagerDetailsFetched(manager);
                        }
                        else {
                            listener.onManagerDetailsFetched(null);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onManagerDetailsFetched(null);
                    }
                });
    }

    public interface onManagerDetailsFetchedListener {
        void onManagerDetailsFetched(Manager manager);
    }

    // Fetch the next appointment at the barbershop
    public void fetchNextAppointment(onNextAppointmentFetchedListener listener) {

        mDatabase.child("Appointments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                    String date = dateSnapshot.getKey();

                    if (isFutureTime((date + " 23:59"))) {
                        for (DataSnapshot hourSnapshot : dateSnapshot.getChildren()) {
                            String hour = hourSnapshot.getKey();
                            if (isFutureTime(date + " " + hour)) {
                                String service = hourSnapshot.child("serviceType").getValue(String.class);
                                Client client = hourSnapshot.child("client").getValue(Client.class);
                                Appointment nextAppointment = new Appointment(date, hour, service, client);
                                listener.onNextAppointmentsFetched(nextAppointment);
                                return;
                            }
                        }
                    }
                }
                listener.onNextAppointmentsFetched(null);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Closest Appointment error", "Closest Appointment error");
                listener.onNextAppointmentsFetched(null);
            }
        });
    }

    public interface onNextAppointmentFetchedListener {
        void onNextAppointmentsFetched(Appointment appointment);
    }

    public void fetchClientsList(Context context , ArrayList<Client> clientArrayList , onClientsListFetchedListener listener){

        mDatabase.child("Clients").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot clientSnapshot : snapshot.getChildren()) {
                    clientArrayList.add(clientSnapshot.getValue(Client.class));
                }
                listener.onClientsListFetched(clientArrayList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Data base access failed", Toast.LENGTH_SHORT).show();
                listener.onClientsListFetched(null);
            }
        });
    }
    public interface onClientsListFetchedListener {
        void onClientsListFetched(ArrayList<Client> clientArrayList);
    }

    public void deleteClientAccount(Client clientToDelete) {
        String uid = clientToDelete.getUid();
        mDatabase.child("Clients").child(uid).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                deleteAllClientAppointments(clientToDelete);
            }
        });
    }

    private void deleteAllClientAppointments(Client clientToDelete) {
        mDatabase.child("Appointments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot appointmentDateSnapshot : snapshot.getChildren()) {
                    String appointmentDate = appointmentDateSnapshot.getKey().toString();
                    for (DataSnapshot appointmentHourSnapshot :appointmentDateSnapshot.getChildren()) {

                        String appointmentHour = appointmentHourSnapshot.getKey().toString();
                        Client client = appointmentHourSnapshot.child("client").getValue(Client.class);

                        assert client != null;
                        if (Objects.equals(client.getUid(), clientToDelete.getUid())){
                            mDatabase.child("Appointments").child(appointmentDate).child(appointmentHour).removeValue();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                return;
            }
        });
    }
}
