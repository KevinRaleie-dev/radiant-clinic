package com.example.radiant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.radiant.Classes.ServiceItem;
import com.example.radiant.R;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>
{
    Context sContext;
    List<ServiceItem> sData;

    public ServiceAdapter(Context sContext, List<ServiceItem> sData) {
        this.sContext = sContext;
        this.sData = sData;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout;
        layout = LayoutInflater.from(sContext).inflate(R.layout.row, viewGroup,false);
        return new ServiceViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder serviceViewHolder, int position) {

        //bind new data here

        //photo animation
        serviceViewHolder.img_service.setAnimation(AnimationUtils.loadAnimation(sContext,R.anim.fade_transition_animation));

        //card animation
        serviceViewHolder.container.setAnimation(AnimationUtils.loadAnimation(sContext,R.anim.fade_scale_animation));

        serviceViewHolder.tv_title.setText(sData.get(position).getTitle());
        serviceViewHolder.tv_content.setText(sData.get(position).getContent());
        //serviceViewHolder.tv_date.setText(sData.get(position).getDate());
        serviceViewHolder.img_service.setImageResource(sData.get(position).getUserPhoto());

    }

    @Override
    public int getItemCount() {
        return sData.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder
    {
        //references
        TextView tv_title, tv_content, tv_date;
        ImageView img_service;
        RelativeLayout container;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            //tv_date = itemView.findViewById(R.id.tv_date);
            img_service = itemView.findViewById(R.id.img_service);

        }
    }
}

