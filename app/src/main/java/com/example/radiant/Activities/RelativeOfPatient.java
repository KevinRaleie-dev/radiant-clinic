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
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RelativeOfPatient extends AppCompatActivity {

    ImageView back_b;
    EditText fullname, relation, phonenumber;
    Button create;

    public FirebaseAuth mAuth;
    public FirebaseFirestore mfirestore;
    public FirebaseUser currentUser;
    private String useremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative_of_patient);

        initViews();
        mAuth = FirebaseAuth.getInstance();
        mfirestore = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        useremail = currentUser.getEmail();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePatientFile();
            }
        });
    }

    private void initViews()
    {
        back_b = findViewById(R.id.back_b);
        fullname = findViewById(R.id.fullname);
        relation = findViewById(R.id.relation);
        phonenumber = findViewById(R.id.phonenumber);
        create = findViewById(R.id.create);
    }

    private void updatePatientFile()
    {
        String name = fullname.getText().toString().trim();
        String relative = relation.getText().toString().trim();
        String number = phonenumber.getText().toString().trim();

        CollectionReference reference = mfirestore.collection("PatientFile");

        Map<String, Object> relativeOfPatient = new HashMap<>();
        relativeOfPatient.put("RelativeFullName", name);
        relativeOfPatient.put("Relation", relative);
        relativeOfPatient.put("RelativePhoneNumber", number);

        reference.document(useremail).update(relativeOfPatient).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                    finish();
                    Toast.makeText(RelativeOfPatient.this, "Patient file created", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(RelativeOfPatient.this, "Something went wrong, could not create file", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
