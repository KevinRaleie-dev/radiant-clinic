package com.example.radiant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Reset extends AppCompatActivity {

    EditText et_reset;
    ImageView btn_reset;
    FirebaseAuth mAuth;
    ImageView back;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        et_reset = findViewById(R.id.et_reset);
        btn_reset = findViewById(R.id.btn_reset);
        back = findViewById(R.id.back);
        progressBar = findViewById(R.id.progressBar);


        mAuth = FirebaseAuth.getInstance();



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ResetPassword();

            }
        });
    }

    public void ResetPassword()
    {
        String email = et_reset.getText().toString().trim();

        if(email.isEmpty())
        {
            et_reset.setError("Please enter your email");
            et_reset.requestFocus();
        }

        else
            {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Reset.this, "Check email to reset your password", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Reset.this, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
        });

    }

    }
}
