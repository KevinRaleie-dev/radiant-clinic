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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class PatientFile extends AppCompatActivity {

    EditText _ID, _houseNum, _streetName, _town, _postal, _occupation, _employer, _fullName,
            _relation, _province, _phone;
    Button _submit;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_file);

        initViews();
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createPatientFile();

            }
        });

    }

    public void initViews() {
        _ID = findViewById(R.id._ID);
        _houseNum = findViewById(R.id._houseNum);
        _streetName = findViewById(R.id._streetName);
        _town = findViewById(R.id._town);
        _postal = findViewById(R.id._postal);
        _occupation = findViewById(R.id._occupation);
        _employer = findViewById(R.id._employer);
        _fullName = findViewById(R.id._fullName);
        _relation = findViewById(R.id._relation);
        _province = findViewById(R.id._province);
        _phone = findViewById(R.id._phone);
        _submit = findViewById(R.id._submit);

    }

    public void goToProfile()
    {
        Intent profile = new Intent(getApplicationContext(),Profile.class);
        startActivity(profile);
        finish();
    }

    /** Validate a South African ID using Luhn's algorithm **/

    public void createPatientFile() {

        String id = _ID.getText().toString().trim();
        String houseNum = _houseNum.getText().toString().trim();
        String street = _streetName.getText().toString().trim();
        String town = _town.getText().toString().trim();
        String postal = _postal.getText().toString().trim();
        String occupation = _occupation.getText().toString().trim();
        String employer = _employer.getText().toString().trim();
        String fullname = _fullName.getText().toString().trim();
        String relation = _relation.getText().toString().trim();
        String province = _province.getText().toString().trim();
        String relPhone = _phone.getText().toString().trim();


        if(id.isEmpty())
        {
            _ID.setError("Please enter a valid ID number");
            _ID.requestFocus();
        }
        else
            {

            CollectionReference ref = mFirestore.collection("PatientFile");

            Map<String, Object> file = new HashMap<>();
            file.put("IDNumber", id);
            file.put("HouseNumber", houseNum);
            file.put("StreetName", street);
            file.put("City", town);
            file.put("PostalCode", postal);
            file.put("Province", province);
            file.put("Occupation", occupation);
            file.put("Employer", employer);
            file.put("RelativeFullName", fullname);
            file.put("Relation", relation);
            file.put("RelativePhoneNumber", relPhone);

            assert currentUser != null;
            ref.document(currentUser.getEmail()).set(file).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Patient file created.", Toast.LENGTH_LONG).show();
                    goToProfile();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Unable to create file", Toast.LENGTH_LONG).show();

                }
            });

        }
    }
}
