package com.example.radiant.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.radiant.Home_Activity;
import com.example.radiant.Login;
import com.example.radiant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends AppCompatActivity {

    ImageView back_button;
    TextView sign_out, deactivate, account, connect_twitter, terms, join;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backhome();
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goToaccount = new Intent(getApplicationContext(), Account.class);
                startActivity(goToaccount);

            }
        });

        connect_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Feature not available yet.", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignOutUser();
            }
        });

        deactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeactivateUserAccount();
                Snackbar.make(v, "Account deactivated", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Terms.class);
                startActivity(intent);
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                startActivity(i);
            }
        });
    }

    private void DeactivateUserAccount()
    {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        try {
            currentUser.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                               // Toast.makeText(getApplicationContext(),"Account deleted", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), Signin.class);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Could not delete account", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }

    }

    private void SignOutUser()
    {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
        finish();

    }

    private void initViews()
    {
        back_button = findViewById(R.id.back_button);
        sign_out = findViewById(R.id.sign_out);
        deactivate = findViewById(R.id.deactivate);
        connect_twitter = findViewById(R.id.connect_twitter);
        account = findViewById(R.id.account);
        terms = findViewById(R.id.terms);
        join = findViewById(R.id.join);
    }
    private void backhome()
    {
        Intent home = new Intent(getApplicationContext(), Home_Activity.class);
        startActivity(home);
        finish();
    }
}
