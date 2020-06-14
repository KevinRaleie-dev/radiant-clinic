package com.example.radiant.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.radiant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PatientAddress extends AppCompatActivity {

    ImageView backbutton;
    Button next_button;
    EditText housenumber, streetname, town_city, province, postal_code;
    FirebaseAuth mAuth;
    FirebaseFirestore mfirestore;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_address);

        initViews();
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePatientFile();

            }
        });
    }

    public void initViews()
    {
        backbutton = findViewById(R.id.backbutton);
        housenumber = findViewById(R.id.housenumber);
        streetname = findViewById(R.id.streetname);
        town_city = findViewById(R.id.town_city);
        province = findViewById(R.id.province);
        postal_code = findViewById(R.id.postal_code);
        next_button = findViewById(R.id.next_button);
    }

    private void updatePatientFile()
    {
        String number = housenumber.getText().toString().trim();
        String street = streetname.getText().toString().trim();
        String town = town_city.getText().toString().trim();
        String prov = province.getText().toString().trim();
        String postal = postal_code.getText().toString().trim();

        if (town.isEmpty())
        {
            town_city.setError("Please enter the city you live in");
        }

        CollectionReference reference = mfirestore.collection("PatientFile");
        Map<String, Object> address = new HashMap<>();
        address.put("HouseNumber", number);
        address.put("StreetName", street);
        address.put("City", town);
        address.put("Province", prov);
        address.put("PostalCode", postal);

        reference.document(currentUser.getEmail()).update(address).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    startActivity(new Intent(getApplicationContext(), RelativeOfPatient.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PatientAddress.this, "Please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
