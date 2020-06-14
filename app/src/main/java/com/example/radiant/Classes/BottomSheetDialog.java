package com.example.radiant.Classes;

import android.content.Context;
import android.content.Intent;
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

public class BottomSheetDialog extends BottomSheetDialogFragment
{
    private BottomSheetListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet,container,false);

        CardView button = view.findViewById(R.id.appoint);
        CardView button2 = view.findViewById(R.id.payAtClinic);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonClicked();
                dismiss();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPayAtClinicClicked();
                dismiss();
            }
        });

        return view;
    }

    public interface BottomSheetListener
    {
        void onButtonClicked();
        void onPayAtClinicClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
        listener = (BottomSheetListener) context;}
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + "must implement BottomSheetListener");
        }
    }
}
