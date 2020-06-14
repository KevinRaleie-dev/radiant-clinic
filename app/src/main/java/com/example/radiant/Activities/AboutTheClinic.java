package com.example.radiant.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.radiant.Home_Activity;
import com.example.radiant.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AboutTheClinic extends AppCompatActivity {

    TextView loadDoc;
    ImageView backk;
    public FirebaseFirestore mfirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_the_clinic);

        loadDoc = findViewById(R.id.loadDoc);
        backk = findViewById(R.id.backk);
        mfirestore = FirebaseFirestore.getInstance();

        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Home_Activity.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference reference = mfirestore.collection("AboutTheClinic").document("AboutUs");

        reference.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if(e != null)
                {
                    Toast.makeText(AboutTheClinic.this, "Error loading doc", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", e.toString());
                    return;
                }

                if (documentSnapshot.exists())
                {
                    String des = documentSnapshot.getString("Description");

                    loadDoc.setText(" " + des );
                }
            }
        });
    }
}
