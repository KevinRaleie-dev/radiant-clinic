package com.example.radiant.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.radiant.Classes.PatientSheetDialog;
import com.example.radiant.Home_Activity;
import com.example.radiant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Profile extends AppCompatActivity implements PatientSheetDialog.PatientSheetListener {

    TextView userEmail, userName;
    ImageView profile_image, back_button, settings_button;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        initViews();
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();



        userProfile();

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar.make(v, "This should open your image", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientSheetDialog patient = new PatientSheetDialog();
                patient.show(getSupportFragmentManager(), "open_patient_file");
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(), Home_Activity.class);
                startActivity(home);
                finish();
            }
        });


    }

    public void initViews()
    {
        back_button = findViewById(R.id.back_button);
        settings_button = findViewById(R.id.settings_button);
        profile_image = findViewById(R.id.profile_image);
        userEmail = findViewById(R.id.userEmail);
        userName = findViewById(R.id.userName);
    }

    public void userProfile (){

        userEmail.setText(currentUser.getEmail());
        userName.setText(currentUser.getDisplayName());
        Glide.with(this).load(currentUser.getPhotoUrl()).into(profile_image);
    }

    @Override
    public void onPatientButtonClicked() {
        Intent patient = new Intent(getApplicationContext(), ParticularsOfClient.class);
        startActivity(patient);
    }


    /** Should open an activity where user can edit their profile and save changes to
     * Firebase  **/
    @Override
    public void onViewAppointmentButtonClicked() {

        startActivity(new Intent(getApplicationContext(), ViewAppointments.class));
    }

}
