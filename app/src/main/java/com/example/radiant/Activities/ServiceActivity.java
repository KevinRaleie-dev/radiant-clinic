package com.example.radiant.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.radiant.Adapters.ServiceAdapter;
import com.example.radiant.Classes.ServiceItem;
import com.example.radiant.R;

import java.util.ArrayList;
import java.util.List;

public class ServiceActivity extends AppCompatActivity {

    RecyclerView service_rv;
    ServiceAdapter serviceAdapter;
    List<ServiceItem> sData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //hide action bar
        //getSupportActionBar().hide();
        //ini view
        service_rv = findViewById(R.id.service_rv);
        sData = new ArrayList<>();

        //Fill list with data
        sData.add(new ServiceItem("Consultation", "Patients can enter the clinic to consult about any thing. If you're not sure about something, consult our doctors any day.", R.drawable.c));
        sData.add(new ServiceItem("Antenatal Care", "Antenatal care is a type of preventative healthcare. Its goal is to provide regular check-ups that allow doctors or midwives to treat and prevent potential health problems throughout the course of the pregnancy and to promote healthy lifestyles that benefit both mother and child.", R.drawable.a));
        sData.add(new ServiceItem("Antenatal Classes", "Antenatal classes are invaluable in helping to prepare expecting parents for birth and parenthood.", R.drawable.a));
        sData.add(new ServiceItem("Sonar", "An ultrasound scan uses a machine which emits very high frequency sound waves (inaudible) which reflect off body tissues and create a picture of our organs. The procedure is painless and usually takes about 45 minutes.", R.drawable.s));
        sData.add(new ServiceItem("Births", "We offer birth services in the comfort of your home ensuring your little one sees home first, and not a hospital bed.", R.drawable.b));
        sData.add(new ServiceItem("Home Visits / Continuity of care", "We come to your home to offer all of our services.", R.drawable.h));
        sData.add(new ServiceItem("Baby Care", "Our baby care services involves taking care of baby and you during the postpartum. The postpartum period begins after the delivery of your baby and ends when your body has nearly returned to its pre-pregnant state. This period often lasts 6 to 8 weeks.", R.drawable.b));
        sData.add(new ServiceItem("Immunisations", "Immunisation is the process whereby a person is made immune or resistant to an infectious disease, typically by the administration of a vaccine.", R.drawable.i));
        sData.add(new ServiceItem("Postnatal Care", "Postnatal care is the the care given to a mother and her newborn baby immediately after the birth and for the first six weeks of life.", R.drawable.p));
        sData.add(new ServiceItem("Family Planning", "We offer the best the family plans in Bloemfontein ensuring you have the family of your dreams.", R.drawable.f));
        sData.add(new ServiceItem("Dual Protections", "The dual protection approach is when you use a male condom and another effective contraceptive method at the same time. Dual methods offer more protection from pregnancy than condoms alone. It also protects you and your partner from STIs, including HIV.", R.drawable.d));
        sData.add(new ServiceItem("STI'S", "We offer testing and treatments from STIs that usually come in single dose antibiotics that can cure many sexually transmitted bacterial and parasitic infections.", R.drawable.s));
        sData.add(new ServiceItem("Pregnancy Test", "Not sure if you're pregnant? Come visit us for an accurate pregnancy test.", R.drawable.p));
        sData.add(new ServiceItem("HIV Testing & Counselling", "We offer HIV testing as well counselling if required.", R.drawable.h));
        sData.add(new ServiceItem("Papsmear", "A Pap smear is a procedure to test for cervical cancer in women. This procedure involves collecting cells from your cervix.", R.drawable.p));

        //adapter ini and setup

        serviceAdapter = new ServiceAdapter(this,sData);
        service_rv.setAdapter(serviceAdapter);
        service_rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
