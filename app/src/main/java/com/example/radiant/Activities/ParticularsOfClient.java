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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class ParticularsOfClient extends AppCompatActivity {

    ImageView back;
    EditText idnumber, occupation, employer;
    Button next;
    String userEmail;
    FirebaseFirestore mfirestore;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particulars_of_client);

        initViews();
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userEmail = currentUser.getEmail();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Profile.class);
                startActivity(i);
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePatientFile();
            }
        });
    }

    private void initViews()
    {
        back = findViewById(R.id.back);
        idnumber = findViewById(R.id.idnumber);
        occupation = findViewById(R.id.occupation);
        employer = findViewById(R.id.employer);
        next = findViewById(R.id.next);
    }

    public Boolean validateId(BigInteger identities)
    {
        char[] id_chars = identities.toString().toCharArray();
        int sum = 0;

        //loop over each digit right to left, including the check-digit
        for(int i = 1; i<= id_chars.length; i++)
        {
            int digit = Character.getNumericValue(id_chars[id_chars.length - i]);
            if((i % 2) != 0)
            {
                sum += digit;
            }
            else
            {
                sum += digit < 5 ? digit * 2 : digit * 2 - 9;
            }
        }
        return (sum % 10) == 0;
    }


    private void updatePatientFile()
    {
        String id = idnumber.getText().toString().trim();
        String work = occupation.getText().toString().trim();
        String employment = employer.getText().toString().trim();


        if (id.isEmpty())
        {
            idnumber.setError("Please enter an ID number");
            idnumber.requestFocus();
        }

        else {
            CollectionReference reference = mfirestore.collection("PatientFile");

            Map<String, Object> particulars = new HashMap<>();
            particulars.put("IDNumber", id);
            particulars.put("Occupation",work);
            particulars.put("Employer", employment);

            reference.document(userEmail).set(particulars).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        startActivity(new Intent(getApplicationContext(), PatientAddress.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ParticularsOfClient.this, "Could not add your file", Toast.LENGTH_SHORT).show();
                }
            });

        }


    }
}
