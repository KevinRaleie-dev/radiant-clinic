package com.example.radiant.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.radiant.Classes.Appointment;
import com.example.radiant.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;

public class AppointmentAdapter extends FirestoreRecyclerAdapter<Appointment, AppointmentAdapter.AppointmentHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AppointmentAdapter(@NonNull FirestoreRecyclerOptions<Appointment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AppointmentHolder appointmentHolder, int i, @NonNull Appointment appointment) {
        appointmentHolder.textViewService.setText(appointment.getService());
        appointmentHolder.textViewDate.setText(appointment.getDate());
        appointmentHolder.textViewDescription.setText(appointment.getDescription());
        appointmentHolder.textViewTime.setText(appointment.getTime());
        appointmentHolder.textViewPayment.setText(appointment.getPayment());
    }

    @NonNull
    @Override
    public AppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item,parent,false);
        return new AppointmentHolder(view);
    }

    public void deleteItem(int position)
    {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    class AppointmentHolder extends RecyclerView.ViewHolder
    {
        TextView textViewService;
        TextView textViewDate;
        TextView textViewDescription;
        TextView textViewPayment;
        TextView textViewTime;

        public AppointmentHolder(@NonNull View itemView) {
            super(itemView);
            textViewService = itemView.findViewById(R.id.text_view_service);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPayment = itemView.findViewById(R.id.text_view_payment);
            textViewTime = itemView.findViewById(R.id.text_view_time);
        }
    }
}
