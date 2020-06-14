package com.example.radiant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.radiant.Activities.Signin;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText _email, _password;
    ImageView _login, back_button;
    TextView _reset;
    ProgressBar progressBar;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser currentUser;
    CardView cd_google, cd_facebook;
    GoogleSignInClient mGoogleSigninClient;
    String TAG = "firebase login";
    int RC_SIGN_IN = 123;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initViews();
        mFirebaseAuth = FirebaseAuth.getInstance();

        //Configure google sign-in options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSigninClient = GoogleSignIn.getClient(getApplicationContext(), gso);

        authStateListener =  new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth)
            {
                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

                if (firebaseUser != null)
                {
                    Toast.makeText(Login.this,"Logging in",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),Home_Activity.class);
                    startActivity(intent);
                    finish();
                }

            }
        };

        _login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginUser();

            }
        });

        _reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Reset.class);
                startActivity(i);
                finish();

            }
        });

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

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign_in = new Intent(getApplicationContext(), Signin.class);
                startActivity(sign_in);
                finish();
            }
        });
    }

    private void initViews()
    {
        _email = findViewById(R.id._email);
        _password = findViewById(R.id._password);
        _login = findViewById(R.id._login);
        _reset = findViewById(R.id._reset);
        progressBar = findViewById(R.id.progressBar);
        cd_google = findViewById(R.id.cd_google);
        cd_facebook = findViewById(R.id.cd_facebook);
        back_button = findViewById(R.id.back_button);
    }

    /** Get the sign-in activity from Google,
     * in order to choose the email you are signing up with **/
    private void signIn()
    {
        _reset.setVisibility(View.INVISIBLE);
        _login.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Intent signIntent = mGoogleSigninClient.getSignInIntent();
        startActivityForResult(signIntent, RC_SIGN_IN);
    }

    /** Get the result returned from launching the intent from GoogleSignInAPI
     * check whether Google sign in was successful, if so, make a request from firebase
     * to authenticate the user...
     * else, catch the API exception
     * **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                _reset.setVisibility(View.VISIBLE);
                _login.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

            }
        }
    }

    /** Authenticate the user's account with firebase and sign-in with their credentials
     *  if the task is successful, navigate the user to the home page and add
     *  their credentials/information to the database.
     *  if the task was not successful, display a message of error.
     *  **/
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct)
    {
        Log.d(TAG,"firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
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

    private void addUserToDatabase(FirebaseUser user)
    {
        if(user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String number = user.getPhoneNumber();

            FirebaseFirestore mfirestore = FirebaseFirestore.getInstance();
            CollectionReference reference = mfirestore.collection("RegisteredUsers");

            Map<String, String> register = new HashMap<>();

            assert name != null;
            register.put("FullName", name);
            assert email != null;
            register.put("EmailAddress", email);
            assert number != null;
            register.put("PhoneNumber", number);


            reference.document(user.getEmail()).set(register);
        }
    }

    /** Logging in a user with email and password, if a user registered with their email.
     *  test if all fields have been completed, if not, throw an error. If both fields
     *  are not empty, sign in a user with their email and password using FirebaseAuth
     *  and if the task was successful navigate to home activity, if not, throw an error message.
     *  **/
    public void LoginUser()
    {
        String email = _email.getText().toString();
        String password = _password.getText().toString();

        if(email.isEmpty())
        {
            _email.setError("Enter your email");
            _email.requestFocus();
        }
        else if (password.isEmpty())
        {
            _password.setError("Enter your password");
            _password.requestFocus();
        }
        else if(!email.contains("@") || !email.contains("."))
        {
            _email.setError("Not a valid email address");
            _email.requestFocus();
        }
        else if (!(email.isEmpty() && password.isEmpty()))
        {
            mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful())
                    {
                         Toast.makeText(Login.this,"Login unsuccessful, try again",Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        _login.setVisibility(View.INVISIBLE);
                        _reset.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        Intent i = new Intent(getApplicationContext(),Home_Activity.class);
                        startActivity(i);
                        finish();
                    }

                }
            });
        }
        else
        {
            Toast.makeText(Login.this,"Error occurred",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    /** Check if user is signed-in (non-null), if so, navigate to home page
     * else, required to login
     * **/
    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(authStateListener);

        currentUser = mFirebaseAuth.getCurrentUser();
    }



}

