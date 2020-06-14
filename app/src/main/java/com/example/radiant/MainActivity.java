package com.example.radiant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.radiant.Activities.Signin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    ImageView _userPhoto, backArrow;
    EditText _email, _password, _name, _phonenumber, _title;
    ImageView _signUp;
    TextView _loginTv;
    ProgressBar progressBar;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirestore;
    static int ReqCode = 1;
    static int RCODE = 1;
    Uri pickedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        _userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                }
            }
        });

        _signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                SignUpUser();
                //InsertAsyncTask task = new InsertAsyncTask(MainActivity.this);
                //task.execute(10);
            }
        });

        _loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();

            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Signin.class));
                finish();
            }
        });

    }




    //start of AsyncTask
    private class InsertAsyncTask extends AsyncTask<Integer, Integer, String>
    {

        private WeakReference<MainActivity> activityWeakReference;
        InsertAsyncTask(MainActivity activity)
        {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing())
            {
                return;
           }
            activity.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Integer... integers) {


            //Add user to database here
            for (int i = 0; i < integers[0]; i++) {
                publishProgress((i * 100) / integers[0]);
                try
                {
                    Thread.sleep(1000);
                    SignUpUser();

                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            return "Finished";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing())
            {
               return;
            }
            activity.progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            MainActivity activity = activityWeakReference.get();
           if (activity == null || activity.isFinishing())
           {
                return;
            }
            Toast.makeText(activity,s,Toast.LENGTH_SHORT).show();
            activity.progressBar.setProgress(0);
            activity.progressBar.setVisibility(View.INVISIBLE);
        }
    }//end of AsyncTask



    private void initViews()
    {
        _userPhoto = findViewById(R.id._userPhoto);
        backArrow = findViewById(R.id.backArrow);
        _name = findViewById(R.id._name);
        _phonenumber = findViewById(R.id._phonenumber);
        _email = findViewById(R.id._email);
        _password = findViewById(R.id._password);
        _loginTv = findViewById(R.id._loginTv);
        _signUp = findViewById(R.id._signUp);
        _title = findViewById(R.id._title);
        progressBar = findViewById(R.id.progressBar);
    }

    private void openGallery()
    {
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery,RCODE);
    }


    /** This method checks and request permission from the user to access their gallery
     *  in order to pick an image to upload. if the permission was not granted, show a message to
     *  the user accept permissions. if the permission was granted, read the external storage of the device
     *  if the build version / SDK is >= 22 and open the gallery. **/
    private void checkAndRequestForPermission()
    {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Toast.makeText(MainActivity.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ReqCode);
            }

        }
        else
        {
            openGallery();
        }


    }


    /** This method handles adding a user to the database using HashMapping under a collection reference
     * named RegisteredUsers in Cloud Firestore. Each user will have a their email
     * that was provided as their unique document ID. Could say this is a "primary key"
     * **/
    private void addUserToDatabase()
    {
        String name = _name.getText().toString().trim();
        String email = _email.getText().toString().trim();
        String number = _phonenumber.getText().toString().trim();
        String title = _title.getText().toString().trim();

        CollectionReference ref = mFirestore.collection("RegisteredUsers");

        Map<String, String> user = new HashMap<>();
        user.put("Title", title);
        user.put("FullName", name);
        user.put("EmailAddress", email);
        user.put("PhoneNumber", number);

        ref.document(email).set(user);

    }


    /** This method takes care of creating a new user with their email and password.
     *  User enters their details in the provided fields, if they are not completed throw an error.
     *  if all fields are not empty, create  a user with email and password using Firebase authentication.
     *  if the task was successful, addUserToDatabase(), updateUserInfo() => their name,
     *  image that was provided and their credentials that were authorised by FirebaseAuth.
     *  if task was not successful throw an error message. **/
    private void SignUpUser()
    {
        final String name = _name.getText().toString().trim();
        String email = _email.getText().toString();
        String password = _password.getText().toString();

//        could work with regex expressions

        if(email.isEmpty() || !email.contains("@") || !email.contains("."))
        {
            _email.setError("Please enter a valid email");
            _email.requestFocus();
        }
        else if(name.isEmpty())
        {
            _name.setError("Please enter your name");
            _name.requestFocus();
        }
        else if (password.isEmpty())
        {
            _password.setError("Please enter a password");
            _password.requestFocus();
        }
        else if (!(email.isEmpty() && password.isEmpty()))
        {
            mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        _signUp.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        addUserToDatabase();
                        updateUserInfo(name,pickedImage,mFirebaseAuth.getCurrentUser());
                        Intent i = new Intent(getApplicationContext(),Home_Activity.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Sign up unsuccessful, try again",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        else
        {
            Toast.makeText(MainActivity.this,"Error occurred",Toast.LENGTH_LONG).show();

        }


    }

    /** Update the users name, profile photo and the current user details.
     *  Store the user's images inside Firebase Storage as a storage reference because we might
     *  need to use it later. if the image was uploaded successfully, create a path for the image
     *  and the current user's details, set their display name and set the image (override), and display a
     *  success message once registration has been completed
     * **/
    private void updateUserInfo(final String name, final Uri pickedImage, final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");

        if (pickedImage == null) {
            _userPhoto.setImageResource(R.drawable.undraw_profile);
        }
            try{
                final StorageReference imagePath = mStorage.child(pickedImage.getLastPathSegment());
                imagePath.putFile(pickedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .setPhotoUri(uri)
                                        .build();

                                currentUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Register complete!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });

            }catch (NullPointerException e)
            {
                e.printStackTrace();
            }

        }


    /** User has successfully uploaded an image, saving its reference to a URI variable **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == RCODE && data != null)
        {
            //user successfully uploaded an image
            //saving its ref to URI variable
            pickedImage = data.getData();
            _userPhoto.setImageURI(pickedImage);

        }
    }
}
