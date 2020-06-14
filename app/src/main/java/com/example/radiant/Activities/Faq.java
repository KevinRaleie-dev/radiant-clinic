package com.example.radiant.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.radiant.Adapters.QuestionAdapter;
import com.example.radiant.Classes.Questions;
import com.example.radiant.Home_Activity;
import com.example.radiant.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class Faq extends AppCompatActivity {

    ImageView back_button;
    EditText questions;
    Button submit;
    String username;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore mfirestore;

    private QuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        initViews();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mfirestore = FirebaseFirestore.getInstance();
        username = currentUser.getDisplayName();


        setUpRecyclerView();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backHome();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frequentlyAskedQuestions();
                questions.setText("");

            }
        });
    }

    private void setUpRecyclerView()
    {
        CollectionReference reference = mfirestore.collection("FrequentlyAskedQuestions");
        Query query = reference.orderBy("Question", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Questions> options = new FirestoreRecyclerOptions.Builder<Questions>()
                .setQuery(query, Questions.class)
                .build();

        adapter = new QuestionAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_questions);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

    }

    private void frequentlyAskedQuestions() {

        String quiz = questions.getText().toString().trim();

        if (quiz.length() < 10)
        {
            questions.setError("question is too short");
        }
        else {
            CollectionReference reference = mfirestore.collection("FrequentlyAskedQuestions");

            Map<String, String> ask = new HashMap<>();
            ask.put("Question", quiz);
            ask.put("FullName", username);
            ask.put("Answer","No answer yet");

            reference.document().set(ask).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Question sent!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Could not send question, try again", Toast.LENGTH_SHORT).show();

                }
            });

        }


    }

    public void initViews()
    {
        back_button = findViewById(R.id.back_button);
        questions = findViewById(R.id.questions);
        submit = findViewById(R.id.submit);
    }
    public void backHome()
    {
        Intent home = new Intent(getApplicationContext(), Home_Activity.class);
        startActivity(home);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
