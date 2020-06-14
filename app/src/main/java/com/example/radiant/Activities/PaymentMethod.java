package com.example.radiant.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.radiant.Home_Activity;
import com.example.radiant.R;
import com.google.android.material.snackbar.Snackbar;

public class PaymentMethod extends AppCompatActivity {

    ImageView back_button;
    TextView add_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        initViews();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(), Home_Activity.class);
                startActivity(home);
                finish();
            }
        });

        add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"Under construction", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initViews() {
        back_button = findViewById(R.id.back_button);
        add_card = findViewById(R.id.add_card);
    }
}
