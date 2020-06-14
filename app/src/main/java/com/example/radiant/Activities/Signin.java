package com.example.radiant.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.radiant.Home_Activity;
import com.example.radiant.Login;
import com.example.radiant.MainActivity;
import com.example.radiant.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signin extends AppCompatActivity {

    int RC_SIGN_IN = 123;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore mfirestore;
    GoogleSignInClient mGoogleSigninClient;
    String TAG = "firebase login";
    ProgressBar progressBar;
    TextView have_account, terms_con;
    CardView cd_google, cd_facebook, cd_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initViews();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleSigninClient = GoogleSignIn.getClient(getApplicationContext(), gso);

        mAuth = FirebaseAuth.getInstance();
        mfirestore = FirebaseFirestore.getInstance();

        cd_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        cd_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"Feature not available yet.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        cd_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(reg);
                finish();
            }
        });
        have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getApplicationContext(), Login.class);
                startActivity(login);
                finish();
            }
        });
        terms_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terms = new Intent(getApplicationContext(), Terms.class);
                startActivity(terms);
                finish();
            }
        });


    }


    /** Get the sign-in activity from Google,
     * in order to choose the email you are signing up with **/
    private void signIn()
    {
        progressBar.setVisibility(View.VISIBLE);
        Intent signIntent = mGoogleSigninClient.getSignInIntent();
        startActivityForResult(signIntent, RC_SIGN_IN);

    }

    private void initViews()
    {
        cd_email = findViewById(R.id.cd_email);
        cd_facebook = findViewById(R.id.cd_facebook);
        cd_google = findViewById(R.id.cd_google);
        have_account = findViewById(R.id.have_account);
        progressBar = findViewById(R.id.progressBar);
        terms_con = findViewById(R.id.terms_con);
    }

    /** Check if user is signed-in (non-null), if so, navigate to home page
     * else, required to login **/
    @Override
    protected void onStart() {
        super.onStart();

        currentUser = mAuth.getCurrentUser();
    }

    /** Get the result returned from launching the intent from GoogleSignInAPI
     * check whether Google sign in was successful, if so, make a request from firebase
     * to authenticate the user...
     * else, catch the API exception**/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);

                if (account != null)
                {
                firebaseAuthWithGoogle(account);
                }

            }
            catch(ApiException e)
            {
                e.printStackTrace();
                progressBar.setVisibility(View.INVISIBLE);

            }
        }
    }

    /** Authenticate the user's account with firebase and sign-in with their credentials
     *  if the task is successful, navigate the user to the home page and add
     *  their credentials/information to the database.
     *  if the task was not successful, display a message of error. **/
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct)
    {
        Log.d(TAG,"firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent home = new Intent(getApplicationContext(), Home_Activity.class);
                            startActivity(home);
                            finish();
                            addUserToDatabase(user);
                            Toast.makeText(getApplicationContext(),"Signing you in", Toast.LENGTH_SHORT).show();
                        }
                        else
                            {

                            Toast.makeText(getApplicationContext(),"Authentication failed.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }


    /** Once signing-in was successful, we get the users information
     * and store it inside our database collection of registered users. Each user
     * will have a document with all their details and their email as their document ID/
     * "primary key".
     * **/
    public void addUserToDatabase(FirebaseUser user)
    {
        if(user != null)
        {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String auth = "Signed in with google";

            CollectionReference reference = mfirestore.collection("RegisteredUsers");

            Map<String, String> register = new HashMap<>();

            assert  name != null;
            register.put("FullName", name);
            assert email != null;
            register.put("EmailAddress", email);
            register.put("PhoneNumber", auth);
            register.put("SecondaryEmail", "");


            reference.document(user.getEmail()).set(register);

        }
    }

}
