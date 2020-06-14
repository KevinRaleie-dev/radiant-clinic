package com.example.radiant.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.radiant.Classes.BottomSheetDialog;
import com.example.radiant.Home_Activity;
import com.example.radiant.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookAppointment extends AppCompatActivity implements BottomSheetDialog.BottomSheetListener{

    EditText _description;
    Button _time;
    FloatingActionButton btn_set;
    Calendar currentTime;
    int hour, minute;
    String format, timeOfAppointment;
    Spinner spinner;
    ImageView btn_cancel;
    CalendarView calendarView;
    String date, service;
    FirebaseAuth mAuth;
    private FirebaseAnalytics analytics;
    FirebaseFirestore mfirestore;
    FirebaseUser currentUser;
    private String userEmail, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        initViews();

        spinnerConfig();

        mfirestore = FirebaseFirestore.getInstance();
        analytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userEmail = currentUser.getEmail();
        userName = currentUser.getDisplayName();

        currentTime = Calendar.getInstance();
        hour = currentTime.get(Calendar.HOUR_OF_DAY);
        minute = currentTime.get(Calendar.MINUTE);

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedToPayment();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  goToAppointmentList();
                startActivity(new Intent(getApplicationContext(), Home_Activity.class));
                finish();
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = year + "-" + (month +1) + "-" + dayOfMonth;
            }
        });


        _time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(BookAppointment.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedTimeFormat(hourOfDay);
                        timeOfAppointment = hourOfDay + ":" + minute + " " + format;
                    }
                }, hour, minute, true);
                timePickerDialog.show();

            }
        });

    }


    public void selectedTimeFormat(int hour)
    {
        if(hour == 0)
        {
            hour +=12;
            format = "AM";

        }
        else if(hour == 12)
        {
            format = "PM";
        }
        else if( hour > 12)
        {
            hour -= 12;
            format = "PM";
        }
        else
        {
            format = "AM";
        }

    }

    public void goToAppointmentList()
    {
        Intent home = new Intent(getApplicationContext(), ViewAppointments.class);
        startActivity(home);
    }


    private void proceedToPayment()
    {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
        bottomSheetDialog.show(getSupportFragmentManager(), "bottomSheet");
    }


    public void initViews()
    {
        _time = findViewById(R.id._time);
        btn_set = findViewById(R.id.btn_set);
        btn_cancel = findViewById(R.id.btn_cancel);
        calendarView = findViewById(R.id.calendarView);
        _description = findViewById(R.id._description);
        spinner = findViewById(R.id.spinner);

    }

    private void spinnerConfig()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.services, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                service = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    @Override
    public void onButtonClicked() {

            String payment = "Paying online";
            String desc = _description.getText().toString().trim();


            CollectionReference ref = mfirestore.collection(userEmail);

            Map<String, Object> appoint = new HashMap<>();
            appoint.put("Service",service );
            appoint.put("Time", timeOfAppointment);
            appoint.put("Email", userEmail);
            appoint.put("Description", desc);
            appoint.put("Date", date);
            appoint.put("FullName", userName);
            appoint.put("Payment",payment);

            ref.document().set(appoint).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Appointment set!", Toast.LENGTH_LONG).show();
                    goToPayPal();
                    logEvent();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Unable to make appointment, try again", Toast.LENGTH_LONG).show();

                }
            });
    }

    @Override
    public void onPayAtClinicClicked() {

            String payment = "Paying at the clinic";
            String desc = _description.getText().toString().trim();


            CollectionReference ref = mfirestore.collection(userEmail);

            Map<String, Object> appoint = new HashMap<>();
            appoint.put("Service",service );
            appoint.put("Time", timeOfAppointment);
            appoint.put("Email", userEmail);
            appoint.put("Description", desc);
            appoint.put("Date", date);
            appoint.put("FullName",userName);
            appoint.put("Payment",payment);

            ref.document().set(appoint).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    goToAppointmentList();
                    logEvent();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Unable to make appointment, try again", Toast.LENGTH_LONG).show();

                }
            });
    }


    private void goToPayPal()
    {
        Intent paypal = new Intent(getApplicationContext(), Payment.class);
        startActivity(paypal);
        finish();
    }

    private void logEvent()
    {
        analytics.logEvent("appointment_set", null);
    }

}
