package com.example.radiant.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.radiant.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Account extends AppCompatActivity {

    EditText input_email;
    TextView display_email;
    ImageView back_button;
    Button add_email;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore mfirestore;
    String input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initViews();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mfirestore = FirebaseFirestore.getInstance();

        getUserEmail();

        input_email.addTextChangedListener(addEmailWatcher);

        add_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                input = input_email.getText().toString().trim();

                if(input.isEmpty())
                {
                    input_email.setError("Please enter a valid email");
                }
                else if(!input.contains("@"))
                {
                    input_email.setError("Please enter a valid email");
                }
                else
                {
                    addUserEmail();
                    input_email.setText("");
                    Snackbar.make(v,"Email address added", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });

    }

    private TextWatcher addEmailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String userInput = input_email.getText().toString().trim();
            add_email.setEnabled(!userInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void addUserEmail() {

        CollectionReference reference = mfirestore.collection("RegisteredUsers");
        Map<String, Object> add = new HashMap<>();
        add.put("SecondaryEmail", input);

        reference.document(currentUser.getEmail()).update(add);
    }

    private void initViews()
    {
        input_email = findViewById(R.id.input_email);
        display_email = findViewById(R.id.display_email);
        add_email = findViewById(R.id.add_email);
        back_button = findViewById(R.id.back_button);
    }

    private void getUserEmail()
    {
        display_email.setText(currentUser.getEmail());
    }
}
