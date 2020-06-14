package com.example.radiant.Classes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.radiant.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PatientSheetDialog extends BottomSheetDialogFragment
{
    private PatientSheetListener pListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.patientfile_sheet,container, false);

        CardView button = view.findViewById(R.id.create);
        CardView button2 = view.findViewById(R.id.edit_profile);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pListener.onPatientButtonClicked();
                dismiss();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pListener.onViewAppointmentButtonClicked();
                dismiss();
            }
        });

        return view;
    }

    public interface PatientSheetListener
    {
        void onPatientButtonClicked();
        void onViewAppointmentButtonClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try
        {
            pListener = (PatientSheetListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + "must implement PatientSheetListener");
        }

    }
}
